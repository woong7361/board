package com.example.notice.service;

import com.example.notice.entity.AttachmentFile;
import com.example.notice.exception.EntityNotExistException;
import com.example.notice.files.PhysicalFileRepository;
import com.example.notice.repository.AttachmentFileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;

import static com.example.notice.constant.ErrorMessageConstant.FILE_NOT_EXIST_MESSAGE;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class FileServiceImpl implements FileService{

    private final PhysicalFileRepository physicalFileRepository;
    private final AttachmentFileRepository fileRepository;

    @Override
    public File getPhysicalFile(Long fileId) {
        AttachmentFile attachmentFile = fileRepository.findByFileId(fileId)
                .orElseThrow(() -> new EntityNotExistException(FILE_NOT_EXIST_MESSAGE));

        return physicalFileRepository.getFile(getFileFullPath(attachmentFile));
    }

    private String getFileFullPath(AttachmentFile attachmentFile) {
        return attachmentFile.getPath() + "/" + attachmentFile.getPhysicalName();
    }



}
