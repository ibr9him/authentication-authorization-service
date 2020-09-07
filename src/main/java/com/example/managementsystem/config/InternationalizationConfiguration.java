//package com.example.managementsystem.config;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.MessageSource;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.support.ResourceBundleMessageSource;
//import org.springframework.web.servlet.LocaleResolver;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
//import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
//import org.springframework.web.servlet.i18n.SessionLocaleResolver;
//
//import java.util.Locale;
//
//@Slf4j
//@RequiredArgsConstructor
//@Configuration
//public class InternationalizationConfiguration extends HandlerInterceptorAdapter implements WebMvcConfigurer {
//
//    @Bean
//    public MessageSource messageSource() {
//        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
//        messageSource.setBasename("i18n/languages");
//        messageSource.setDefaultEncoding("UTF-8");
//        messageSource.setFallbackToSystemLocale(false);
//        messageSource.setCacheSeconds(0);
//        return messageSource;
//    }
//
//    /**
//     * To determine which locale is currently being used.
//     * Default is English
//     */
//    @Bean
//    public LocaleResolver localeResolver() {
//        SessionLocaleResolver sessionLocaleResolver = new SessionLocaleResolver();
//        sessionLocaleResolver.setDefaultLocale(Locale.ENGLISH);
//        return sessionLocaleResolver;
//    }
//
//    /**
//     * An interceptor bean that will switch to a new locale based on the value of
//     * the lang parameter appended to a request
//     */
//    @Bean
//    public LocaleChangeInterceptor localeChangeInterceptor() {
//        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
//        localeChangeInterceptor.setParamName("lang");
//        return localeChangeInterceptor;
//    }
//
//    /**
//     * Adding language interceptor to the application registry
//     */
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(localeChangeInterceptor());
//    }
//}
