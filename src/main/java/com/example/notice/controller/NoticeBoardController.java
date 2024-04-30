package com.example.notice.controller;

import com.example.notice.auth.resolvehandler.AdminAuthenticationPrincipal;
import com.example.notice.auth.principal.Principal;
import com.example.notice.dto.request.NoticeBoardSearchDTO;
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
     * 공지게시판 카테고리 조회
     *
     * @return 카테고리 리스트
     */
    @GetMapping("/api/boards/notice/category")
    public ResponseEntity<Map<String, List<String>>> getFreeBoardCategory() {
        List<String> categories = noticeBoardService.getCategory();

        return ResponseEntity
                .ok(Map.of(CATEGORY_PARAM, categories));
    }

    /**
     * 공지 게시글 생성
     *
     * @param noticeBoard 공지 게시글 생성 파라미터
     * @param principal 관리자 인증 객체
     * @return 생성된 공지 게시글 식별자
     */
    @PostMapping("/admin/boards/notice")
    public ResponseEntity<Map<String, Long>> createNoticeBoard(
            @Valid @RequestBody NoticeBoard noticeBoard,
            @AdminAuthenticationPrincipal Principal<Member> principal
    ) {
        Member member = principal.getAuthentication();
        Long noticeBoardId = noticeBoardService.createNoticeBoard(noticeBoard, member.getMemberId());

        return ResponseEntity
                .ok(Map.of(NOTICE_BOARD_ID_PARAM, noticeBoardId));
    }

    /**
     * 상단 고정 공지 게시글 조회
     *
     * @return 상단 고정 게시글들
     */
    @GetMapping("/api/boards/notice/fixed")
    public ResponseEntity<Map<String, List<NoticeBoard>>> getFixedNoticeBoard(
    ) {
        List<NoticeBoard> noticeBoards = noticeBoardService.getFixedNoticeBoardWithoutContent();

        return ResponseEntity
                .ok(Map.of(FIXED_NOTICE_BOARDS_PARAM, noticeBoards));
    }

    /**
     * 공지 게시글 검색
     * (* 상단 고정 게시글 제외)
     *
     * @param noticeBoardSearchDTO 공지 게시글 검색 파라미터
     * @param pageRequest 페이지네이션 요청 파라미터
     * @return 검색된 게시글 결과
     */
    @GetMapping("/api/boards/notice")
    public ResponseEntity<PageResponse<NoticeBoard>> getNoticeBoardsWithoutFixed(
            @ModelAttribute NoticeBoardSearchDTO noticeBoardSearchDTO,
            @Valid @ModelAttribute PageRequest pageRequest
    ) {
        PageResponse<NoticeBoard> noneFixedNoticeBoards =
                noticeBoardService.getNoneFixedNoticeBoardSearch(noticeBoardSearchDTO, pageRequest);
        return ResponseEntity
                .ok(noneFixedNoticeBoards);
    }

    /**
     * 공지 게시글 조회
     *
     * @param noticeBoardId 공지 게시글 식별자
     * @return 공지 게시글
     */
    @GetMapping("/api/boards/notice/{noticeBoardId}")
    public ResponseEntity<NoticeBoard> getNoticeBoard(
            @PathVariable Long noticeBoardId
    ) {
        NoticeBoard noticeBoard = noticeBoardService.getNoticeBoardById(noticeBoardId);

        return ResponseEntity.ok(noticeBoard);
    }

    /**
     * 공지 게시글 수정
     *
     * @param noticeBoardId 공지 게시글 식별자
     * @param noticeBoard 공지 게시글 수정 요청 파라미터
     * @return 200 OK
     */
    @PutMapping("/admin/boards/notice/{noticeBoardId}")
    public ResponseEntity<Object> updateNoticeBoard(
            @PathVariable Long noticeBoardId,
            @Valid @RequestBody NoticeBoard noticeBoard
    ) {
        noticeBoardService.updateNoticeBoardById(noticeBoardId, noticeBoard);

        return ResponseEntity.ok().build();
    }

    /**
     * 공지 게시글 삭제
     * @param noticeBoardId 공지 게시글 식별자
     */
    @DeleteMapping("/admin/boards/notice/{noticeBoardId}")
    public ResponseEntity<Object> deleteNoticeBoard(
            @PathVariable Long noticeBoardId
    ) {
        noticeBoardService.deleteNoticeBoardById(noticeBoardId);

        return ResponseEntity.ok().build();
    }
}
