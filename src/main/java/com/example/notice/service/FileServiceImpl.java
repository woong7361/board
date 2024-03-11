package com.example.notice.service;

import com.example.notice.config.ConfigurationService;
import com.example.notice.dto.SuccessesAndFails;
import com.example.notice.entity.AttachmentFile;
import com.example.notice.exception.FileSaveCheckedException;
import com.example.notice.exception.FileSaveException;
import com.example.notice.files.PhysicalFileRepository;
import com.example.notice.repository.AttachmentFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FileServiceImpl implements FileService{

    private final PhysicalFileRepository physicalFileRepository;
    private final AttachmentFileRepository fileRepository;
    private final ConfigurationService configurationService;

    @Override
    @Transactional
    public SuccessesAndFails<AttachmentFile> save(List<MultipartFile> files, Long boardId) {
        SuccessesAndFails<AttachmentFile> successesAndFails = saveFiles(files, boardId);

        return successesAndFails;
    }


    private SuccessesAndFails<AttachmentFile> saveFiles(List<MultipartFile> multipartFiles, Long boardId) {
        SuccessesAndFails<AttachmentFile> results = SuccessesAndFails.emptyList();

        for (MultipartFile multipartFile : multipartFiles) {

            try {
                checkAllowedExtensionFile(multipartFile);
                AttachmentFile attachmentFile = saveFile(multipartFile, boardId);

                results.addSuccess(attachmentFile);
            } catch (FileSaveCheckedException e) {
                results.addFail(AttachmentFile.builder()
                        .originalName(multipartFile.getOriginalFilename())
                        .build());
            }
        }

        return results;
    }

    private AttachmentFile saveFile(MultipartFile multipartFile, Long boardId) throws FileSaveCheckedException {
        String savedPath = physicalFileRepository.save(getBytes(multipartFile), multipartFile.getOriginalFilename());

        AttachmentFile attachmentFile = AttachmentFile.builder()
                .originalName(multipartFile.getOriginalFilename())
                .physicalName(getFileName(savedPath))
                .path(getFileSubPath(savedPath))
                .extension(getExtension(savedPath))
                .build();

        fileRepository.saveWithFreeBoardId(attachmentFile, boardId);
        return attachmentFile;
    }

    private void checkAllowedExtensionFile(MultipartFile file) throws FileSaveCheckedException {
        if (isNotAllowedExtensionFile(file)) {
            throw new FileSaveCheckedException("not allow extension exception", file.getOriginalFilename());
        }
    }

    private boolean isNotAllowedExtensionFile(MultipartFile file) {
        return configurationService.getAllowExtension().stream()
                .noneMatch((allowExtension) -> allowExtension.equals(getExtension(file.getOriginalFilename())));
    }

    private byte[] getBytes(MultipartFile multipartFile) {
        try {
            return multipartFile.getBytes();
        } catch (IOException e) {
            throw new FileSaveException("temp file output exception");
        }
    }

    private String getExtension(String originalFilename) {
        return originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
    }

    public String getFileName(String path) {
        return path.substring(path.lastIndexOf("/") + 1);
    }

    public String getFileSubPath(String path) {
        return path.substring(0, path.lastIndexOf("/"));
    }

}
