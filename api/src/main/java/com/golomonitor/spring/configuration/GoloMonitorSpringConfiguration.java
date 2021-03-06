package com.golomonitor.spring.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Created by abelov on 02/08/17.
 */

@Configuration
@EnableWebMvc
@ComponentScan("com.golomonitor")
@PropertySource("file:/golomonitor/config/golomonitor.properties")
public class GoloMonitorSpringConfiguration {
}
