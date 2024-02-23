package com.ylab.app.test.util;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

/**
 * TestDataReader class
 *
 * @author razlivinsky
 * @since 18.02.2024
 */
public class TestDataReader {
    public String readValueConfigFile(String value) {
        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
        yaml.setResources(new ClassPathResource("test.yml"));
        return yaml.getObject().getProperty(value);
    }
}