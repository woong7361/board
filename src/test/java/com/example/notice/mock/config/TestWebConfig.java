package com.example.notice.mock.config;

import com.example.notice.auth.resolvehandler.AdminAuthenticationHolderResolveHandler;
import com.example.notice.auth.resolvehandler.AuthenticationHolderResolveHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

//@Configuration
public class TestWebConfig implements WebMvcConfigurer {

    /**
     * argumentResolvers에 authenticationHolderResolveHandler 추가
     * argumentResolvers에 adminAuthenticationHolderResolveHandler 추가
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authenticationHolderResolveHandler());
        resolvers.add(adminAuthenticationHolderResolveHandler());
    }

    @Bean
    public AuthenticationHolderResolveHandler authenticationHolderResolveHandler() {
        return new AuthenticationHolderResolveHandler();
    }

    @Bean
    public AdminAuthenticationHolderResolveHandler adminAuthenticationHolderResolveHandler() {
        return new AdminAuthenticationHolderResolveHandler();
    }
}
