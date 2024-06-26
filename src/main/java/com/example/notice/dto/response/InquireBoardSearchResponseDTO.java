package com.example.notice.dto.response;

import com.example.notice.entity.InquireBoard;
import com.example.notice.entity.Member;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 문의 게시판 검색 응답 DTO
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class InquireBoardSearchResponseDTO {
    private Long inquireBoardId;

    private Long memberId;
    private String memberName;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    private String title;
    private String content;
    private Boolean isSecret;

    private Long views;
    private Boolean isAnswered;

    public InquireBoardSearchResponseDTO(InquireBoard inquireBoard, Boolean isAnswered) {
        this.inquireBoardId = inquireBoard.getInquireBoardId();
        this.memberId = inquireBoard.getMemberId();
        this.memberName = inquireBoard.getMemberName();
        this.createdAt = inquireBoard.getCreatedAt();
        this.modifiedAt = inquireBoard.getModifiedAt();
        this.title = inquireBoard.getTitle();
        this.content = inquireBoard.getContent();
        this.isSecret = inquireBoard.getIsSecret();
        this.views = inquireBoard.getViews();

        this.isAnswered = isAnswered;
    }
}
