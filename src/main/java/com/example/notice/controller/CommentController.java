package com.example.notice.controller;

import com.example.notice.auth.resolvehandler.AdminAuthenticationPrincipal;
import com.example.notice.auth.resolvehandler.AuthenticationPrincipal;
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
 * 댓글 컨트롤러
 */
@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;


    /**
     * 댓글 생성
     *
     * @param freeBoardId 댓글 부모 게시글 식별자
     * @param principal 인증된 회원 객체
     * @param comment 댓글 생성 파라미터
     * @return 200 OK
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
     * 게시글의 댓글들 조회
     *
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

    /**
     * 댓글 삭제
     *
     * @param commentId 댓글 부모 게시글 식별자
     * @param principal 댓글 삭제를 요청한 사용자 인증 객체
     * @return 200 OK
     */
    @DeleteMapping("/api/boards/free/comments/{commentId}")
    public ResponseEntity<Object> deleteComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal Principal<Member> principal
    ) {
        Member member = principal.getAuthentication();

        commentService.delete(commentId, member.getMemberId());

        return ResponseEntity.ok().build();
    }

    /**
     * 관리자 댓글 생성
     *
     * @param freeBoardId 댓글 부모 게시글 식별자
     * @param principal 관리자 인증 객체
     * @param comment 댓글 생성 파라미터
     * @return 200 OK
     */
    @PostMapping("/admin/boards/free/{freeBoardId}/comments")
    public ResponseEntity<Object> createCommentByAdmin(
            @PathVariable Long freeBoardId,
            @AdminAuthenticationPrincipal Principal<Member> principal,
            @Valid @RequestBody Comment comment
    ) {
        Member member = principal.getAuthentication();
        commentService.createComment(comment, freeBoardId, member.getMemberId());

        return ResponseEntity.ok()
                .build();
    }

    /**
     * 관리자 댓글 삭제
     *
     * @param commentId 댓글 식별자
     * @return 200 OK
     */
    @DeleteMapping("/admin/boards/free/comments/{commentId}")
    public ResponseEntity<Object> deleteCommentByAdmin(
            @PathVariable Long commentId,
            @AdminAuthenticationPrincipal Principal<Member> principal) {
        Member member = principal.getAuthentication();

        commentService.deleteByAdmin(commentId, member.getMemberId());

        return ResponseEntity.ok().build();
    }

}
