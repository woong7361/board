package com.example.notice.entity;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 문의 게시글 응답 엔티티
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InquireAnswer {

    private Long inquireAnswerId;
    private Long inquireBoardId;

    @Getter(AccessLevel.PRIVATE)
    private Member member;

    @Size(min = 1, max = 1000)
    @NotBlank
    private String answer;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    @Builder
    public InquireAnswer(Long inquireAnswerId, Long inquireBoardId, Long memberId, String memberName, String answer, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.inquireAnswerId = inquireAnswerId;
        this.inquireBoardId = inquireBoardId;
        this.member = Member.builder()
                .memberId(memberId)
                .name(memberName)
                .build();
        this.answer = answer;
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
