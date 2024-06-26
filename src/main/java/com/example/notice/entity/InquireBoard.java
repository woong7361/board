package com.example.notice.entity;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 문의 게시글 엔티티
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InquireBoard {
    private Long inquireBoardId;

    @Getter(AccessLevel.PRIVATE)
    private Member member;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    @Size(min = 1, max = 100)
    @NotBlank
    private String title;
    @Size(min = 1, max = 4000)
    @NotBlank
    private String content;
    @NotNull
    private Boolean isSecret;

    private Long views;

    @Builder
    public InquireBoard(Long inquireBoardId, Long memberId, String memberName, LocalDateTime createdAt, LocalDateTime modifiedAt, String title, String content, Long views, Boolean isSecret) {
        this.member = Member.builder()
                .memberId(memberId)
                .name(memberName)
                .build();
        this.inquireBoardId = inquireBoardId;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.title = title;
        this.content = content;
        this.views = views;
        this.isSecret = isSecret;
    }

    public Long getMemberId() {
        return member.getMemberId();
    }

    public String getMemberName() {
        return member.getName();
    }
}
