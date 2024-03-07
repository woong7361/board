package com.example.notice.controller;

import com.example.notice.auth.AuthenticationPrincipal;
import com.example.notice.auth.principal.Principal;
import com.example.notice.entity.Member;
import com.example.notice.service.AuthService;
import com.example.notice.validate.group.MemberLoginValidationGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 인증에 관련된 컨트롤러
 */
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * 로그인 컨트롤러
     * @param member 회원 정보
     * @return 인증된 jwt token
     */
    //TODO URI 디자인?
    @PostMapping("/api/auth/login")
    public ResponseEntity<Map<String, String>> login(
            @Validated(MemberLoginValidationGroup.class) @RequestBody Member member) {
        String token = authService.login(member);

        Map<String, String> body = new HashMap<>();
        body.put("token", token);

        return ResponseEntity
                .ok(body);
    }
}
