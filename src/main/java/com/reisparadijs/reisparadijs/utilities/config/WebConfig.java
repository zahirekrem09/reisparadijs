package com.reisparadijs.reisparadijs.utilities.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Zahir Ekrem SARITEKE
 * @project reisparadijs
 * @created 25 August Sunday 2024 - 12:40
 */

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/auth/login").setViewName("forward:/pages/auth/login/login.html");
        registry.addViewController("/auth/register").setViewName("forward:/pages/auth/register/register.html");
        registry.addViewController("/auth/forgot-password").setViewName("forward:/pages/auth/forgot-password/forgot-password.html");
        registry.addViewController("/auth/reset-password").setViewName("forward:/pages/auth/reset-password/reset-password.html");
        registry.addViewController("/").setViewName("forward:/pages/home/index.html");
        registry.addViewController("/dashboard").setViewName("forward:/pages/dashboard/dashboard.html");
        registry.addViewController("/dashboard/messages").setViewName("forward:/pages/dashboard/messages/messages.html");
//        registry.addViewController("/house-detail").setViewName("forward:/house-detail.html");
    }


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
    }
}