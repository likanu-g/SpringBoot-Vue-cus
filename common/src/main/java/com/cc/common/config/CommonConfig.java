package com.cc.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 读取项目相关配置
 *
 * @author liukang
 */
@Component
@ConfigurationProperties(prefix = "common")
public class CommonConfig {
    /**
     * 上传路径
     */
    private static String profile;
    /**
     * 获取地址开关
     */
    private static boolean addressEnabled;
    /**
     * 验证码类型
     */
    private static String captchaType;
    /**
     * 没有数据库，通过配置文件读写数据
     */
    private static boolean noDatabaseEnabled;
    //是否开启ehCache缓存
    private static boolean ehCacheEnabled;
    /**
     * 项目名称
     */
    @Value("${common.application.name}")
    private String name;
    /**
     * 版本
     */
    @Value("${common.application.version}")
    private String version;
    /**
     * 版权年份
     */
    @Value("${common.application.copyrightYear}")
    private String copyrightYear;

    public static boolean isNoDatabaseEnabled() {
        return noDatabaseEnabled;
    }

    public void setNoDatabaseEnabled(boolean noDatabaseEnabled) {
        CommonConfig.noDatabaseEnabled = noDatabaseEnabled;
    }

    public static String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        CommonConfig.profile = profile;
    }

    public static boolean isAddressEnabled() {
        return addressEnabled;
    }

    public void setAddressEnabled(boolean addressEnabled) {
        CommonConfig.addressEnabled = addressEnabled;
    }

    public static String getCaptchaType() {
        return captchaType;
    }

    public void setCaptchaType(String captchaType) {
        CommonConfig.captchaType = captchaType;
    }

    public static boolean isEhCacheEnabled() {
        return ehCacheEnabled;
    }

    public void setEhCacheEnabled(boolean ehCacheEnabled) {
        CommonConfig.ehCacheEnabled = ehCacheEnabled;
    }

    /**
     * 获取导入上传路径
     */
    public static String getImportPath() {
        return getProfile() + "/import";
    }

    /**
     * 获取头像上传路径
     */
    public static String getAvatarPath() {
        return getProfile() + "/avatar";
    }

    /**
     * 获取下载路径
     */
    public static String getDownloadPath() {
        return getProfile() + "/download/";
    }

    /**
     * 获取上传路径
     */
    public static String getUploadPath() {
        return getProfile() + "/upload";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getCopyrightYear() {
        return copyrightYear;
    }

    public void setCopyrightYear(String copyrightYear) {
        this.copyrightYear = copyrightYear;
    }

}
