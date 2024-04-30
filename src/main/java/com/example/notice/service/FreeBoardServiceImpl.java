package com.example.notice.service;

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
import java.util.stream.Collectors;

import static com.example.notice.constant.ErrorMessageConstant.AUTHORIZATION_EXCEPTION_MESSAGE;
import static com.example.notice.constant.ErrorMessageConstant.BOARD_NOT_EXIST_MESSAGE;

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
        freeBoardRepository.save(freeBoard, memberId);
        SuccessesAndFails<AttachmentFile> fileResult = saveFiles(files, freeBoard.getFreeBoardId());

        return freeBoard.getFreeBoardId();
    }

    @Transactional
    @Override
    public FreeBoard getBoardById(Long freeBoardId) {
        freeBoardRepository.increaseViewsByBoardId(freeBoardId);

        return freeBoardRepository.findBoardById(freeBoardId)
                .orElseThrow(() -> new BoardNotExistException(BOARD_NOT_EXIST_MESSAGE));
    }

    @Override
    public PageResponse<FreeBoard> getBoardsBySearchParams(FreeBoardSearchDTO freeBoardSearchDTO, PageRequest pageRequest) {
        Integer totalCount = freeBoardRepository.getTotalCountBySearchParam(freeBoardSearchDTO);
        List<FreeBoard> boards = freeBoardRepository.findBoardsBySearchParam(freeBoardSearchDTO, pageRequest);

        return new PageResponse<>(boards, pageRequest, totalCount);
    }

    /**
     * @implNote comment를 포함하고 있다면 내용과 제목만 지우고 comment는 남겨둔다.
     */
    @Transactional
    @Override
    public void deleteFreeBoardById(Long freeBoardId, Long memberId) {
        checkFreeBoardAuthorization(freeBoardId, memberId);

        deleteFiles(freeBoardId);

        if (freeBoardRepository.hasCommentByBoardId(freeBoardId)) {
            freeBoardRepository.deleteContentAndTitleByBoardId(freeBoardId);
        } else {
            freeBoardRepository.deleteByBoardId(freeBoardId);
        }

    }

    @Transactional
    @Override
    public void updateFreeBoardById(FreeBoard freeBoard, List<MultipartFile> saveFiles, List<Long> deleteFileIds, Long freeBoardId, Long memberId) {
        checkFreeBoardAuthorization(freeBoardId, memberId);

        freeBoardRepository.update(freeBoard, freeBoardId);
        saveFiles(saveFiles, freeBoardId);
        deleteFiles(deleteFileIds);
    }

    @Override
    @Transactional
    public void deleteFreeBoardByAdmin(Long freeBoardId, Long memberId) {
        deleteFiles(freeBoardId);
        freeBoardRepository.deleteByAdmin(freeBoardId);
    }

    @Override
    public List<String> getCategories() {
        return freeBoardRepository.getCategory();
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
                .physicalName(fileUtil.getFileNameFromPath(savedPath))
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
                    physicalFileRepository.delete(fileId);
                });
    }

    private void deleteFiles(Long freeBoardId) {
        List<Long> fileIds = attachmentFileRepository.findByFreeBoardId(freeBoardId)
                .stream()
                .map(af -> af.getFileId())
                .collect(Collectors.toList());
        deleteFiles(fileIds);
    }

    private void checkFreeBoardAuthorization(Long freeBoardId, Long memberId) {
        freeBoardRepository.findBoardByIdAndMemberId(freeBoardId, memberId)
                .orElseThrow(() -> new AuthorizationException(AUTHORIZATION_EXCEPTION_MESSAGE));
    }
}



