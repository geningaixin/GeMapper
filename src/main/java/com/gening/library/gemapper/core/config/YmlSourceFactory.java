package com.gening.library.gemapper.core.config;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;
import org.springframework.util.ObjectUtils;

import java.util.Properties;

/**
 * @author G
 * @version 1.0
 * @className YmlSourceFactory
 * @description yml格式文件读取拓展类
 * @date 2022/3/18 16:58
 */
public class YmlSourceFactory implements PropertySourceFactory {

    @Override
    public PropertySource<?> createPropertySource(String name, EncodedResource resource) {
        YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
        factory.setResources(resource.getResource());
        factory.afterPropertiesSet();
        Properties ymlProperties = factory.getObject();
        String propertyName = !ObjectUtils.isEmpty(name) ? name : resource.getResource().getFilename();
        assert propertyName != null;
        assert ymlProperties != null;
        return new PropertiesPropertySource(propertyName, ymlProperties);
    }
}
