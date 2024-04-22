package com.example.notice.controller;

import com.example.notice.auth.AuthenticationPrincipal;
import com.example.notice.auth.principal.Principal;
import com.example.notice.dto.request.InquireBoardSearchDTO;
import com.example.notice.dto.response.InquireBoardResponseDTO;
import com.example.notice.dto.response.InquireBoardSearchResponseDTO;
import com.example.notice.entity.InquireBoard;
import com.example.notice.entity.Member;
import com.example.notice.page.PageRequest;
import com.example.notice.page.PageResponse;
import com.example.notice.service.InquireBoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.example.notice.constant.ResponseConstant.INQUIRE_BOARD_ID_PARAM;

/**
 * 문의 게시판 컨트롤러
 */
@RestController
@RequiredArgsConstructor
public class InquireBoardController {


    private final InquireBoardService inquireBoardService;

    /**
     * 문의 게시판 게시글 생성
     *
     * @param inquireBoard 문의 게시판 게시글 파라미터
     * @param principal    게시글 생성자 인증 객체
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


    /**
     * 문의 게시판 검색
     *
     * @param inquireBoardSearchDTO 문의 게시판 검색 파라미터
     * @param pageRequest 페이지 요청 파라미터
     * @param principal 회원 인증 객체
     * @return 검색 결과
     */
    @GetMapping("/api/boards/inquire")
    public ResponseEntity<PageResponse<InquireBoardSearchResponseDTO>> searchInquireBoard(
            @ModelAttribute InquireBoardSearchDTO inquireBoardSearchDTO,
            @ModelAttribute PageRequest pageRequest,
            @AuthenticationPrincipal Principal<Member> principal) {
        Member member = principal.getAuthentication();

        PageResponse<InquireBoardSearchResponseDTO> inquireBoards =
                inquireBoardService.searchInquireBoard(inquireBoardSearchDTO, pageRequest, member.getMemberId());

        return ResponseEntity
                .ok(inquireBoards);
    }

    /**
     * 문의 게시판 게시글 조회
     *
     * @param inquireBoardId 문의 게시판 게시글 식별자
     * @return 해당하는 게시글의 내용
     */
    @GetMapping("/api/boards/inquire/{inquireBoardId}")
    public ResponseEntity<InquireBoardResponseDTO> getInquireBoard(@PathVariable Long inquireBoardId) {
        //TODO 인증 인가 필터 구현후 구현하는게 맞을듯
        InquireBoardResponseDTO inquireBoard = inquireBoardService.getBoardById(inquireBoardId);

        return ResponseEntity
                .ok(inquireBoard);
    }

    /**
     * 문의 게시판 게시글 수정
     * @param inquireBoard 문의 게시판 게시글 수정 요청 파라미터
     * @param inquireBoardId 문의 게시판 게시글 식별자
     * @param principal 요청하는 회원 인증 객체
     * @return 200 ok
     */
    @PutMapping("/api/boards/inquire/{inquireBoardId}")
    public ResponseEntity<Object> updateInquireBoard(
            @Valid @RequestBody InquireBoard inquireBoard,
            @PathVariable Long inquireBoardId,
            @AuthenticationPrincipal Principal<Member> principal) {
        Member member = principal.getAuthentication();
        inquireBoardService.updateById(inquireBoard, inquireBoardId, member.getMemberId());

        return ResponseEntity
                .ok()
                .build();
    }

    /**
     * 문의 게시판 게시글 삭제
     * @param inquireBoardId 삭제할 문의 게시판 게시글 식별자
     * @param principal 요청하는 회원 인증 객체
     * @return 200 ok
     */
    @DeleteMapping("/api/boards/inquire/{inquireBoardId}")
    public ResponseEntity<Object> deleteInquireBoard(
            @PathVariable Long inquireBoardId,
            @AuthenticationPrincipal Principal<Member> principal) {
        Member member = principal.getAuthentication();
        inquireBoardService.deleteById(inquireBoardId, member.getMemberId());

        return ResponseEntity
                .ok()
                .build();
    }
}
