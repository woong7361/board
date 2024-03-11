package com.example.notice.controller;

import com.example.notice.dto.SuccessesAndFails;
import com.example.notice.entity.AttachmentFile;
import com.example.notice.service.FileServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 파일 컨트롤러
 */
@RestController
@RequiredArgsConstructor
public class FileController {

    private final FileServiceImpl fileService;

    /**
     * 게시판 첨부파일 저장 컨트롤러
     * @param files 첨부파일
     * @return 첨부파일 성공 실패 결과
     */
    //TODO 파일 엔티티 그대로 보내고 있어서 path같은 중요데이터 유출중...
    @PostMapping("/api/boards/free/files")
    public ResponseEntity<Long> saveFiles(
            @RequestParam Long freeBoardId,
            @RequestParam List<MultipartFile> files) {
        SuccessesAndFails<AttachmentFile> successesAndFails = fileService.save(files, freeBoardId);

        return ResponseEntity.ok(freeBoardId);
    }
}

