package com.example.notice.controller;

import com.example.notice.auth.AdminAuthenticationPrincipal;
import com.example.notice.auth.AuthenticationPrincipal;
import com.example.notice.auth.principal.Principal;
import com.example.notice.entity.InquireAnswer;
import com.example.notice.entity.Member;
import com.example.notice.service.InquireAnswerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.example.notice.constant.ResponseConstant.INQUIRE_ANSWER_ID_PARAM;

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
    @PostMapping("/api/boards/inquire/{inquireBoardId}/answers")
    public ResponseEntity<Map<String, Long>> createInquireAnswer(
            @Valid @RequestBody InquireAnswer inquireAnswer,
            @PathVariable Long inquireBoardId,
            @AdminAuthenticationPrincipal Principal<Member> principal) {
        Member admin = principal.getAuthentication();

        Long inquireAnswerId = inquireAnswerService.createAnswer(inquireAnswer, inquireBoardId, admin.getMemberId());

        return ResponseEntity
                .ok(Map.of(INQUIRE_ANSWER_ID_PARAM, inquireAnswerId));
    }

    /**
     * 문의 게시판 게시글 문의 답변 삭제
     * @param inquireAnswerId 문의 답변 식별자
     * @return 200 ok
     */
    @DeleteMapping("/api/boards/inquire/answers/{inquireAnswerId}")
    public ResponseEntity<Object> deleteInquireAnswer(
            @PathVariable Long inquireAnswerId) {
        inquireAnswerService.deleteById(inquireAnswerId);

        return ResponseEntity.ok()
                .build();
    }
}
