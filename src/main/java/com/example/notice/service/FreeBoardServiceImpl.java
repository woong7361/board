package com.example.notice.service;

import com.example.notice.dto.common.IdList;
import com.example.notice.dto.common.SuccessesAndFails;
import com.example.notice.dto.request.FreeBoardSearchDTO;
import com.example.notice.entity.AttachmentFile;
import com.example.notice.entity.FreeBoard;
import com.example.notice.exception.AuthorizationException;
import com.example.notice.exception.BoardNotExistException;
import com.example.notice.exception.FileSaveCheckedException;
import com.example.notice.files.PhysicalFileRepository;
import com.example.notice.page.PageRequest;
import com.example.notice.page.PageResponse;
import com.example.notice.repository.AttachmentFileRepository;
import com.example.notice.repository.FreeBoardRepository;
import com.example.notice.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FreeBoardServiceImpl implements FreeBoardService{

    private final FreeBoardRepository freeBoardRepository;
    private final AttachmentFileRepository attachmentFileRepository;
    private final PhysicalFileRepository physicalFileRepository;
    private final FileUtil fileUtil;

    @Override
    @Transactional
    public Long createFreeBoard(FreeBoard freeBoard, List<MultipartFile> files, Long memberId) {
        freeBoardRepository.save(freeBoard);
        SuccessesAndFails<AttachmentFile> fileResult = saveFiles(files, freeBoard.getFreeBoardId());

        return freeBoard.getFreeBoardId();
    }

    /**
     * @implNote 게시글의 조회수 +1
     */
    @Transactional
    @Override
    public FreeBoard getBoardById(Long freeBoardId) {
        freeBoardRepository.increaseViewsByBoardId(freeBoardId);

        return freeBoardRepository.findBoardById(freeBoardId)
                .orElseThrow(() -> new BoardNotExistException("not exist board"));
    }

    @Override
    public PageResponse<FreeBoard> getBoardsBySearchParams(FreeBoardSearchDTO freeBoardSearchDTO, PageRequest pageRequest) {
        Integer totalCount = freeBoardRepository.getTotalCountBySearchParam(freeBoardSearchDTO);
        List<FreeBoard> boards =  freeBoardRepository.findBoardsBySearchParam(freeBoardSearchDTO, pageRequest);

        return new PageResponse<>(boards, pageRequest, totalCount);
    }

    /**
     * @implNote 게시글에 댓글이 달려있다면 삭제하지 않고 게시글의 내용만 지운다.
     */
    @Transactional
    @Override
    public void deleteFreeBoardById(Long freeBoardId) {
        if (freeBoardRepository.hasCommentByBoardId(freeBoardId)) {
            freeBoardRepository.deleteContentAndMemberByBoardId(freeBoardId);
        } else {
            freeBoardRepository.deleteByBoardId(freeBoardId);
        }

    }

    @Transactional
    @Override
    public void updateFreeBoardById(FreeBoard freeBoard, List<MultipartFile> saveFiles, List<Long> deleteFileIds, Long freeBoardId) {
        freeBoardRepository.update(freeBoard, freeBoardId);
        saveFiles(saveFiles, freeBoardId);
        deleteFiles(deleteFileIds);

    }

    @Override
    public void checkFreeBoardAuthorization(Long freeBoardId, Long memberId) {
        freeBoardRepository.findBoardByIdAndMemberId(freeBoardId, memberId)
                .orElseThrow(() -> new AuthorizationException("삭제할 권한이 없습니다."));
    }


    private SuccessesAndFails<AttachmentFile> saveFiles(List<MultipartFile> multipartFiles, Long boardId) {
        SuccessesAndFails<AttachmentFile> results = SuccessesAndFails.emptyList();

        for (MultipartFile multipartFile : multipartFiles) {
            try {
                fileUtil.checkAllowFileExtension(multipartFile);
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
        String savedPath = physicalFileRepository.save(fileUtil.getBytes(multipartFile), multipartFile.getOriginalFilename());

        AttachmentFile attachmentFile = AttachmentFile.builder()
                .originalName(multipartFile.getOriginalFilename())
                .physicalName(fileUtil.getFileName(savedPath))
                .path(fileUtil.getFileSubPath(savedPath))
                .extension(fileUtil.getExtension(savedPath))
                .build();

        attachmentFileRepository.saveWithFreeBoardId(attachmentFile, boardId);
        return attachmentFile;
    }

    private void deleteFiles(List<Long> fileIds) {
        fileIds
                .forEach(id -> deleteFile(id));
    }

    private void deleteFile(Long fileId) {
        attachmentFileRepository.findByFileId(fileId)
                .ifPresent((file) -> {
                    attachmentFileRepository.deleteByFileId(fileId);
                    physicalFileRepository.delete(fileUtil.getFileFullPath(file));
                });
    }
}



