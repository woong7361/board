package com.example.notice.controller;

import com.example.notice.auth.AdminAuthenticationPrincipal;
import com.example.notice.auth.AuthenticationPrincipal;
import com.example.notice.auth.principal.Principal;
import com.example.notice.constant.ResponseConstant;
import com.example.notice.dto.request.FreeBoardSearchDTO;
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
import org.springframework.web.multipart.MultipartFile;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

import static com.example.notice.constant.ResponseConstant.FREE_BOARD_ID_PARAM;

/**
 * 자유 게시판 컨트롤러
 */
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
            @Valid @ModelAttribute FreeBoard freeBoard,
            @RequestParam(required = false) List<MultipartFile> files,
            @AuthenticationPrincipal Principal<Member> principal
    ) {
        Member member = principal.getAuthentication();
        freeBoard.setOwner(member);

        Long freeBoardId = freeBoardService.createFreeBoard(freeBoard, files, member.getMemberId());

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
            @PathVariable Long freeBoardId
    ) {
        FreeBoard freeBoard = freeBoardService.getBoardById(freeBoardId);

        return ResponseEntity
                .ok(freeBoard);
    }

    /**
     * 자유게시판 게시글 리스트 조회/검색
     *
     * @param freeBoardSearchDTO 게시글 검색 파라미터
     * @param pageRequest          페이지네이션 요청 파라미터
     * @return 게시글 페이지
     */
    @GetMapping("/api/boards/free")
    public ResponseEntity<PageResponse<FreeBoard>> getFreeBoards(
            @ModelAttribute FreeBoardSearchDTO freeBoardSearchDTO,
            @Valid @ModelAttribute PageRequest pageRequest
    ) {
        checkSearchRange(freeBoardSearchDTO);

        PageResponse<FreeBoard> boards = freeBoardService.getBoardsBySearchParams(freeBoardSearchDTO, pageRequest);

        return ResponseEntity.ok(boards);
    }

    /**
     * 자유게시판 게시글 삭제
     *
     * @return 200 ok
     */
    @DeleteMapping("/api/boards/free/{freeBoardId}")
    public ResponseEntity<Object> deleteFreeBoard(
            @PathVariable Long freeBoardId,
            @AuthenticationPrincipal Principal<Member> principal) {
        Member member = principal.getAuthentication();
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
            @Valid @ModelAttribute FreeBoard freeBoard,
            @RequestParam List<MultipartFile> saveFiles,
            @RequestParam List<Long> deleteFileIds,
            @PathVariable Long freeBoardId,
            @AuthenticationPrincipal Principal<Member> principal) {
        Member member = principal.getAuthentication();
        freeBoardService.checkFreeBoardAuthorization(freeBoardId, member.getMemberId());

        freeBoardService.updateFreeBoardById(freeBoard, saveFiles, deleteFileIds, freeBoardId);

        return ResponseEntity
                .ok(Map.of(FREE_BOARD_ID_PARAM, freeBoardId));
    }

    /**
     * 관리자 자유게시판 게시글 생성
     * @param freeBoard 게시글 생성 파라미터
     * @param files 게시글 첨부파일들
     * @param principal 관리자 인증 객체
     * @return 생성된 자유게시판 식별자
     */
    @PostMapping("/admin/boards/free")
    public ResponseEntity<Map<String, Long>> createFreeBoardByAdmin(
            @Valid @ModelAttribute FreeBoard freeBoard,
            @RequestParam List<MultipartFile> files,
            @AdminAuthenticationPrincipal Principal<Member> principal
    ) {
        Member admin = principal.getAuthentication();

        Long freeBoardId = freeBoardService.createFreeBoard(freeBoard, files, admin.getMemberId());

        return ResponseEntity
                .ok(Map.of(FREE_BOARD_ID_PARAM, freeBoardId));
    }

    /**
     * 관리자 자유게시판 게시글 삭제
     * @param freeBoardId 자유게시판 게시글 식별자
     * @return 200 ok
     */
    @DeleteMapping("/admin/boards/free/{freeBoardId}")
    public ResponseEntity<Object> deleteFreeBoardByAdmin(
            @PathVariable Long freeBoardId) {
        freeBoardService.deleteFreeBoardById(freeBoardId);

        return ResponseEntity
                .ok()
                .build();
    }

    /**
     * 관리자 자유게시판 게시글 수정
     * @param freeBoard 게시글 수정 파라미터
     * @param saveFiles 추가로 저장할 첨부파일들
     * @param deleteFileIds 삭제할 첨부파일 식별자들
     * @param freeBoardId 수정할 자유게시판 게시글 식별자
     * @return 200 ok
     */
    @PutMapping("/admin/boards/free/{freeBoardId}")
    public ResponseEntity<Object> updateFreeBoardByAdmin(
            @Valid @ModelAttribute FreeBoard freeBoard,
            @RequestParam List<MultipartFile> saveFiles,
            @RequestParam List<Long> deleteFileIds,
            @PathVariable Long freeBoardId) {
        freeBoardService.updateFreeBoardById(freeBoard, saveFiles, deleteFileIds, freeBoardId);

        return ResponseEntity
                .ok(Map.of(FREE_BOARD_ID_PARAM, freeBoardId));
    }

    private static void checkSearchRange(FreeBoardSearchDTO freeBoardSearchDTO) {
        if (freeBoardSearchDTO.getEndDate() == null || freeBoardSearchDTO.getStartDate() == null) {
            return;
        }

        if (isSearchRangeMoreThan1Year(freeBoardSearchDTO)) {
            throw new BadRequestParamException("최대 날짜 범위는 1년 이하 입니다.");
        }
    }

    private static boolean isSearchRangeMoreThan1Year(FreeBoardSearchDTO freeBoardSearchDTO) {
        return ChronoUnit.YEARS.between(freeBoardSearchDTO.getStartDate(), freeBoardSearchDTO.getEndDate()) > 0;
    }
}


















