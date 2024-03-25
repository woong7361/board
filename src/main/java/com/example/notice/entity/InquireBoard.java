package com.example.notice.entity;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InquireBoard {
    private Long inquireBoardId;
    private Member member;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    @NotBlank
    private String title;
    @NotBlank
    private String content;
    @NotNull
    private Boolean isSecret;

    private Long views;

    @Builder
    public InquireBoard(Long inquireBoardId, Long memberId, LocalDateTime createdAt, LocalDateTime modifiedAt, String title, String content, Long views, Boolean isSecret) {
        this.member = Member.builder()
                .memberId(memberId)
                .build();
        this.inquireBoardId = inquireBoardId;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.title = title;
        this.content = content;
        this.views = views;
        this.isSecret = isSecret;
    }
}
