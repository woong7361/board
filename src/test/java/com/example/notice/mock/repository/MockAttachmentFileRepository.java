package com.example.notice.mock.repository;

import com.example.notice.entity.AttachmentFile;
import com.example.notice.entity.FreeBoard;
import com.example.notice.mock.database.MemoryDataBase;
import com.example.notice.repository.AttachmentFileRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.notice.mock.database.MemoryDataBase.attachmentFileRepository;
import static com.example.notice.mock.database.MemoryDataBase.freeBoardRepository;

public class MockAttachmentFileRepository implements AttachmentFileRepository {

//    private List<AttachmentFile> attachmentFileRepository = MemoryDataBase.attachmentFileRepository;
//    private List<FreeBoard> freeBoardRepository = MemoryDataBase.freeBoardRepository;

    @Override
    public void saveWithFreeBoardId(AttachmentFile file, Long boardId) {
        AttachmentFile addFile = attachmentFileBuilderMapper(file)
                .freeBoardId(boardId)
                .build();

        attachmentFileRepository.add(addFile);
    }

    @Override
    public Optional<AttachmentFile> findByFileId(Long fileId) {
        return attachmentFileRepository.stream()
                .filter((af) -> af.getFileId().equals(fileId))
                .findFirst();
    }

    @Override
    public void deleteByFileId(Long fileId) {
        attachmentFileRepository
                .removeIf((af) -> af.getFileId().equals(fileId));
    }

    //TODO 구현상의 어려움 -> 불가능은 아니지만 테스트를 위한 비용이 너무 커진다는 단점
    //  임시로 MemoryDataBase 를 따로 구현하여 구현함
    @Override
    public Optional<AttachmentFile> findByFileIdAndMemberId(Long fileId, Long memberId) {
        return attachmentFileRepository.stream()
                .filter((af) -> af.getFileId().equals(fileId))
                .filter((af) -> freeBoardRepository.stream()
                            .filter((fb) -> af.getFreeBoardId().equals(fb.getFreeBoardId()))
                            .anyMatch((fb) -> fb.getMemberId().equals(memberId)))
                .findFirst();
    }

    public static AttachmentFile.AttachmentFileBuilder attachmentFileBuilderMapper(AttachmentFile file) {
        return AttachmentFile.builder()
                .fileId(file.getFileId())
                .physicalName(file.getPhysicalName())
                .originalName(file.getOriginalName())
                .extension(file.getExtension())
                .path(file.getPath());
    }

    public void clearRepository() {
        attachmentFileRepository = new ArrayList<>();
    }
}
