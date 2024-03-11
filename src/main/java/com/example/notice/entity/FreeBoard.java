package com.example.notice.entity;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 자유 게시판 엔티티
 */
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class FreeBoard {
    private Long freeBoardId;
    private Long memberId;

    @NotBlank
    private String category;

    @Size(min = 1, max = 100)
    @NotBlank
    private String title;

    @Size(min = 1, max = 4000)
    @NotEmpty
    private String content;
    private Long views;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public void setOwner(Member member) {
        this.memberId = member.getMemberId();
    }

}
