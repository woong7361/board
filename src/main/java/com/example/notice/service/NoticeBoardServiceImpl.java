package com.example.notice.service;

import com.example.notice.config.ConfigurationService;
import com.example.notice.dto.request.NoticeBoardSearchDTO;
import com.example.notice.entity.NoticeBoard;
import com.example.notice.exception.EntityNotExistException;
import com.example.notice.page.PageRequest;
import com.example.notice.page.PageResponse;
import com.example.notice.repository.NoticeBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeBoardServiceImpl implements NoticeBoardService {

    private final NoticeBoardRepository noticeBoardRepository;
    private final ConfigurationService configurationService;


    @Override
    @Transactional
    public Long createNoticeBoard(NoticeBoard noticeBoard, Long memberId) {
        noticeBoardRepository.save(noticeBoard, memberId);

        return noticeBoard.getNoticeBoardId();
    }

    @Override
    public List<NoticeBoard> getFixedNoticeBoardWithoutContent() {
        return noticeBoardRepository.findFixedNoticeBoardByLimit(configurationService.getMaxNoticeFixedCount());
    }

    @Override
    public PageResponse<NoticeBoard> getNoneFixedNoticeBoards(NoticeBoardSearchDTO noticeBoardSearchDTO, PageRequest pageRequest) {
        Integer totalCount = noticeBoardRepository.findNoneFixedNoticeBoardCountBySearchParam(
                noticeBoardSearchDTO,
                configurationService.getMaxNoticeFixedCount());

        List<NoticeBoard> noneFixedNoticeBoards = noticeBoardRepository.findNoneFixedNoticeBoardBySearchParam(
                noticeBoardSearchDTO,
                pageRequest,
                configurationService.getMaxNoticeFixedCount());

        return new PageResponse<>(noneFixedNoticeBoards, pageRequest, totalCount);
    }

    @Override
    public NoticeBoard getNoticeBoardById(Long noticeBoardId) {
        noticeBoardRepository.increaseViewsById(noticeBoardId);

        return noticeBoardRepository.findById(noticeBoardId)
                .orElseThrow(() -> new EntityNotExistException("해당하는 게시글이 존재하지 않는다."));

    }

    @Override
    @Transactional
    public void deleteNoticeBoardById(Long noticeBoardId) {
        noticeBoardRepository.deleteById(noticeBoardId);
    }

    @Override
    @Transactional
    public void updateNoticeBoardById(Long noticeBoardId, NoticeBoard noticeBoard) {
        noticeBoardRepository.updateBoardById(noticeBoardId, noticeBoard);
    }
}
