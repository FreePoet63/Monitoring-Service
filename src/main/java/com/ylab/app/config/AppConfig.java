package com.ylab.app.config;

import com.ylab.app.aspect.EnableLogging;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * AppConfig class represents the main configuration class for the application.
 *
 * This class includes configuration for enabling AspectJ auto proxy and component scanning.
 *
 * @author razlivinsky
 * @since 17.02.2024
 */
@Configuration
@EnableAspectJAutoProxy
@ComponentScan("com.ylab.app")
public class AppConfig {
}