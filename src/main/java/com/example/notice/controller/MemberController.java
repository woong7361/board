package com.example.notice.controller;

import com.example.notice.entity.Member;
import com.example.notice.exception.BadRequestParamException;
import com.example.notice.service.MemberService;
import com.example.notice.validate.group.MemberRegisterValidationGroup;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 회원 컨트롤러
 */
@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    /**
     * 회원가입
     * @param member 새로운 멤버 요청 파라미터
     * @return 200 ok
     */
    @PostMapping("/api/member")
    public ResponseEntity<Object> register(@Validated(MemberRegisterValidationGroup.class) @RequestBody Member member) {
        checkLoginIdSameAsPassword(member);
        memberService.createUserRoleMember(member);

        return ResponseEntity
                .ok()
                .build();
    }


    private void checkLoginIdSameAsPassword(Member member) {
        if (member.getLoginId().equals(member.getPassword())) {
            throw new BadRequestParamException("login Id should not be same as password");
        }
    }
}
