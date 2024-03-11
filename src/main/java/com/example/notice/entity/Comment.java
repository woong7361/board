package com.example.notice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 코멘트
 */
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Comment {

    private Long commentId;
    private Long memberId;
    private Long freeBoardId;

    private String content;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
