package com.example.managementsystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Optional;

@EnableJpaAuditing
@EnableTransactionManagement
@Configuration
public class DataAccessConfiguration {

    /**
     * Bean to process and evaluate the currently logged in user for auditing
     */
    @Profile(value = {"!Test"})
    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @Profile(value = {"Test"})
    static class DataAccessTestConfiguration {
        @Bean
        public AuditorAware<String> auditorAware() {
            return () -> Optional.of("Dummy User");
        }
    }
}
