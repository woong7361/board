package com.example.notice.config;

import com.example.notice.auth.AdminAuthenticationHolderResolveHandler;
import com.example.notice.auth.AuthenticationHolderResolveHandler;
import com.example.notice.auth.filter.AdminSessionInterceptor;
import com.example.notice.auth.filter.JwtTokenAuthFilter;
import com.example.notice.auth.AuthProvider;
import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * webMVC관련 설정 클래스
 * WebMvcConfigurer는 Web설정이므로 WebMvcTest시 로딩되어 bean not Found Exception이 일어날 수 있다.
 */
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final AuthenticationHolderResolveHandler authenticationHolderResolveHandler;
    private final AdminAuthenticationHolderResolveHandler adminAuthenticationHolderResolveHandler;
    private final AdminSessionInterceptor adminSessionInterceptor;
    /**
     * argumentResolvers에 authenticationHolderResolveHandler 추가
     * argumentResolvers에 adminAuthenticationHolderResolveHandler 추가
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authenticationHolderResolveHandler);
        resolvers.add(adminAuthenticationHolderResolveHandler);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminSessionInterceptor)
                .addPathPatterns("/admin/*");
    }
}
