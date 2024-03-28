package com.example.notice.entity;


import jakarta.validation.constraints.NotBlank;
import lombok.*;

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

    @NotBlank
    private String answer;

    @Builder
    public InquireAnswer(Long inquireAnswerId, Long inquireBoardId, Long memberId, String answer) {
        this.inquireAnswerId = inquireAnswerId;
        this.inquireBoardId = inquireBoardId;
        this.member = Member.builder()
                .memberId(memberId)
                .build();
        this.answer = answer;
    }

    public Long getMemberId() {
        return member.getMemberId();
    }

    public String getMemberName() {
        return member.getName();
    }
}
