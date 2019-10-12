package com.liseh.config;

import com.liseh.auth.service.impl.ClientDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

@EnableAuthorizationServer
@Configuration
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {
//    @Autowired
//    @Qualifier("authenticationManagerBean")
//    private AuthenticationManager authenticationManager;
    private final ClientDetailsServiceImpl clientDetailsService;

    public AuthServerConfig(ClientDetailsServiceImpl clientDetailsService) {
        this.clientDetailsService = clientDetailsService;
    }

    @Bean
    public TokenStore tokenStore() {
        InMemoryTokenStore tokenStore = new InMemoryTokenStore();
        return tokenStore;
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                .passwordEncoder(passwordEncoder())
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("permitAll()"); // permitAll() / isAuthenticated()
    }

/*    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.jdbc(dataSource())
                .withClient("sampleClientId")
                .authorizedGrantTypes("implicit")
                .scopes("read")
                .autoApprove(true)
                .and()
                .withClient("clientIdPassword")
                .secret("secret")
                .authorizedGrantTypes("password","authorization_code", "refresh_token")
                .scopes("read");
    }*/

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetailsService);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenStore(tokenStore());
//                .authenticationManager(authenticationManager);
    }

//    @Bean
//    public DataSource dataSource() {
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        return dataSource;
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
