package com.example.notice.service;

import com.example.notice.config.ConfigurationService;
import com.example.notice.entity.NoticeBoard;
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

    //TODO session이니까 memberId가 아닌 member를 전부 사용하는거? 괜찮을듯하다?
    // 굳이인가? -> 관리자의 정보가 세션만료기간 사이에 변할까?
    // 그냥 id만 쓰는게 속편할지도
    @Override
    @Transactional
    public Long createNoticeBoard(NoticeBoard noticeBoard, Long memberId) {
        noticeBoardRepository.save(noticeBoard, memberId);

        return noticeBoard.getNoticeBoardId();
    }

    @Override
    public List<NoticeBoard> getFixedNoticeBoardWithoutContent() {
        return noticeBoardRepository.findFixedNoticeBoardWithoutContentByLimit(configurationService.getMaxNoticeFixedCount());
    }
}
