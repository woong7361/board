package com.example.notice.entity;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 코멘트
 */
@Getter
@NoArgsConstructor
//@Builder
@AllArgsConstructor
public class Comment {

    private Long commentId;
    //    private Long memberId;
    @Getter(AccessLevel.PRIVATE)
    private Member member;

    private Long freeBoardId;

    @NotBlank
    private String content;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public Long getMemberId() {
        return member == null ? null : member.getMemberId();
    }

    public String getMemberName() {
        return member == null ? null : member.getName();
    }

    //TODO 좀 복잡하게 내부가 들어가있어서 호불호가 갈릴수도 but, 내부를 가릴 수 있어 편하다는 장점 존재
    // 그 이전 DTO를 생성하지 않기위해 Comment에서 getMemberName()을 정책적으로 허용하느냐의 문제
    @Builder
    public Comment(Long commentId, Long memberId, Long freeBoardId, String content, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        if (memberId != null) {
            this.member = Member.builder()
                    .memberId(memberId)
                    .build();
        }

        this.commentId = commentId;
        this.freeBoardId = freeBoardId;
        this.content = content;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
