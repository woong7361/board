package com.example.notice.controller;

import com.example.notice.auth.AuthenticationPrincipal;
import com.example.notice.auth.principal.Principal;
import com.example.notice.constant.ResponseConstant;
import com.example.notice.dto.FreeBoardSearchParam;
import com.example.notice.exception.BadRequestParamException;
import com.example.notice.page.PageRequest;
import com.example.notice.entity.FreeBoard;
import com.example.notice.entity.Member;
import com.example.notice.page.PageResponse;
import com.example.notice.service.FreeBoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;

/**
 * 자유 게시판 컨트롤러
 */
// TODO MSA의 transactional 처리?
@RestController
@RequiredArgsConstructor
public class FreeBoardController {

    private final FreeBoardService freeBoardService;

    /**
     * 자유게시판 게시글 생성
     *
     * @param freeBoard 게시글 요청 인자
     * @param principal 인증된 사용자
     * @return 게시글 식별자
     */
    @PostMapping("/api/boards/free")
    public ResponseEntity<Map<String, Long>> createFreeBoard(
            @Valid @RequestBody FreeBoard freeBoard,
            @AuthenticationPrincipal Principal<Member> principal
    ) {
        Member member = principal.getAuthentication();
        freeBoard.setOwner(member);

        Long freeBoardId = freeBoardService.createFreeBoard(freeBoard);

        return ResponseEntity
                .ok(Map.of(ResponseConstant.FREE_BOARD_ID_PARAM, freeBoardId));
    }

    /**
     * 자유게시판 게시글 조회
     * @param freeBoardId 게시글 식별자
     * @return 게시글 내용
     */
    @GetMapping("/api/boards/free/{freeBoardId}")
    public ResponseEntity<FreeBoard> getFreeBoard(
            @PathVariable(name = "freeBoardId") Long freeBoardId
    ) {
        FreeBoard freeBoard = freeBoardService.getBoardById(freeBoardId);

        return ResponseEntity
                .ok(freeBoard);
    }

    /**
     * 자유게시판 게시글 리스트 조회/검색
     * @param freeBoardSearchParam 게시글 검색 파라미터
     * @param pageRequest 페이지네이션 요청 파라미터
     * @return 게시글 페이지
     */
    @GetMapping("/api/boards/free")
    public ResponseEntity<PageResponse<FreeBoard>> getFreeBoards(
            @ModelAttribute FreeBoardSearchParam freeBoardSearchParam,
            @Valid @ModelAttribute PageRequest pageRequest
    ) {
        if (ChronoUnit.YEARS.between(freeBoardSearchParam.getStartDate(), freeBoardSearchParam.getEndDate()) > 0) {
            throw new BadRequestParamException("최대 날짜 범위는 1년 이하 입니다.");
        }

        PageResponse<FreeBoard> boards = freeBoardService.getBoardsBySearchParams(freeBoardSearchParam, pageRequest);

        return ResponseEntity.ok(boards);
    }
}


















