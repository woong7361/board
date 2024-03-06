package com.example.notice.config;

import com.example.notice.filter.JwtTokenAuthFilter;
import com.example.notice.service.AuthProvider;
import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class WebConfig {
    private final AuthProvider authProvider;

    @Bean
    public FilterRegistrationBean authFilter() {
        FilterRegistrationBean<Filter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
        filterFilterRegistrationBean.setFilter(new JwtTokenAuthFilter(authProvider));
        filterFilterRegistrationBean.setOrder(1);
        filterFilterRegistrationBean.addUrlPatterns("/api/*");

        return filterFilterRegistrationBean;
    }
}
