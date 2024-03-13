package com.example.notice.controller;

import com.example.notice.auth.AuthenticationPrincipal;
import com.example.notice.auth.principal.Principal;
import com.example.notice.entity.Comment;
import com.example.notice.entity.Member;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommentController {


    @PostMapping("/api/boards/free/{freeBoardId}/comments")
    public ResponseEntity<Object> createComment(
            @PathVariable Long freeBoardId,
            @AuthenticationPrincipal Principal<Member> principal,
            @Valid @RequestBody Comment comment
    ) {


        return ResponseEntity.ok()
                .build();
    }
}
