package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    private static final String[] AUTH_WHITELIST = {
            "/v1/mytel/send",
            "/v1/smspoh/send"
    };

    @Value("${security.oauth2.resource.id}")
    private String RESOURCE_ID;

    @Autowired
    private RestAuthenticationEntryPoint authEntryPoint;

    @Override
    public void configure(ResourceServerSecurityConfigurer security) throws Exception
    {
        security.resourceId(RESOURCE_ID);
        security.authenticationEntryPoint(authEntryPoint);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception
    {  http
            .csrf().disable().cors().and()
            .authorizeRequests()
            .antMatchers(AUTH_WHITELIST).permitAll()
            .anyRequest().authenticated();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
