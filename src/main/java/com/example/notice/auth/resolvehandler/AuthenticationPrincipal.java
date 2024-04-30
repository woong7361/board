package com.example.notice.auth.resolvehandler;



import java.lang.annotation.*;

/**
 * 회원 인증객체 주입 annotation
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthenticationPrincipal {

}
