package com.example.notice.config;

import com.example.notice.auth.AuthenticationHolderResolveHandler;
import com.example.notice.auth.filter.JwtTokenAuthFilter;
import com.example.notice.auth.AuthProvider;
import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final AuthenticationHolderResolveHandler authenticationHolderResolveHandler;

    /**
     * argumentResolvers에 authenticationHolderResolveHandler 추가
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authenticationHolderResolveHandler);
    }
}
