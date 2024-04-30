package com.example.notice.dto.response;

import lombok.*;

/**
 * 파일 조회 응답 DTO
 */
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Getter
@Setter
public class FileResponseDTO {
    private Long fileId;
    private Long freeBoardId;

    private String originalName;
}
