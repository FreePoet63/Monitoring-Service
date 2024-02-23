package com.ylab.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * JdbcTemplateConfig class represents the configuration class for setting up the JdbcTemplate.
 *
 * This class provides the configuration for creating a JdbcTemplate bean using the specified DataSource.
 *
 * @author razlivinsky
 * @since 14.02.2024
 */
@Configuration
public class JdbcTemplateConfig {
    private final DataSource dataSource;

    /**
     * Instantiates a new Jdbc template config.
     *
     * @param dataSource the data source
     */
    public JdbcTemplateConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Configures and creates a JdbcTemplate bean.
     *
     * @return the configured JdbcTemplate
     */
    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource);
    }
}
