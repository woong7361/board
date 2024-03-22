package com.example.notice.controller;

import com.example.notice.auth.AuthenticationPrincipal;
import com.example.notice.auth.principal.Principal;
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

import java.time.temporal.ChronoUnit;
import java.util.Map;

import static com.example.notice.constant.ResponseConstant.FREE_BOARD_ID_PARAM;

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
                .ok(Map.of(FREE_BOARD_ID_PARAM, freeBoardId));
    }

    /**
     * 자유게시판 게시글 조회
     *
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
     *
     * @param freeBoardSearchParam 게시글 검색 파라미터
     * @param pageRequest          페이지네이션 요청 파라미터
     * @return 게시글 페이지
     */
    @GetMapping("/api/boards/free")
    public ResponseEntity<PageResponse<FreeBoard>> getFreeBoards(
            @ModelAttribute FreeBoardSearchParam freeBoardSearchParam,
            @Valid @ModelAttribute PageRequest pageRequest
    ) {
        checkSearchRange(freeBoardSearchParam);

        PageResponse<FreeBoard> boards = freeBoardService.getBoardsBySearchParams(freeBoardSearchParam, pageRequest);

        return ResponseEntity.ok(boards);
    }

    /**
     * 자유게시판 게시글 삭제
     *
     * @return 200 ok
     */
    @DeleteMapping("/api/boards/free/{freeBoardId}")
    public ResponseEntity<Object> deleteFreeBoard(
            @PathVariable(name = "freeBoardId") Long freeBoardId,
            @AuthenticationPrincipal Principal<Member> principal) {
        Member member = principal.getAuthentication();
        //
        freeBoardService.checkFreeBoardAuthorization(freeBoardId, member.getMemberId());

        freeBoardService.deleteFreeBoardById(freeBoardId);
        return ResponseEntity.ok().build();
    }

    /**
     * 자유게시판 게시글 수정
     * @param freeBoard 게시글 수정 인자
     * @param freeBoardId 게시글 식별자
     * @param principal 인증된 사용자 정보
     * @return 200 ok
     */
    @PutMapping("/api/boards/free/{freeBoardId}")
    public ResponseEntity<Object> updateBoard(
            @Valid @RequestBody FreeBoard freeBoard,
            @PathVariable(name = "freeBoardId") Long freeBoardId,
            @AuthenticationPrincipal Principal<Member> principal) {
        Member member = principal.getAuthentication();
        freeBoardService.checkFreeBoardAuthorization(freeBoardId, member.getMemberId());

        // TODO 구조를 어떻게 해야할지 - setter? - 새로이 build? - 그대로?
        freeBoardService.updateFreeBoardById(freeBoard, freeBoardId);

        return ResponseEntity
                .ok(Map.of(FREE_BOARD_ID_PARAM, freeBoardId));
    }

    private static void checkSearchRange(FreeBoardSearchParam freeBoardSearchParam) {
        if (freeBoardSearchParam.getEndDate() == null || freeBoardSearchParam.getStartDate() == null) {
            return;
        }

        if (isSearchRangeMoreThan1Year(freeBoardSearchParam)) {
            throw new BadRequestParamException("최대 날짜 범위는 1년 이하 입니다.");
        }
    }

    private static boolean isSearchRangeMoreThan1Year(FreeBoardSearchParam freeBoardSearchParam) {
        return ChronoUnit.YEARS.between(freeBoardSearchParam.getStartDate(), freeBoardSearchParam.getEndDate()) > 0;
    }
}


















