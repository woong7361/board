package com.example.notice.controller;

import com.example.notice.auth.AuthenticationPrincipal;
import com.example.notice.auth.principal.Principal;
import com.example.notice.constant.ResponseConstant;
import com.example.notice.dto.FreeBoardCreateRequest;
import com.example.notice.entity.AttachmentFile;
import com.example.notice.entity.FreeBoard;
import com.example.notice.entity.Member;
import com.example.notice.service.FreeBoardService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 자유 게시판 컨트롤러
 */
@RestController
@RequiredArgsConstructor
public class FreeBoardController {

    private final FreeBoardService freeBoardService;

    /**
     * 자유게시판 게시글 생성
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

}
