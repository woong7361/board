package com.example.notice.utils;

import com.example.notice.config.ConfigurationService;
import com.example.notice.entity.AttachmentFile;
import com.example.notice.exception.FileSaveCheckedException;
import com.example.notice.exception.FileSaveException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class FileUtil {

    private final ConfigurationService configurationService;

    public void checkAllowFileExtension(MultipartFile file) throws FileSaveCheckedException {
        if (isNotAllowedExtensionFile(file)) {
            throw new FileSaveCheckedException("not allow extension exception", file.getOriginalFilename());
        }
    }

    private boolean isNotAllowedExtensionFile(MultipartFile file) {
        return configurationService.getAllowExtension().stream()
                .noneMatch((allowExtension) -> allowExtension.equals(getExtension(file.getOriginalFilename())));
    }

    public byte[] getBytes(MultipartFile multipartFile) {
        try {
            return multipartFile.getBytes();
        } catch (IOException e) {
            throw new FileSaveException("temp file output exception");
        }
    }

    public String getExtension(String originalFilename) {
        return originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
    }

    public String getFileName(String path) {
        return path.substring(path.lastIndexOf("/") + 1);
    }

    public String getFileSubPath(String path) {
        return path.substring(0, path.lastIndexOf("/"));
    }

    public String getFileFullPath(AttachmentFile attachmentFile) {
        return attachmentFile.getPath() + "/" + attachmentFile.getPhysicalName();
    }
}
