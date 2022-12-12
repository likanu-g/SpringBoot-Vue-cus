package com.cc.controller.monitor;

import com.cc.common.annotation.Log;
import com.cc.common.constant.CacheConstants;
import com.cc.common.constant.Constants;
import com.cc.common.controller.BaseController;
import com.cc.common.enums.BusinessType;
import com.cc.common.po.AjaxResult;
import com.cc.common.po.model.LoginUser;
import com.cc.common.po.page.TableDataInfo;
import com.cc.common.utils.CacheUtils;
import com.cc.common.utils.StringUtils;
import com.cc.framework.web.service.TokenService;
import com.cc.system.po.SysUserOnline;
import com.cc.system.service.ISysUserOnlineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 在线用户监控
 *
 * @author liukang
 */
@RestController
@RequestMapping("/monitor/online")
public class SysUserOnlineController extends BaseController {
    private final ThreadLocal<Boolean> saved = new ThreadLocal<>();
    @Autowired
    private ISysUserOnlineService userOnlineService;
    @Autowired
    private TokenService tokenService;

    @PreAuthorize("@ss.hasPermit('monitor:online:list')")
    @GetMapping("/list")
    public TableDataInfo list(String ipaddr, String userName) {
        Collection<String> keys = CacheUtils.getTokenCacheKeys();
        List<SysUserOnline> userOnlineList = new ArrayList<>();
        for (String key : keys) {
            LoginUser user = CacheUtils.getCacheObject(key, Constants.TOKEN_EHCACHE);
            if (StringUtils.isNotEmpty(ipaddr) && StringUtils.isNotEmpty(userName)) {
                assert user != null;
                if (StringUtils.equals(ipaddr, user.getIpaddr()) && StringUtils.equals(userName, user.getUsername())) {
                    userOnlineList.add(userOnlineService.selectOnlineByInfo(ipaddr, userName, user));
                }
            } else if (StringUtils.isNotEmpty(ipaddr)) {
                assert user != null;
                if (StringUtils.equals(ipaddr, user.getIpaddr())) {
                    userOnlineList.add(userOnlineService.selectOnlineByIpaddr(ipaddr, user));
                }
            } else if (StringUtils.isNotEmpty(userName) && StringUtils.isNotNull(Objects.requireNonNull(user).getUser())) {
                if (StringUtils.equals(userName, user.getUsername())) {
                    userOnlineList.add(userOnlineService.selectOnlineByUserName(userName, user));
                }
            } else {
                userOnlineList.add(userOnlineService.loginUserToUserOnline(user));
            }
        }
        Collections.reverse(userOnlineList);
        userOnlineList.removeAll(Collections.singleton(null));
        return getDataTable(userOnlineList);
    }

    /**
     * 强退用户
     */
    @PreAuthorize("@ss.hasPermit('monitor:online:forceLogout')")
    @Log(title = "在线用户", businessType = BusinessType.FORCE)
    @DeleteMapping("/{tokenId}")
    public AjaxResult forceLogout(@PathVariable String tokenId) {
        CacheUtils.removeTokenCacheKey(CacheConstants.LOGIN_TOKEN_KEY + tokenId);
        CacheUtils.deleteCacheObject(CacheConstants.LOGIN_TOKEN_KEY + tokenId);
        return success();
    }
}
