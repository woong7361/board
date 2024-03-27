package com.example.notice.controller;

import com.example.notice.auth.AuthenticationPrincipal;
import com.example.notice.auth.principal.Principal;
import com.example.notice.dto.common.IdList;
import com.example.notice.dto.common.SuccessesAndFails;
import com.example.notice.entity.AttachmentFile;
import com.example.notice.entity.Member;
import com.example.notice.exception.PhysicalFileNotFoundException;
import com.example.notice.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static com.example.notice.constant.ResponseConstant.FREE_BOARD_ID_PARAM;

/**
 * 파일 컨트롤러
 */
@RestController
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    /**
     * 게시판 첨부파일 저장 컨트롤러
     *
     * @param files 첨부파일
     * @return 첨부파일 성공 실패 결과
     */
    @PostMapping("/api/boards/free/{freeBoardId}/files")
    public ResponseEntity<Map<String, Long>> saveFiles(
            @PathVariable Long freeBoardId,
            @RequestParam List<MultipartFile> files) {
        SuccessesAndFails<AttachmentFile> successesAndFails = fileService.save(files, freeBoardId);


        return ResponseEntity.ok(Map.of(FREE_BOARD_ID_PARAM, freeBoardId));
    }

    /**
     * 파일 삭제
     *
     * @param fileIds   파일 식별자
     * @param principal 인증된 사용자 정보
     * @return 200 ok
     */
    @DeleteMapping("/api/boards/free/files")
    public ResponseEntity<Object> deleteFiles(
            @RequestBody IdList fileIds,
            @AuthenticationPrincipal Principal<Member> principal
    ) {
        Member member = principal.getAuthentication();
        fileService.checkFilesAuthorization(fileIds, member.getMemberId());

        fileService.delete(fileIds);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/api/boards/files/{fileId}/download")
    public ResponseEntity<InputStreamResource> downloadFile(
            @PathVariable Long fileId) {
        File file = fileService.getPhysicalFile(fileId);

        return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_PNG)
                .contentLength(file.length())
                .header(HttpHeaders.CONTENT_DISPOSITION, getContentDispositionValue(file))
                .body(getInputStreamResource(file));
    }


    private String getContentDispositionValue(File file) {
        return ContentDisposition
                .attachment()
                .filename(file.getName(), StandardCharsets.UTF_8)
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

