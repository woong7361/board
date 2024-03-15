package com.example.notice.controller;

import com.example.notice.auth.AdminAuthenticationPrincipal;
import com.example.notice.auth.principal.Principal;
import com.example.notice.constant.ResponseConstant;
import com.example.notice.entity.Member;
import com.example.notice.entity.NoticeBoard;
import com.example.notice.service.NoticeBoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 공지 게시판 컨트롤러
 */
@RestController
@RequiredArgsConstructor
public class NoticeBoardController {
    private final NoticeBoardService noticeBoardService;

    /**
     * 공지 게시판 생성 엔드포인트
     *
     * @param noticeBoard 공지 게시판 생성 인자
     * @param principal   관리자 인증 객체
     * @return 생성된 공지 게시판 식별자
     */
    @PostMapping("/admin/boards/notice")
    public ResponseEntity<Map<String, Long>> createNoticeBoard(
            @Valid @RequestBody NoticeBoard noticeBoard,
            @AdminAuthenticationPrincipal Principal<Member> principal) {
        Member member = principal.getAuthentication();

        Long noticeBoardId = noticeBoardService.createNoticeBoard(noticeBoard, member.getMemberId());

        return ResponseEntity
                .ok(Map.of(ResponseConstant.NOTICE_BOARD_ID_PARAM, noticeBoardId));
    }

    /**
     * 상단 고정된 공지 게시글을 가져온다.
     *
     * @return 상단 고정된 공지 게시글들
     */
    @GetMapping("/api/boards/notice/fixed")
    public ResponseEntity<Map<String, List<NoticeBoard>>> getNoticeBoard(
    ) {
        List<NoticeBoard> noticeBoards = noticeBoardService.getFixedNoticeBoardWithoutContent();

        return ResponseEntity
                .ok(Map.of(ResponseConstant.BOARDS_PARAM, noticeBoards));
    }

    // 고정 공지글 뺀 나머지 공지 검색

    // 공지글 상세 보기

    // 공지글 수정

    // 공지글 삭제

    //문의 게시판 생성
    // 검색
    // 조회
    // 수정
    // 삭제
    // 답변
    // 답변 삭제

}
