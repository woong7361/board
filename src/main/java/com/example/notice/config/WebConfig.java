package com.example.notice.config;

import com.example.notice.auth.AdminAuthenticationHolderResolveHandler;
import com.example.notice.auth.AuthenticationHolderResolveHandler;
import com.example.notice.auth.filter.AdminSessionInterceptor;
import com.example.notice.auth.filter.JwtTokenAuthFilter;
import com.example.notice.auth.AuthProvider;
import com.example.notice.auth.filter.JwtTokenInterceptor;
import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * webMVC관련 설정 클래스
 */
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final AuthenticationHolderResolveHandler authenticationHolderResolveHandler;
    private final AdminAuthenticationHolderResolveHandler adminAuthenticationHolderResolveHandler;
    private final AdminSessionInterceptor adminSessionInterceptor;

    private final JwtTokenInterceptor jwtTokenInterceptor;


    /**
     * argumentResolvers에 authenticationHolderResolveHandler 추가
     * argumentResolvers에 adminAuthenticationHolderResolveHandler 추가
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authenticationHolderResolveHandler);
        resolvers.add(adminAuthenticationHolderResolveHandler);
    }

    /**
     *  인증관련 interceptor 추가
     *  - 관리자 인증
     *  - 사용자 인증
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminSessionInterceptor)
                .addPathPatterns("/admin/**");

        registry.addInterceptor(jwtTokenInterceptor)
                .addPathPatterns("/api/**");
    }

    /**
     * cors 관련 설정
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5174", "http://localhost:5173", "http://localhost:3000")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("Authorization", "Content-Type")
                .exposedHeaders("content-disposition");
    }

}
