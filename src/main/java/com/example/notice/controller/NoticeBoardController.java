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
    public ResponseEntity<PageResponse<NoticeBoard>> getNoticeBoardsWithoutFixed(
            @ModelAttribute NoticeBoardSearchParam noticeBoardSearchParam,
            @Valid @ModelAttribute PageRequest pageRequest) {

        PageResponse<NoticeBoard> noneFixedNoticeBoards =
                noticeBoardService.getNoneFixedNoticeBoards(noticeBoardSearchParam, pageRequest);
        return ResponseEntity
                .ok(noneFixedNoticeBoards);
    }

    /**
     * 게시글 식별자를 통해 공지글을 가져온다.
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
     * 게시글 식별자를 통해 게시글 수정
     * @param noticeBoardId 공지 게시글 식별자
     * @param noticeBoard 공지 게시글 수정 요청 파라미터
     * @return 200 ok
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
     * 게시글 식별자를 통해 게시글 삭제
     * @param noticeBoardId 공지 게시글 식별자
     */
    @DeleteMapping("/admin/boards/notice/{noticeBoardId}")
    public ResponseEntity<Object> deleteNoticeBoard(
            @PathVariable Long noticeBoardId
    ) {
        noticeBoardService.deleteNoticeBoardById(noticeBoardId);

        return ResponseEntity.ok().build();
    }

    //파일 다운로드

    //문의 게시판 생성 - 사용자만
    // 검색
    // 조회
    // 수정
    // 삭제
    // 답변   - 관리자만
    // 답변 삭제

    //자유게시판 - 관리자용
    // 생성
    // 수정
    // 삭제

}
