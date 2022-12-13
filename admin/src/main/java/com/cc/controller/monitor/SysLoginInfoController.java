package com.cc.controller.monitor;

import com.cc.common.annotation.Log;
import com.cc.common.controller.BaseController;
import com.cc.common.enums.BusinessType;
import com.cc.common.po.AjaxResult;
import com.cc.common.po.page.TableDataInfo;
import com.cc.common.utils.poi.ExcelUtil;
import com.cc.framework.web.service.SysPasswordService;
import com.cc.system.po.SysLoginInfo;
import com.cc.system.service.ISysLoginInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 系统访问记录
 *
 * @author liukang
 */
@RestController
@RequestMapping("/monitor/logininfor")
public class SysLoginInfoController extends BaseController {
    @Autowired
    private ISysLoginInfoService logininforService;

    @Autowired
    private SysPasswordService passwordService;

    @PreAuthorize("@ss.hasPermit('monitor:logininfor:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysLoginInfo logininfor) {
        startPage();
        List<SysLoginInfo> list = logininforService.selectLogininforList(logininfor);
        return getDataTable(list);
    }

    @Log(title = "登录日志", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermit('monitor:logininfor:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysLoginInfo logininfor) {
        List<SysLoginInfo> list = logininforService.selectLogininforList(logininfor);
        ExcelUtil<SysLoginInfo> util = new ExcelUtil<>(SysLoginInfo.class);
        util.exportExcel(response, list, "登录日志");
    }

    @PreAuthorize("@ss.hasPermit('monitor:logininfor:remove')")
    @Log(title = "登录日志", businessType = BusinessType.DELETE)
    @DeleteMapping("/{infoIds}")
    public AjaxResult remove(@PathVariable Long[] infoIds) {
        return toAjax(logininforService.deleteLogininforByIds(infoIds));
    }

    @PreAuthorize("@ss.hasPermit('monitor:logininfor:remove')")
    @Log(title = "登录日志", businessType = BusinessType.CLEAN)
    @DeleteMapping("/clean")
    public AjaxResult clean() {
        logininforService.cleanLogininfor();
        return success();
    }

    @PreAuthorize("@ss.hasPermit('monitor:logininfor:unlock')")
    @Log(title = "账户解锁", businessType = BusinessType.OTHER)
    @GetMapping("/unlock/{userName}")
    public AjaxResult unlock(@PathVariable("userName") String userName) {
        passwordService.clearLoginRecordCache(userName);
        return success();
    }
}
