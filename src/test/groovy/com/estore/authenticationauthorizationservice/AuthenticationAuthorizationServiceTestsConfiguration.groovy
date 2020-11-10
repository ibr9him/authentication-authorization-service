package com.estore.authenticationauthorizationservice


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.password.PasswordEncoder

import javax.annotation.PostConstruct

@TestConfiguration
class AuthenticationAuthorizationServiceTestsConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    com.estore.authenticationauthorizationservice.user.UserRepository userRepository

    @Autowired
    PasswordEncoder passwordEncoder

    @PostConstruct
    void setupToken() {
        com.estore.authenticationauthorizationservice.user.UserEntity u = new com.estore.authenticationauthorizationservice.user.UserEntity()
        u.setUsername("test_user")
        u.setPassword(passwordEncoder.encode("test_user"))
        userRepository.save(u)
    }
}
