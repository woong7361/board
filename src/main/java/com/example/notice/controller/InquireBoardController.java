package com.example.notice.controller;

import com.example.notice.auth.AuthenticationPrincipal;
import com.example.notice.auth.principal.Principal;
import com.example.notice.entity.InquireBoard;
import com.example.notice.entity.Member;
import com.example.notice.service.InquireBoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 문의 게시판 컨트롤러
 */
@RestController
@RequiredArgsConstructor
public class InquireBoardController {

    public static final String INQUIRE_BOARD_ID_PARAM = "inquireBoardId";

    private final InquireBoardService inquireBoardService;

    /**
     * 문의 게시판 게시글 생성
     * @param inquireBoard 문의 게시판 게시글 파라미터
     * @param principal 게시글 생성자 인증 객체
     * @return 문의 게시판 게시글 식별자
     */
    @PostMapping("/api/boards/inquire")
    public ResponseEntity<Map<String, Long>> createInquireBoard(
            @Valid @RequestBody InquireBoard inquireBoard,
            @AuthenticationPrincipal Principal<Member> principal) {
        Member member = principal.getAuthentication();
        Long inquireBoardId = inquireBoardService.createBoard(inquireBoard, member.getMemberId());

        return ResponseEntity
                .ok(Map.of(INQUIRE_BOARD_ID_PARAM, inquireBoardId));
    }


}
