//package com.example.managementsystem.config;
//
//import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class GlobalConfiguration implements WebMvcConfigurer {
//
//    /**
//     * Bean to propagate the SecurityContextHolder to async method calls
//     */
//    @Bean
//    public MethodInvokingFactoryBean methodInvokingFactoryBean() {
//        MethodInvokingFactoryBean methodInvokingFactoryBean = new MethodInvokingFactoryBean();
//        methodInvokingFactoryBean.setTargetClass(SecurityContextHolder.class);
//        methodInvokingFactoryBean.setTargetMethod("setStrategyName");
//        methodInvokingFactoryBean.setArguments(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
//        return methodInvokingFactoryBean;
//    }
//}
