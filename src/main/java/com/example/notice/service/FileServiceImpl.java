package com.example.notice.service;

import com.example.notice.dto.common.SuccessesAndFails;
import com.example.notice.dto.response.FileResponseDTO;
import com.example.notice.entity.AttachmentFile;
import com.example.notice.exception.EntityNotExistException;
import com.example.notice.exception.FileSaveCheckedException;
import com.example.notice.files.PhysicalFileRepository;
import com.example.notice.repository.AttachmentFileRepository;
import com.example.notice.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.notice.constant.ErrorMessageConstant.FILE_NOT_EXIST_MESSAGE;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class FileServiceImpl implements FileService{

    private final PhysicalFileRepository physicalFileRepository;
    private final AttachmentFileRepository fileRepository;
    private final FileUtil fileUtil;

    @Override
    public File getPhysicalFile(Long fileId) {
        AttachmentFile attachmentFile = fileRepository.findByFileId(fileId)
                .orElseThrow(() -> new EntityNotExistException(FILE_NOT_EXIST_MESSAGE));

        return physicalFileRepository.getFile(getFileFullPath(attachmentFile));
    }

    @Override
    public List<FileResponseDTO> getFileByFreeBoardId(Long freeBoardId) {
        return fileRepository.findByFreeBoardId(freeBoardId);

    }

    @Override
    public String getFileOriginalNameById(Long fileId) {
        return fileRepository.findOriginalNameById(fileId);
    }

    @Transactional
    @Override
    public SuccessesAndFails<String> saveFiles(List<MultipartFile> multipartFiles, Long freeBoardId) {
        SuccessesAndFails<String> results = SuccessesAndFails.emptyList();

        for (MultipartFile multipartFile : multipartFiles) {
            try {
                fileUtil.checkAllowFileExtension(multipartFile);
                AttachmentFile attachmentFile = saveFile(multipartFile, freeBoardId);

                results.addSuccess(attachmentFile.getOriginalName());
            } catch (FileSaveCheckedException e) {
                String originalFilename = multipartFile.getOriginalFilename();

                results.addFail(multipartFile.getOriginalFilename());
            }
        }

        return results;
    }

    @Transactional
    @Override
    public void deleteFileByFileIds(List<Long> fileIds) {
        fileIds
                .forEach(id -> deleteFileByFileId(id));
    }

    @Transactional
    @Override
    public void deleteFileByFileId(Long fileId) {
        fileRepository.findByFileId(fileId)
                .ifPresent((file) -> {
                    fileRepository.deleteByFileId(fileId);
                    physicalFileRepository.delete(fileId);
                });
    }

    @Transactional
    @Override
    public void deleteFileByFreeBoardId(Long freeBoardId) {
        List<Long> fileIds = fileRepository.findByFreeBoardId(freeBoardId)
                .stream()
                .map(af -> af.getFileId())
                .collect(Collectors.toList());
        deleteFileByFileIds(fileIds);
    }


    private AttachmentFile saveFile(MultipartFile multipartFile, Long boardId) throws FileSaveCheckedException {
        String savedPath = physicalFileRepository
                .save(fileUtil.getBytes(multipartFile), multipartFile.getOriginalFilename());

        AttachmentFile attachmentFile = AttachmentFile.builder()
                .originalName(multipartFile.getOriginalFilename())
                .physicalName(fileUtil.getFileNameFromPath(savedPath))
                .path(fileUtil.getFileSubPath(savedPath))
                .extension(fileUtil.getExtension(savedPath))
                .build();

        fileRepository.saveWithFreeBoardId(attachmentFile, boardId);
        return attachmentFile;
    }

    private String getFileFullPath(AttachmentFile attachmentFile) {
        return attachmentFile.getPath() + "/" + attachmentFile.getPhysicalName();
    }


}
