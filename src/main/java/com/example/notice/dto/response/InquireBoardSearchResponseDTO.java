package com.example.notice.dto.response;

import com.example.notice.entity.InquireBoard;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 문의 게시판 검색 응답 DTO
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class InquireBoardSearchResponseDTO extends InquireBoard {
    private Boolean isAnswered;

    public InquireBoardSearchResponseDTO(Long inquireBoardId, Long memberId, LocalDateTime createdAt, LocalDateTime modifiedAt, String title, String content, Long views, Boolean isSecret, Boolean isAnswered) {
        super(inquireBoardId, memberId, createdAt, modifiedAt, title, content, views, isSecret);
        this.isAnswered = isAnswered;
    }
}
