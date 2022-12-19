package com.cc.system.dao.impl;

import com.cc.common.po.entity.SysUser;
import com.cc.system.dao.ISysUserDao;

import java.util.List;

public class SysUserDaoImpl implements ISysUserDao {
    @Override
    public List<SysUser> selectUserList(SysUser sysUser) {
        return null;
    }

    @Override
    public List<SysUser> selectAllocatedList(SysUser user) {
        return null;
    }

    @Override
    public List<SysUser> selectUnallocatedList(SysUser user) {
        return null;
    }

    @Override
    public SysUser selectUserByUserName(String userName) {
        return null;
    }

    @Override
    public SysUser selectUserById(Long userId) {
        return null;
    }

    @Override
    public int insertUser(SysUser user) {
        return 0;
    }

    @Override
    public int updateUser(SysUser user) {
        return 0;
    }

    @Override
    public int updateUserAvatar(String userName, String avatar) {
        return 0;
    }

    @Override
    public int resetUserPwd(String userName, String password) {
        return 0;
    }

    @Override
    public int deleteUserById(Long userId) {
        return 0;
    }

    @Override
    public int deleteUserByIds(Long[] userIds) {
        return 0;
    }

    @Override
    public SysUser checkUserNameUnique(String userName) {
        return null;
    }

    @Override
    public SysUser checkPhoneUnique(String phonenumber) {
        return null;
    }

    @Override
    public SysUser checkEmailUnique(String email) {
        return null;
    }
}
