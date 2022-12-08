package com.cc.common.exception.base;

import com.cc.common.utils.MessageUtils;
import com.cc.common.utils.StringUtils;

/**
 * 基础异常
 *
 * @author liukang
 */
public abstract class AbstractBaseException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * 所属模块
     */
    private String module;

    /**
     * 错误码
     */
    private String code;

    /**
     * 错误码对应的参数
     */
    private Object[] args;

    /**
     * 错误消息
     */
    private String defaultMessage;

    public AbstractBaseException(String module, String code, Object[] args, String defaultMessage) {
        this.module = module;
        this.code = code;
        this.args = args;
        this.defaultMessage = defaultMessage;
    }

    public AbstractBaseException(String module, String code, Object[] args) {
        this(module, code, args, null);
    }

    public AbstractBaseException(String module, String defaultMessage) {
        this(module, null, null, defaultMessage);
    }

    public AbstractBaseException(String code, Object[] args) {
        this(null, code, args, null);
    }

    public AbstractBaseException(String defaultMessage) {
        this(null, null, null, defaultMessage);
    }

    @Override
    public String getMessage() {
        String message = null;
        if (!StringUtils.isEmpty(code)) {
            message = MessageUtils.message(code, args);
        }
        if (message == null) {
            message = defaultMessage;
        }
        return message;
    }

    public String getModule() {
        return module;
    }

    public String getCode() {
        return code;
    }

    public Object[] getArgs() {
        return args;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }
}
