package com.example.notice.entity;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InquireAnswer {

    private Long inquireAnswerId;
    private Long inquireBoardId;
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
}
