package com.example.notice.mock.repository;

import com.example.notice.dto.InquireBoardSearchParam;
import com.example.notice.dto.InquireBoardSearchResponseDTO;
import com.example.notice.entity.InquireBoard;
import com.example.notice.page.PageRequest;
import com.example.notice.repository.InquireBoardRepository;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.notice.mock.database.MemoryDataBase.INQUIRE_BOARD_STORAGE;

public class MockInquireBoardRepository implements InquireBoardRepository {


    @Override
    public Long save(InquireBoard inquireBoard, Long memberId) {
        InquireBoard saveBoard = InquireBoardBuilderMapper(inquireBoard)
                .memberId(memberId)
                .build();

        INQUIRE_BOARD_STORAGE.add(saveBoard);

        return saveBoard.getInquireBoardId();
    }

    @Override
    public List<InquireBoardSearchResponseDTO> search(InquireBoardSearchParam inquireBoardSearchParam, PageRequest pageRequest, Long memberId) {

        return INQUIRE_BOARD_STORAGE.stream()
                .filter((ib) -> {
                    boolean result = true;
                    if (inquireBoardSearchParam.getKeyWord() != null) {
                        result = result && ib.getContent().contains(inquireBoardSearchParam.getKeyWord());
                        result = result && ib.getTitle().contains(inquireBoardSearchParam.getKeyWord());
                    }
                    if (inquireBoardSearchParam.getStartDate() != null && inquireBoardSearchParam.getEndDate() != null) {
                        result = result && ib.getCreatedAt().isBefore(inquireBoardSearchParam.getEndDate());
                        result = result && ib.getCreatedAt().isAfter(inquireBoardSearchParam.getStartDate());
                    }
                    if (inquireBoardSearchParam.getOnlyMine()) {
                        result = result && ib.getMemberId().equals(memberId);
                    }
                    return result;
                })
                //TODO JOIN 해결해야함
                .map(ib -> new InquireBoardSearchResponseDTO(ib.getInquireBoardId(), ib.getMemberId(), ib.getCreatedAt(),
                        ib.getModifiedAt(), ib.getTitle(), ib.getContent(), ib.getViews(), ib.getIsSecret(), false))
                .collect(Collectors.toList());
    }

    @Override
    public Integer getSearchTotalCount(InquireBoardSearchParam inquireBoardSearchParam, Long memberId) {
        return (int) INQUIRE_BOARD_STORAGE.stream()
                .filter((ib) -> {
                    boolean result = true;
                    if (inquireBoardSearchParam.getKeyWord() != null) {
                        result = result && ib.getContent().contains(inquireBoardSearchParam.getKeyWord());
                        result = result && ib.getTitle().contains(inquireBoardSearchParam.getKeyWord());
                    }
                    if (inquireBoardSearchParam.getStartDate() != null && inquireBoardSearchParam.getEndDate() != null) {
                        result = result && ib.getCreatedAt().isBefore(inquireBoardSearchParam.getEndDate());
                        result = result && ib.getCreatedAt().isAfter(inquireBoardSearchParam.getStartDate());
                    }
                    if (inquireBoardSearchParam.getOnlyMine()) {
                        result = result && ib.getMemberId().equals(memberId);
                    }
                    return result;
                })
                .count();
    }

    public static InquireBoard.InquireBoardBuilder InquireBoardBuilderMapper(InquireBoard inquireBoard) {
        return InquireBoard.builder()
                .title(inquireBoard.getTitle())
                .content(inquireBoard.getContent())
                .isSecret(inquireBoard.getIsSecret())
                .memberId(inquireBoard.getMemberId())
                .createdAt(inquireBoard.getCreatedAt())
                .modifiedAt(inquireBoard.getModifiedAt())
                .inquireBoardId(inquireBoard.getInquireBoardId());
    }
}
