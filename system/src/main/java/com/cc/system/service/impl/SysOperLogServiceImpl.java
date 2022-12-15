package com.cc.system.service.impl;

import com.cc.system.dao.ISysOperLogDao;
import com.cc.system.po.SysOperLog;
import com.cc.system.service.ISysOperLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 操作日志 服务层处理
 *
 * @author liukang
 */
@Service
public class SysOperLogServiceImpl implements ISysOperLogService {
    @Autowired
    private ISysOperLogDao ISysOperLogDao;

    /**
     * 新增操作日志
     *
     * @param operLog 操作日志对象
     */
    @Override
    public void insertOperlog(SysOperLog operLog) {
        ISysOperLogDao.insertOperlog(operLog);
    }

    /**
     * 查询系统操作日志集合
     *
     * @param operLog 操作日志对象
     * @return 操作日志集合
     */
    @Override
    public List<SysOperLog> selectOperLogList(SysOperLog operLog) {
        return ISysOperLogDao.selectOperLogList(operLog);
    }

    /**
     * 批量删除系统操作日志
     *
     * @param operIds 需要删除的操作日志ID
     * @return 结果
     */
    @Override
    public int deleteOperLogByIds(Long[] operIds) {
        return ISysOperLogDao.deleteOperLogByIds(operIds);
    }

    /**
     * 查询操作日志详细
     *
     * @param operId 操作ID
     * @return 操作日志对象
     */
    @Override
    public SysOperLog selectOperLogById(Long operId) {
        return ISysOperLogDao.selectOperLogById(operId);
    }

    /**
     * 清空操作日志
     */
    @Override
    public void cleanOperLog() {
        ISysOperLogDao.cleanOperLog();
    }
}
