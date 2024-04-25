package com.example.notice.auth.resolvehandler;



import java.lang.annotation.*;

/**
 * parameter에 인증 객체를 주입시켜준다.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthenticationPrincipal {

}
