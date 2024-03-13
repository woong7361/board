package com.example.notice.controller;

import com.example.notice.auth.AuthenticationPrincipal;
import com.example.notice.auth.principal.Principal;
import com.example.notice.entity.Comment;
import com.example.notice.entity.Member;
import com.example.notice.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.example.notice.constant.ResponseConstant.COMMENTS_PARAM;

/**
 * 댓글 컨트롤러 클래스
 */
@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;


    /**
     * 댓글 생성 엔드포인트
     * @param freeBoardId 댓글 부모 게시글 식별자
     * @param principal 인증된 회원 객체
     * @param comment 댓글 생성 인자
     * @return 200 ok
     */
    @PostMapping("/api/boards/free/{freeBoardId}/comments")
    public ResponseEntity<Object> createComment(
            @PathVariable Long freeBoardId,
            @AuthenticationPrincipal Principal<Member> principal,
            @Valid @RequestBody Comment comment
    ) {
        Member member = principal.getAuthentication();
        commentService.createComment(comment, freeBoardId, member.getMemberId());

        return ResponseEntity.ok()
                .build();
    }

    /**
     * 댓글 조회 엔드포인트
     * @param freeBoardId 댓글 부모 게시글 식별자
     * @return 게시글에 속한 댓글들
     */
    @GetMapping("/api/boards/free/{freeBoardId}/comments")
    public ResponseEntity<Map<String, List<Comment>>> getComments(
            @PathVariable Long freeBoardId
    ) {
        List<Comment> comments = commentService.getComments(freeBoardId);

        return ResponseEntity
                .ok(Map.of(COMMENTS_PARAM, comments));
    }

    @DeleteMapping("/api/boards/free/comments/{commentId}")
    public ResponseEntity<Object> deleteComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal Principal<Member> principal
    ) {
        Member member = principal.getAuthentication();
        commentService.checkAuthorization(commentId, member.getMemberId());

        commentService.delete(commentId);

        return ResponseEntity.ok().build();
    }

}
