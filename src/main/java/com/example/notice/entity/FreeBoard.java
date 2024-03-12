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
public class FreeBoard {
    private Long freeBoardId;

    //TODO 식별자(FK)인 memberId 대신 연관 객체 Member를 대신 사용
    // private Getter로 민감한값 유출 방지 (password, ...)
    // public getter로 외부 노출값 설정 getMemberName(), ...
    // 컨트롤러에서 엔티티 타입으로 반환값 예측이 어려워진다는 단점?
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

    public void setOwner(Member member) {
        this.member = member;
    }

    public Long getMemberId() {
        return this.member != null ? this.member.getMemberId() : null;
    }

    public String getMemberName() {
        return this.member != null ? this.member.getName() : null;
    }


}
