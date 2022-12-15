package com.cc.framework.web.service;

import com.cc.common.constant.CacheConstants;
import com.cc.common.constant.Constants;
import com.cc.common.exception.user.UserPasswordNotMatchException;
import com.cc.common.exception.user.UserPasswordRetryLimitExceedException;
import com.cc.common.po.entity.SysUser;
import com.cc.common.utils.CacheUtils;
import com.cc.common.utils.MessageUtils;
import com.cc.common.utils.SecurityUtils;
import com.cc.framework.manager.AsyncManager;
import com.cc.framework.manager.factory.AsyncFactory;
import com.cc.framework.security.context.AuthenticationContextHolder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 登录密码方法
 *
 * @author liukang
 */
@Component
public class SysPasswordService {
    @Value(value = "${user.password.maxRetryCount}")
    private int maxRetryCount;

    @Value(value = "${user.password.lockTime}")
    private int lockTime;

    /**
     * 登录账户密码错误次数缓存键名
     *
     * @param username 用户名
     * @return 缓存键key
     */
    private String getCacheKey(String username) {
        return CacheConstants.PWD_ERR_CNT_KEY + username;
    }

    public void validate(SysUser user) {
        Authentication usernamePasswordAuthenticationToken = AuthenticationContextHolder.getContext();
        String username = usernamePasswordAuthenticationToken.getName();
        String password = usernamePasswordAuthenticationToken.getCredentials().toString();

        Integer retryCount = CacheUtils.getCacheObject(getCacheKey(username), Constants.PWD_ERR_CNT_EHCACHE);

        if (retryCount == null) {
            retryCount = 0;
        }

        if (retryCount >= maxRetryCount) {
            AsyncManager.me().execute(AsyncFactory.recordLoginInfo(username, Constants.LOGIN_FAIL,
                    MessageUtils.message("user.password.retry.limit.exceed", maxRetryCount, lockTime)));
            throw new UserPasswordRetryLimitExceedException(maxRetryCount, lockTime);
        }

        if (!matches(user, password)) {
            retryCount = retryCount + 1;
            AsyncManager.me().execute(AsyncFactory.recordLoginInfo(username, Constants.LOGIN_FAIL,
                    MessageUtils.message("user.password.retry.limit.count", retryCount)));
            CacheUtils.putCacheObject(getCacheKey(username), retryCount, lockTime, TimeUnit.MINUTES, Constants.PWD_ERR_CNT_EHCACHE);
            throw new UserPasswordNotMatchException();
        } else {
            clearLoginRecordCache(username);
        }
    }

    public boolean matches(SysUser user, String rawPassword) {
        return SecurityUtils.matchesPassword(rawPassword, user.getPassword());
    }

    public void clearLoginRecordCache(String loginName) {
        CacheUtils.deleteCacheObject(getCacheKey(loginName));
    }
}
