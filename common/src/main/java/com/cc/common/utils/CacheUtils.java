package com.cc.common.utils;

import com.cc.common.config.CommonConfig;
import com.cc.common.constant.Constants;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 缓存处理
 *
 * @author ligaoyuan
 */
public class CacheUtils {
    //在线用户缓存keys
    private static Set<String> tokenCacheKeys = new HashSet<>();
    //参数配置缓存keys
    private static Set<String> sysConfigCacheKeys = new HashSet<>();

    /**
     * 设置缓存
     *
     * @param key       键
     * @param value     值
     * @param timeout   超时时间
     * @param timeUnit  时间单位
     * @param cacheName ehCache的缓存名称
     */
    public static void putCacheObject(String key, Object value, Integer timeout, TimeUnit timeUnit, String cacheName) {
        //重复提交有5000ms的有效期
        //令牌有30min的有效期
        //验证码有2min的有效期
        //密码锁定时间有10min的有效期
        Cache cache = SpringUtils.getBean(CacheManager.class).getCache(cacheName);
        Element element = new Element(key, value);
        cache.put(element);
    }


    /**
     * 设置缓存
     *
     * @param key   键
     * @param value 值
     */
    public static void putCacheObject(String key, Object value) {
        putCacheObject(key, value, CommonConfig.isEhCacheEnabled());
    }

    /**
     * 设置缓存
     *
     * @param key            键
     * @param value          值
     * @param ehCacheEnabled 是否开启ehCache缓存
     */
    public static void putCacheObject(String key, Object value, boolean ehCacheEnabled) {
        Cache cache = SpringUtils.getBean(CacheManager.class).getCache(Constants.DEFAULT_EHCACHE);
        Element element = new Element(key, value);
        cache.put(element);
    }

    /**
     * 获取缓存
     *
     * @param key 键
     */
    public static <T> T getCacheObject(String key) {
        return getCacheObject(key, Constants.DEFAULT_EHCACHE);
    }

    /**
     * 获取缓存
     *
     * @param key 键
     */
    public static <T> T getCacheObject(String key, String cacheName) {
        Cache cache = SpringUtils.getBean(CacheManager.class).getCache(cacheName);
        Element element = cache.get(key);
        if (element != null) {
            return (T) element.getObjectValue();
        } else {
            return null;
        }
    }


    /**
     * 删除缓存
     *
     * @param key 键
     */
    public static void deleteCacheObject(String key) {
        Cache cache = SpringUtils.getBean(CacheManager.class).getCache(Constants.DEFAULT_EHCACHE);
        cache.remove(key);
    }

    /**
     * 增加在线用户缓存key
     *
     * @param key 键值
     */
    public static void addTokenCacheKey(String key) {
        tokenCacheKeys.add(key);
    }

    /**
     * 删除在线用户缓存key
     *
     * @param key 键值
     */
    public static void removeTokenCacheKey(String key) {
        tokenCacheKeys.remove(key);
    }

    /**
     * 获取在线用户缓存keys
     */
    public static Set<String> getTokenCacheKeys() {
        return tokenCacheKeys;
    }

    /**
     * 增加参数配置缓存key
     *
     * @param key 键值
     */
    public static void addConfigCacheKey(String key) {
        sysConfigCacheKeys.add(key);
    }

    /**
     * 删除参数配置缓存key
     *
     * @param key 键值
     */
    public static void removeConfigCacheKey(String key) {
        sysConfigCacheKeys.remove(key);
    }

    /**
     * 获取参数配置缓存keys
     */
    public static Set<String> getConfigCacheKeys() {
        return sysConfigCacheKeys;
    }
}
