package com.estore.authenticationauthorizationservice

import com.estore.authenticationauthorizationservice.user.UserEntity
import com.estore.authenticationauthorizationservice.user.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.password.PasswordEncoder

import javax.annotation.PostConstruct

@TestConfiguration
class AuthenticationAuthorizationServiceTestsConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    UserRepository userRepository

    @Autowired
    PasswordEncoder passwordEncoder

    @PostConstruct
    void setupToken() {
        UserEntity u = new UserEntity()
        u.setUsername("test_user")
        u.setPassword(passwordEncoder.encode("test_user"))
        userRepository.save(u)
    }
}
