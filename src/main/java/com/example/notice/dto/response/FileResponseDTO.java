package com.example.notice.dto.response;

import lombok.*;

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
