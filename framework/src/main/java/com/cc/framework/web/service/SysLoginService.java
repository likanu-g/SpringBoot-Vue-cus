package com.cc.framework.web.service;

import com.cc.common.constant.CacheConstants;
import com.cc.common.constant.Constants;
import com.cc.common.exception.ServiceException;
import com.cc.common.exception.user.CaptchaException;
import com.cc.common.exception.user.CaptchaExpireException;
import com.cc.common.exception.user.UserPasswordNotMatchException;
import com.cc.common.po.entity.SysUser;
import com.cc.common.po.model.LoginUser;
import com.cc.common.utils.*;
import com.cc.common.utils.ip.IpUtils;
import com.cc.framework.manager.AsyncManager;
import com.cc.framework.manager.factory.AsyncFactory;
import com.cc.framework.security.context.AuthenticationContextHolder;
import com.cc.system.service.ISysConfigService;
import com.cc.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 登录校验方法
 *
 * @author liukang
 */
@Component
public class SysLoginService {
    @Autowired
    private TokenService tokenService;

    @Resource
    private AuthenticationManager authenticationManager;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysConfigService configService;

    /**
     * 登录验证
     *
     * @param username 用户名
     * @param password 密码
     * @param code     验证码
     * @param uuid     唯一标识
     * @return 结果
     */
    public String login(String username, String password, String code, String uuid) {
        boolean captchaEnabled = configService.selectCaptchaEnabled();
        // 验证码开关
        if (captchaEnabled) {
            validateCaptcha(username, code, uuid);
        }
        // 用户验证
        Authentication authentication;
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
            AuthenticationContextHolder.setContext(authenticationToken);
            // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
            authentication = authenticationManager.authenticate(authenticationToken);
        } catch (Exception e) {
            if (e instanceof BadCredentialsException) {
                AsyncManager.me().execute(AsyncFactory.recordLoginInfo(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
                throw new UserPasswordNotMatchException();
            } else {
                AsyncManager.me().execute(AsyncFactory.recordLoginInfo(username, Constants.LOGIN_FAIL, e.getMessage()));
                throw new ServiceException(e.getMessage());
            }
        } finally {
            AuthenticationContextHolder.clearContext();
        }
        //记录登录信息
        AsyncManager.me().execute(AsyncFactory.recordLoginInfo(username, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
        //获取身份授权后的用户
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        recordLoginInfo(loginUser.getUserId());
        // 生成token
        return tokenService.createToken(loginUser);
    }

    /**
     * 校验验证码
     *
     * @param username 用户名
     * @param code     验证码
     * @param uuid     唯一标识
     */
    public void validateCaptcha(String username, String code, String uuid) {
        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + StringUtils.nvl(uuid, "");

        String captcha = CacheUtils.getCacheObject(verifyKey, Constants.CAPTCH_EHCACHE);
        CacheUtils.deleteCacheObject(verifyKey);
        if (captcha == null) {
            AsyncManager.me().execute(AsyncFactory.recordLoginInfo(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.expire")));
            throw new CaptchaExpireException();
        }
        if (!code.equalsIgnoreCase(captcha)) {
            AsyncManager.me().execute(AsyncFactory.recordLoginInfo(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.error")));
            throw new CaptchaException();
        }
    }

    /**
     * 记录登录信息
     *
     * @param userId 用户ID
     */
    public void recordLoginInfo(Long userId) {
        SysUser sysUser = new SysUser();
        sysUser.setUserId(userId);
        sysUser.setLoginIp(IpUtils.getIpAddr(ServletUtils.getRequest()));
        sysUser.setLoginDate(DateUtils.getNowDate());
        userService.updateUserProfile(sysUser);
    }
}
