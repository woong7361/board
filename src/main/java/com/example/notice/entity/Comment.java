package com.example.notice.entity;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 코멘트 엔티티
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    private Long commentId;
    @Getter(AccessLevel.PRIVATE)
    private Member member;

    private Long freeBoardId;

    @NotBlank
    private String content;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public Long getMemberId() {
        return member.getMemberId();
    }

    public String getMemberName() {
        return member.getName();
    }

    @Builder
    protected Comment(Long commentId, Long memberId, String memberName, Long freeBoardId, String content, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.member = Member.builder()
                .memberId(memberId)
                .name(memberName)
                .build();

        this.commentId = commentId;
        this.freeBoardId = freeBoardId;
        this.content = content;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
