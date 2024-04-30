package com.example.notice.config;

import com.example.notice.auth.filter.AuthorizationInterceptor;
import com.example.notice.auth.path.AuthorizationRole;
import com.example.notice.auth.path.PathMethod;
import com.example.notice.auth.resolvehandler.AdminAuthenticationHolderResolveHandler;
import com.example.notice.auth.resolvehandler.AuthenticationHolderResolveHandler;
import com.example.notice.auth.filter.AdminSessionInterceptor;
import com.example.notice.auth.filter.JwtTokenInterceptor;
import lombok.RequiredArgsConstructor;
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
    private final AuthorizationInterceptor authorizationInterceptor;
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
     *  - 사용자 인가
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminSessionInterceptor)
                .addPathPatterns("/admin/**");

        registry.addInterceptor(jwtTokenInterceptor)
                .addPathPatterns("/api/**");

        registry.addInterceptor(authorizationInterceptor)
                .addPathPatterns("/api/**");

        setInterceptorPatterns(authorizationInterceptor);
    }

    private void setInterceptorPatterns(AuthorizationInterceptor authorizationInterceptor) {
        authorizationInterceptor.includePathPatterns("/api/**", PathMethod.ANY, AuthorizationRole.MEMBER);

        authorizationInterceptor.includePathPatterns("/api/**", PathMethod.ANY, AuthorizationRole.GUEST);

        authorizationInterceptor.excludePathPatterns("/api/boards/free", PathMethod.POST, AuthorizationRole.GUEST);
        authorizationInterceptor.excludePathPatterns("/api/boards/free", PathMethod.PUT, AuthorizationRole.GUEST);
        authorizationInterceptor.excludePathPatterns("/api/boards/free", PathMethod.DELETE, AuthorizationRole.GUEST);
        authorizationInterceptor.excludePathPatterns("/api/boards/free/*/comments", PathMethod.POST, AuthorizationRole.GUEST);
        authorizationInterceptor.excludePathPatterns("/api/boards/free/*/comments", PathMethod.DELETE, AuthorizationRole.GUEST);
        authorizationInterceptor.excludePathPatterns("/api/boards/inquire", PathMethod.POST, AuthorizationRole.GUEST);
        authorizationInterceptor.excludePathPatterns("/api/boards/inquire", PathMethod.PUT, AuthorizationRole.GUEST);
        authorizationInterceptor.excludePathPatterns("/api/boards/inquire", PathMethod.DELETE, AuthorizationRole.GUEST);
    }

    /**
     * cors 관련 설정
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5173", "http://localhost:3000", "http://13.125.243.251")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("Authorization", "Content-Type")
                .exposedHeaders("content-disposition")
                .allowCredentials(true);
    }

}
