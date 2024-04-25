package com.example.notice.controller;

import com.example.notice.auth.resolvehandler.AdminAuthenticationPrincipal;
import com.example.notice.auth.principal.Principal;
import com.example.notice.entity.InquireAnswer;
import com.example.notice.entity.Member;
import com.example.notice.service.InquireAnswerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 문의 게시판 답변 컨트롤러
 */
@RestController
@RequiredArgsConstructor
public class InquireAnswerController {


    private final InquireAnswerService inquireAnswerService;

    /**
     * 문의게시판 문의에 대한 답변 생성
     *
     * @param inquireAnswer  문의 게시판 답변 생성 파라미터
     * @param inquireBoardId 문의 게시판 게시글 식별자
     * @param principal      답변하는 관리자의 인증 객체
     * @return 문의의 생성된 답변 식별자
     */
    @PostMapping("/admin/boards/inquire/{inquireBoardId}/answers")
    public ResponseEntity<Object> createInquireAnswer(
            @Valid @RequestBody InquireAnswer inquireAnswer,
            @PathVariable Long inquireBoardId,
            @AdminAuthenticationPrincipal Principal<Member> principal) {
        Member admin = principal.getAuthentication();

        inquireAnswerService.createAnswer(inquireAnswer, inquireBoardId, admin.getMemberId());

        return ResponseEntity.ok()
                .build();
    }

    /**
     * 문의 게시판 게시글 문의 답변 삭제
     * @param inquireAnswerId 문의 답변 식별자
     * @return 200 ok
     */
    @DeleteMapping("/admin/boards/inquire/answers/{inquireAnswerId}")
    public ResponseEntity<Object> deleteInquireAnswer(
            @PathVariable Long inquireAnswerId) {
        inquireAnswerService.deleteById(inquireAnswerId);

        return ResponseEntity.ok()
                .build();
    }
}
