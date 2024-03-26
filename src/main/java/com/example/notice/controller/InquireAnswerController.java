package com.example.notice.controller;

import com.example.notice.auth.AdminAuthenticationPrincipal;
import com.example.notice.auth.principal.Principal;
import com.example.notice.entity.InquireAnswer;
import com.example.notice.entity.Member;
import com.example.notice.service.InquireAnswerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 문의 게시판 답변 컨트롤러
 */
@RestController
@RequiredArgsConstructor
public class InquireAnswerController {

    public static final String INQUIRE_ANSWER_ID_PARAM = "inquireAnswerId";

    private final InquireAnswerService inquireAnswerService;

    /**
     * 문의게시판 문의에 대한 답변 생성
     * @param inquireAnswer 문의 게시판 답변 생성 파라미터
     * @param inquireBoardId 문의 게시판 게시글 식별자
     * @param principal 답변하는 관리자의 인증 객체
     * @return 문의의 생성된 답변 식별자
     */
    @PostMapping("/api/boards/inquire/{inquireBoardId}/answers")
    public ResponseEntity<Map<String, Long>> createInquireAnswer(
            @Valid @RequestBody InquireAnswer inquireAnswer,
            @PathVariable String inquireBoardId,
            @AdminAuthenticationPrincipal Principal<Member> principal) {
        Member admin = principal.getAuthentication();

        Long inquireAnswerId = inquireAnswerService.createAnswer(inquireAnswer, inquireBoardId, admin.getMemberId());

        return ResponseEntity
                .ok(Map.of(INQUIRE_ANSWER_ID_PARAM, inquireAnswerId));
    }
}
