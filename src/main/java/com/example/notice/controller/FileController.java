package com.example.notice.controller;


import com.example.notice.dto.response.FileResponseDTO;
import com.example.notice.entity.AttachmentFile;
import com.example.notice.exception.PhysicalFileNotFoundException;
import com.example.notice.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 파일 컨트롤러
 */
@RestController
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    /**
     * 파일 다운로드
     *
     * @param fileId 파일 식별자
     * @return 파일 byte stream
     */
    @PostMapping("/api/boards/files/{fileId}/download")
    public ResponseEntity<InputStreamResource> downloadFile(
            @PathVariable Long fileId) {
        File file = fileService.getPhysicalFile(fileId);
        String originalName = fileService.getFileOriginalNameById(fileId);

        return ResponseEntity
                .ok()
                .contentLength(file.length())
                .header(HttpHeaders.CONTENT_DISPOSITION, getContentDispositionValue(originalName))
                .body(getInputStreamResource(file));
    }


    /**
     * 파일 조회
     *
     * @param freeBoardId 자유게시판 식별자
     * @return 파일 리스트
     */
    @GetMapping("/api/boards/free/{freeBoardId}/files")
    public ResponseEntity<List<FileResponseDTO>> getFiles(@PathVariable Long freeBoardId) {
        List<FileResponseDTO> files = fileService.getFileByFreeBoardId(freeBoardId);

        return ResponseEntity
                .ok(files);
    }


    private String getContentDispositionValue(String originalName) {
        return ContentDisposition
                .attachment()
                .filename(originalName, StandardCharsets.UTF_8)
                .build()
                .toString();
    }

    private InputStreamResource getInputStreamResource(File file) {
        try {
            return new InputStreamResource(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            throw new PhysicalFileNotFoundException(e.getMessage());
        }
    }

}

