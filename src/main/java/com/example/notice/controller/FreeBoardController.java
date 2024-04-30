package com.example.notice.controller;

import com.example.notice.auth.resolvehandler.AdminAuthenticationPrincipal;
import com.example.notice.auth.resolvehandler.AuthenticationPrincipal;
import com.example.notice.auth.principal.Principal;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.example.notice.constant.ResponseConstant.CATEGORY_PARAM;
import static com.example.notice.constant.ResponseConstant.FREE_BOARD_ID_PARAM;

/**
 * 자유 게시판 컨트롤러
 */
@RestController
@RequiredArgsConstructor
public class FreeBoardController {

    private final FreeBoardService freeBoardService;

    /**
     * 자유게시판 카테고리 조회
     *
     * @return 카테고리 리스트
     */
    @GetMapping("/api/boards/free/category")
    public ResponseEntity<Map<String, List<String>>> getFreeBoardCategories() {
        List<String> categories = freeBoardService.getCategories();

        return ResponseEntity
                .ok(Map.of(CATEGORY_PARAM, categories));
    }

    /**
     * 자유게시판 게시글 생성
     *
     * @param freeBoard 게시글 생성 파라미터
     * @param principal 인증된 사용자 객체
     * @return 생성된 게시글 식별자
     */
    @PostMapping("/api/boards/free")
    public ResponseEntity<Map<String, Long>> createFreeBoard(
            @Valid @ModelAttribute FreeBoard freeBoard,
            @RequestParam(required = false) List<MultipartFile> files,
            @AuthenticationPrincipal Principal<Member> principal
    ) {
        if (files == null) {
            files = List.of();
        }

        Member member = principal.getAuthentication();
        Long freeBoardId = freeBoardService.createFreeBoard(freeBoard, files, member.getMemberId());

        return ResponseEntity
                .ok(Map.of(FREE_BOARD_ID_PARAM, freeBoardId));
    }

    /**
     * 자유게시판 게시글 조회
     *
     * @param freeBoardId 게시글 식별자
     * @return 게시글
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
     * @param pageRequest 페이지네이션 요청 파라미터
     * @return 게시글 페이지 정보
     */
    @GetMapping("/api/boards/free")
    public ResponseEntity<PageResponse<FreeBoard>> getFreeBoards(
            @ModelAttribute FreeBoardSearchDTO freeBoardSearchDTO,
            @Valid @ModelAttribute PageRequest pageRequest
    ) {
        PageResponse<FreeBoard> boards = freeBoardService.getBoardsBySearchParams(freeBoardSearchDTO, pageRequest);

        return ResponseEntity.ok(boards);
    }

    /**
     * 자유게시판 게시글 삭제
     *
     * @param freeBoardId 게시글 식별자
     * @param principal 회원 인증 객체
     * @return 200 OK
     */
    @DeleteMapping("/api/boards/free/{freeBoardId}")
    public ResponseEntity<Object> deleteFreeBoard(
            @PathVariable Long freeBoardId,
            @AuthenticationPrincipal Principal<Member> principal) {
        Member member = principal.getAuthentication();

        freeBoardService.deleteFreeBoardById(freeBoardId, member.getMemberId());
        return ResponseEntity.ok().build();
    }

    /**
     * 자유게시판 게시글 수정
     *
     * @param freeBoard 게시글 수정 파라미터
     * @param freeBoardId 게시글 식별자
     * @param saveFiles 추가할 파일들
     * @param deleteFileIds 삭제할 파일 식별자들
     * @param principal 인증된 사용자 정보
     * @return 200 OK
     */
    @PutMapping("/api/boards/free/{freeBoardId}")
    public ResponseEntity<Object> updateBoard(
            @Valid @ModelAttribute FreeBoard freeBoard,
            @RequestParam(required = false) List<MultipartFile> saveFiles,
            @RequestParam(required = false) List<Long> deleteFileIds,
            @PathVariable Long freeBoardId,
            @AuthenticationPrincipal Principal<Member> principal
    ) {
        if (saveFiles == null) {
            saveFiles = List.of();
        }
        if (deleteFileIds == null) {
            deleteFileIds = List.of();
        }

        Member member = principal.getAuthentication();
        freeBoardService.updateFreeBoardById(freeBoard, saveFiles, deleteFileIds, freeBoardId, member.getMemberId());

        return ResponseEntity
                .ok()
                .build();
    }

    /**
     * 관리자 자유게시판 게시글 생성
     *
     * @param freeBoard 게시글 생성 파라미터
     * @param files 게시글 첨부파일들
     * @param principal 관리자 인증 객체
     * @return 생성된 자유게시판 식별자
     */
    @PostMapping("/admin/boards/free")
    public ResponseEntity<Map<String, Long>> createFreeBoardByAdmin(
            @Valid @ModelAttribute FreeBoard freeBoard,
            @RequestParam(required = false) List<MultipartFile> files,
            @AdminAuthenticationPrincipal Principal<Member> principal
    ) {
        if (files == null) {
            files = List.of();
        }

        Member admin = principal.getAuthentication();
        Long freeBoardId = freeBoardService.createFreeBoard(freeBoard, files, admin.getMemberId());

        return ResponseEntity
                .ok(Map.of(FREE_BOARD_ID_PARAM, freeBoardId));
    }

    /**
     * 관리자 자유게시판 게시글 삭제
     *
     * @param freeBoardId 삭제할 게시글 식별자
     * @return 200 OK
     */
    @DeleteMapping("/admin/boards/free/{freeBoardId}")
    public ResponseEntity<Object> deleteFreeBoardByAdmin(
            @PathVariable Long freeBoardId,
            @AdminAuthenticationPrincipal Principal<Member> principal) {
        Member admin = principal.getAuthentication();

        freeBoardService.deleteFreeBoardByAdmin(freeBoardId, admin.getMemberId());

        return ResponseEntity
                .ok()
                .build();
    }

    /**
     * 관리자 자유게시판 게시글 수정
     *
     * @param freeBoard 게시글 수정 파라미터
     * @param saveFiles 추가로 저장할 첨부파일들
     * @param deleteFileIds 삭제할 첨부파일 식별자들
     * @param freeBoardId 수정할 자유게시판 게시글 식별자
     * @param principal 관리자 인증 객체
     * @return 200 OK
     */
    @PutMapping("/admin/boards/free/{freeBoardId}")
    public ResponseEntity<Object> updateFreeBoardByAdmin(
            @Valid @ModelAttribute FreeBoard freeBoard,
            @RequestParam(required = false) List<MultipartFile> saveFiles,
            @RequestParam(required = false) List<Long> deleteFileIds,
            @PathVariable Long freeBoardId,
            @AdminAuthenticationPrincipal Principal<Member> principal) {
        if (saveFiles == null) {
            saveFiles = List.of();
        }
        if (deleteFileIds == null) {
            deleteFileIds = List.of();
        }
        Member member = principal.getAuthentication();

        freeBoardService.updateFreeBoardById(freeBoard, saveFiles, deleteFileIds, freeBoardId, member.getMemberId());

        return ResponseEntity
                .ok()
                .build();
    }
}


















