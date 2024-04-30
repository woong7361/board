package com.example.notice.controller;

import com.example.notice.dto.response.AdminLoginResponseDTO;
import com.example.notice.entity.Member;
import com.example.notice.service.AuthService;
import com.example.notice.validate.group.MemberLoginValidationGroup;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static com.example.notice.constant.ResponseConstant.TOKEN_PARAM;
import static com.example.notice.constant.SessionConstant.ADMIN_SESSION_KEY;

/**
 * 인증에 관련 컨트롤러
 */
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * 일반 유저 로그인
     *
     * @param member 회원 로그인 요청 파라미터
     * @return 인증된 jwt token
     */
    @PostMapping("/auth/member/login")
    public ResponseEntity<Map<String, String>> userLogin(
            @Validated(MemberLoginValidationGroup.class) @RequestBody Member member
    ) {
        String token = authService.userAuthentication(member);

        return ResponseEntity
                .ok(Map.of(TOKEN_PARAM, token));
    }

    /**
     * 관리자 로그인
     *
     * @param member 로그인 요청 파라미터
     * @return 관리자 정보와 관리자 세션 만료시간
     */
    @PostMapping("/auth/admin/login")
    public ResponseEntity<AdminLoginResponseDTO> adminLogin(
            @Validated(MemberLoginValidationGroup.class) @RequestBody Member member,
            HttpSession httpSession
    ) {
        Member adminMember = authService.adminAuthentication(member);

        httpSession.setAttribute(ADMIN_SESSION_KEY, adminMember);

        AdminLoginResponseDTO response = AdminLoginResponseDTO.builder()
                .memberId(adminMember.getMemberId())
                .name(adminMember.getName())
                .sessionTimeOut(httpSession.getMaxInactiveInterval())
                .build();

        return ResponseEntity.ok(response);
    }

    /**
     * 로그인 아이디 중복 확인
     * @param loginId 로그인 아이디
     * @return 200 OK 응답
     */
    @PostMapping("/auth/member/login-id")
    public ResponseEntity<Object> checkDuplicateLoginId(
            @RequestBody @Valid String loginId
    ) {
        authService.checkDuplicateLoginId(loginId);

        return ResponseEntity
                .ok()
                .build();
    }
}
