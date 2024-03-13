package com.example.notice.service;


import com.example.notice.mock.repository.MockAttachmentFileRepository;
import com.example.notice.mock.service.MockConfigurationService;

class FileServiceTest {

    private FileService fileService = new FileServiceImpl(
            new MockPhysicalFileRepository(),
            new MockAttachmentFileRepository(),
            new MockConfigurationService()
    );
}