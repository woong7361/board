package com.example.notice.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 첨부파일 엔티티
 */
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class AttachmentFile {
    private Long fileId;
    private Long freeBoardId;

    private String physicalName;
    private String originalName;
    private String path;
    private String extension;
}
