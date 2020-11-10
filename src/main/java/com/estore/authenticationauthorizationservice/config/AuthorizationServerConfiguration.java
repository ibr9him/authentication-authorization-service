package com.estore.authenticationauthorizationservice.config;

import com.estore.authenticationauthorizationservice.authentication.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;

@RequiredArgsConstructor
@EnableAuthorizationServer
@Configuration
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

//    @Value("${spring.datasource.url}")
//    private String datasourceUrl;
//
//    @Value("${spring.datasource.username}")
//    private String dbUsername;
//
//    @Value("${spring.datasource.password}")
//    private String dbPassword;

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final AuthenticationService authenticationService;

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()");
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("R2dpxQ3vPrtfgF72")
                .secret(passwordEncoder.encode("fDw7Mpkk5czHNuSRtmhGmAGL42CaxQB9"))
                .authorizedGrantTypes("authorization_code")
                .scopes("user_info")
                .and()
                .withClient("management-system")
                .secret(passwordEncoder.encode("managementsystem"))
                .authorizedGrantTypes("authorization_code")
                .scopes("user_info")
                .and()
                .withClient("postman")
                .secret(passwordEncoder.encode("postman"))
                .authorizedGrantTypes("password","refresh_token")
                .scopes("user_info")
                .accessTokenValiditySeconds(99999999)
                .refreshTokenValiditySeconds(99999999);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .authenticationManager(authenticationManager)
                .userDetailsService(authenticationService);
//                .tokenStore(tokenStore());
    }

//    @Bean
//    public TokenStore tokenStore() {
//        return new JdbcTokenStore(dataSource());
//    }

//    @Bean
//    public DataSource dataSource() {
//        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setUrl(datasourceUrl);
//        dataSource.setUsername(dbUsername);
//        dataSource.setPassword(dbPassword);
//        return dataSource;
//    }
}
