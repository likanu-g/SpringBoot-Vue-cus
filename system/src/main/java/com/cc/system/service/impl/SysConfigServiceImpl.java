package com.cc.system.service.impl;

import com.cc.common.annotation.DataSource;
import com.cc.common.constant.CacheConstants;
import com.cc.common.constant.UserConstants;
import com.cc.common.enums.DataSourceType;
import com.cc.common.exception.ServiceException;
import com.cc.common.utils.CacheUtils;
import com.cc.common.utils.StringUtils;
import com.cc.common.utils.text.Convert;
import com.cc.system.dao.ISysConfigDao;
import com.cc.system.po.SysConfig;
import com.cc.system.service.ISysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Iterator;
import java.util.List;

/**
 * 参数配置 服务层实现
 *
 * @author liukang
 */
@Service
public class SysConfigServiceImpl implements ISysConfigService {
    @Autowired
    private ISysConfigDao ISysConfigDao;

    @Value("${common.ehCacheEnabled}")
    private boolean ehCacheEnabled;

    /**
     * 项目启动时，初始化参数到缓存
     */
    @PostConstruct
    public void init() {
        loadingConfigCache();
    }

    /**
     * 查询参数配置信息
     *
     * @param configId 参数配置ID
     * @return 参数配置信息
     */
    @Override
    @DataSource(DataSourceType.MASTER)
    public SysConfig selectConfigById(Long configId) {
        SysConfig config = new SysConfig();
        config.setConfigId(configId);
        return ISysConfigDao.selectConfig(config);
    }

    /**
     * 根据键名查询参数配置信息
     *
     * @param configKey 参数key
     * @return 参数键值
     */
    @Override
    public String selectConfigByKey(String configKey) {
        String configStr = CacheUtils.getCacheObject(getCacheKey(configKey));
        String configValue = Convert.toStr(configStr);
        if (StringUtils.isNotEmpty(configValue)) {
            return configValue;
        }
        SysConfig config = new SysConfig();
        config.setConfigKey(configKey);
        SysConfig retConfig = ISysConfigDao.selectConfig(config);
        if (StringUtils.isNotNull(retConfig)) {
            CacheUtils.addConfigCacheKey(getCacheKey(configKey));
            CacheUtils.putCacheObject(getCacheKey(configKey), retConfig.getConfigValue());
            return retConfig.getConfigValue();
        }
        return StringUtils.EMPTY;
    }

    /**
     * 获取验证码开关
     *
     * @return true开启，false关闭
     */
    @Override
    public boolean selectCaptchaEnabled() {
        String captchaEnabled = selectConfigByKey("sys.account.captchaEnabled");
        if (StringUtils.isEmpty(captchaEnabled)) {
            return true;
        }
        return Convert.toBool(captchaEnabled);
    }

    /**
     * 查询参数配置列表
     *
     * @param config 参数配置信息
     * @return 参数配置集合
     */
    @Override
    public List<SysConfig> selectConfigList(SysConfig config) {
        return ISysConfigDao.selectConfigList(config);
    }

    /**
     * 新增参数配置
     *
     * @param config 参数配置信息
     * @return 结果
     */
    @Override
    public int insertConfig(SysConfig config) {
        int row = ISysConfigDao.insertConfig(config);
        if (row > 0) {
            CacheUtils.addConfigCacheKey(getCacheKey(config.getConfigKey()));
            CacheUtils.putCacheObject(getCacheKey(config.getConfigKey()), config.getConfigValue());
        }
        return row;
    }

    /**
     * 修改参数配置
     *
     * @param config 参数配置信息
     * @return 结果
     */
    @Override
    public int updateConfig(SysConfig config) {
        int row = ISysConfigDao.updateConfig(config);
        if (row > 0) {
            CacheUtils.addConfigCacheKey(getCacheKey(config.getConfigKey()));
            CacheUtils.putCacheObject(getCacheKey(config.getConfigKey()), config.getConfigValue());
        }
        return row;
    }

    /**
     * 批量删除参数信息
     *
     * @param configIds 需要删除的参数ID
     */
    @Override
    public void deleteConfigByIds(Long[] configIds) {
        for (Long configId : configIds) {
            SysConfig config = selectConfigById(configId);
            if (StringUtils.equals(UserConstants.YES, config.getConfigType())) {
                throw new ServiceException(String.format("内置参数【%1$s】不能删除 ", config.getConfigKey()));
            }
            ISysConfigDao.deleteConfigById(configId);
            CacheUtils.removeConfigCacheKey(getCacheKey(config.getConfigKey()));
            CacheUtils.deleteCacheObject(getCacheKey(config.getConfigKey()));
        }
    }

    /**
     * 加载参数缓存数据
     */
    @Override
    public void loadingConfigCache() {
        List<SysConfig> configsList = ISysConfigDao.selectConfigList(new SysConfig());
        for (SysConfig config : configsList) {
            CacheUtils.addConfigCacheKey(getCacheKey(config.getConfigKey()));
            //PostConstruct 是在实例化之后，才会执行，而RuoYiConfig这个时候还没有拿配置文件的数据，导致RuoYiConfig.isEhCacheEnabled()取值错误
            //所以ehCacheEnabled修改为@Value 方式获取
            CacheUtils.putCacheObject(getCacheKey(config.getConfigKey()), config.getConfigValue(), ehCacheEnabled);
        }
    }

    /**
     * 清空参数缓存数据
     */
    @Override
    public void clearConfigCache() {
        Iterator<String> iterator = CacheUtils.getConfigCacheKeys().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            CacheUtils.deleteCacheObject(key);
            iterator.remove();
        }
    }

    /**
     * 重置参数缓存数据
     */
    @Override
    public void resetConfigCache() {
        clearConfigCache();
        loadingConfigCache();
    }

    /**
     * 校验参数键名是否唯一
     *
     * @param config 参数配置信息
     * @return 结果
     */
    @Override
    public String checkConfigKeyUnique(SysConfig config) {
        long configId = StringUtils.isNull(config.getConfigId()) ? -1L : config.getConfigId();
        SysConfig info = ISysConfigDao.checkConfigKeyUnique(config.getConfigKey());
        if (StringUtils.isNotNull(info) && info.getConfigId() != configId) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 设置cache key
     *
     * @param configKey 参数键
     * @return 缓存键key
     */
    private String getCacheKey(String configKey) {
        return CacheConstants.SYS_CONFIG_KEY + configKey;
    }
}
