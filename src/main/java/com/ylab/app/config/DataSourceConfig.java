package com.ylab.app.config;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.util.Objects;

/**
 * DataSourceConfig class provides the configuration for setting up the data source.
 *
 * This class reads the data source configuration from the "application.yml" file and creates a DataSource bean.
 *
 * @author razlivinsky
 * @since 17.02.2024
 */
@Configuration
public class DataSourceConfig {
    /**
     * Creates and configures a data source bean.
     *
     * @return the configured data source
     */
    @Bean
    public DataSource dataSource() {
        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
        yaml.setResources(new ClassPathResource("application.yml"));
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(Objects.requireNonNull(yaml.getObject()).getProperty("spring.datasource.driver-class-name"));
        dataSource.setUrl(yaml.getObject().getProperty("spring.datasource.url"));
        dataSource.setUsername(yaml.getObject().getProperty("spring.datasource.username"));
        dataSource.setPassword(yaml.getObject().getProperty("spring.datasource.password"));
        return dataSource;
    }
}
