package com.cc.common.po.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 用户注册对象
 *
 * @author liukang
 */
public class RegisterBody extends LoginBody {
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
