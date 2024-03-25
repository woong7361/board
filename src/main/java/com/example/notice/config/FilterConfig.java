package com.example.notice.config;

import com.example.notice.auth.AuthProvider;
import com.example.notice.auth.filter.JwtTokenAuthFilter;
import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 필터 설정 클래스
 */
@Configuration
@RequiredArgsConstructor
public class FilterConfig {

    private final AuthProvider authProvider;

    /**
     * 인증 필터 설정 및 bean 등록
     */
    @Bean
    public FilterRegistrationBean authFilter() {
        FilterRegistrationBean<Filter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
        filterFilterRegistrationBean.setFilter(new JwtTokenAuthFilter(authProvider));
        filterFilterRegistrationBean.setOrder(1);
        filterFilterRegistrationBean.addUrlPatterns("/api/*");

        return filterFilterRegistrationBean;
    }
}
