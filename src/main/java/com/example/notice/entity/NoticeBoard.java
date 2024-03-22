package com.example.notice.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 알림 게시판 엔티티
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class NoticeBoard {
    private Long noticeBoardId;

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

    @NotNull
    private Boolean isFixed;

    private Long views;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    @Builder
    private NoticeBoard(Long noticeBoardId, Long memberId, String category, String title, String content, Boolean isFixed, Long views, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.member = Member.builder()
                .memberId(memberId)
                .build();

        this.noticeBoardId = noticeBoardId;
        this.category = category;
        this.title = title;
        this.content = content;
        this.isFixed = isFixed;
        this.views = views;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public Long getMemberId() {
        return member.getMemberId();
    }

    public String getMemberName() {
        return member.getName();
    }
}
