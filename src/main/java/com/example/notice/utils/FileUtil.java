package com.example.notice.utils;

import com.example.notice.config.ConfigurationService;
import com.example.notice.entity.AttachmentFile;
import com.example.notice.exception.FileSaveCheckedException;
import com.example.notice.exception.FileSaveException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.example.notice.constant.ErrorMessageConstant.FILE_IO_EXCEPTION_MESSAGE;
import static com.example.notice.constant.ErrorMessageConstant.NOT_ALLOWED_FILE_EXTENSION_MESSAGE;

/**
 * 파일 Util 클래스
 */
@Component
@RequiredArgsConstructor
public class FileUtil {

    private final ConfigurationService configurationService;

    /**
     * 파일의 확장자가 허용되는지 확인
     *
     * @param file 요청 파일
     * @throws FileSaveCheckedException 실패시 throw exception
     */
    public void checkAllowFileExtension(MultipartFile file) throws FileSaveCheckedException {
        if (isNotAllowedExtensionFile(file)) {
            throw new FileSaveCheckedException(NOT_ALLOWED_FILE_EXTENSION_MESSAGE, file.getOriginalFilename());
        }
    }

    /**
     * 파일 bytes 추출
     * @param multipartFile multipartFile
     * @return byte 배열
     */
    public byte[] getBytes(MultipartFile multipartFile) {
        try {
            return multipartFile.getBytes();
        } catch (IOException e) {
            throw new FileSaveException(FILE_IO_EXCEPTION_MESSAGE);
        }
    }

    /**
     * 파일 이름에서 파일 확장자 추출
     * @param originalFilename 파일 이름
     * @return 확장자
     */
    public String getExtension(String originalFilename) {
        return originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
    }

    /**
     * 파일 경로에서 파일 이름 추출
     *
     * @param path 파일 경로
     * @return 파일 이름
     */
    public String getFileNameFromPath(String path) {
        return path.substring(path.lastIndexOf("/") + 1);
    }

    /**
     * 파일 경로에서 이름을 제외한 경로 추출
     *
     * @param path 파일 경로
     * @return 이름을 제외한 경로
     */
    public String getFileSubPath(String path) {
        return path.substring(0, path.lastIndexOf("/"));
    }

    /**
     * 파일 entity에서 파일 경로 반환
     *
     * @param attachmentFile 파일 entity
     * @return 파일 경로
     */
    public String getFileFullPath(AttachmentFile attachmentFile) {
        return attachmentFile.getPath() + "/" + attachmentFile.getPhysicalName();
    }

    private boolean isNotAllowedExtensionFile(MultipartFile file) {
        return configurationService.getAllowExtension().stream()
                .noneMatch((allowExtension) -> allowExtension.equals(getExtension(file.getOriginalFilename())));
    }
}
