package com.example.notice.controller;

import com.example.notice.auth.AdminAuthenticationPrincipal;
import com.example.notice.auth.principal.Principal;
import com.example.notice.dto.NoticeBoardSearchParam;
import com.example.notice.entity.Member;
import com.example.notice.entity.NoticeBoard;
import com.example.notice.page.PageRequest;
import com.example.notice.page.PageResponse;
import com.example.notice.service.NoticeBoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.example.notice.constant.ResponseConstant.*;

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
                .ok(Map.of(NOTICE_BOARD_ID_PARAM, noticeBoardId));
    }

    /**
     * 상단 고정된 공지 게시글을 가져온다.
     *
     * @return 상단 고정된 공지 게시글들
     */
    @GetMapping("/api/boards/notice/fixed")
    public ResponseEntity<Map<String, List<NoticeBoard>>> getFixedNoticeBoard(
    ) {
        List<NoticeBoard> noticeBoards = noticeBoardService.getFixedNoticeBoardWithoutContent();

        return ResponseEntity
                .ok(Map.of(FIXED_NOTICE_BOARDS_PARAM, noticeBoards));
    }

    /**
     * 고정으로 반환된 공지글 말고 남은 공지글을 반환
     *
     * @param noticeBoardSearchParam 공지글 검색 파라미터
     * @param pageRequest            페이지 요청 파라미터
     * @return 고정 공지글이 아닌 공지글들 반환
     */
    @GetMapping("/api/boards/notice")
    public ResponseEntity<Map<String, PageResponse<NoticeBoard>>> getNoticeBoardsWithoutFixed(
            @ModelAttribute NoticeBoardSearchParam noticeBoardSearchParam,
            @ModelAttribute PageRequest pageRequest) {

        PageResponse<NoticeBoard> noneFixedNoticeBoards =
                noticeBoardService.getNoneFixedNoticeBoards(noticeBoardSearchParam, pageRequest);
        return ResponseEntity
                .ok(Map.of(NONE_FIXED_NOTICE_BOARDS_PARAM, noneFixedNoticeBoards));
    }

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
