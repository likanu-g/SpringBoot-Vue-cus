package com.cc.common.exception.file;

import com.cc.common.exception.base.AbstractBaseException;

/**
 * 文件信息异常类
 *
 * @author liukang
 */
public class FileException extends AbstractBaseException {
    private static final long serialVersionUID = 1L;

    public FileException(String code, Object[] args) {
        super("file", code, args, null);
    }

}
