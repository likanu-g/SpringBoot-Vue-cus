package com.cc.common.exception.user;

import com.cc.common.exception.base.AbstractBaseException;

/**
 * 用户信息异常类
 *
 * @author liukang
 */
public class UserException extends AbstractBaseException {
    private static final long serialVersionUID = 1L;

    public UserException(String code, Object[] args) {
        super("user", code, args, null);
    }
}
