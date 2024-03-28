package com.example.notice.mock.config;

import com.example.notice.auth.filter.JwtTokenInterceptor;
import com.example.notice.config.WebConfig;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@WebMvcTest(
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
                        classes = {WebConfig.class, JwtTokenInterceptor.class})}
)
@Import(TestWebConfig.class)
public @interface NoFilterMvcTest{

    @AliasFor("controllers")
    Class<?>[] value() default {};

    @AliasFor("value")
    Class<?>[] controllers() default {};
}
