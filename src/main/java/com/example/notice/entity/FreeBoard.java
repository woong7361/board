package com.example.notice.entity;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 자유 게시판 엔티티
 */
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Getter
@Setter
public class FreeBoard {
    private Long freeBoardId;

    @Getter(AccessLevel.PRIVATE)
    private Member member;

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

    public Long getMemberId() {
        return this.member != null ? this.member.getMemberId() : null;
    }

    public String getMemberName() {
        return this.member != null ? this.member.getName() : null;
    }
}
