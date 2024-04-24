package com.example.notice.mock.repository;

import com.example.notice.dto.request.InquireBoardSearchDTO;
import com.example.notice.dto.response.InquireBoardResponseDTO;
import com.example.notice.dto.response.InquireBoardSearchResponseDTO;
import com.example.notice.entity.InquireBoard;
import com.example.notice.page.PageRequest;
import com.example.notice.repository.InquireBoardRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.notice.mock.database.MemoryDataBase.INQUIRE_BOARD_STORAGE;

public class MockInquireBoardRepository implements InquireBoardRepository {


    @Override
    public void save(InquireBoard inquireBoard, Long memberId) {
        InquireBoard saveBoard = InquireBoardBuilderMapper(inquireBoard)
                .memberId(memberId)
                .build();

        INQUIRE_BOARD_STORAGE.add(saveBoard);
    }


    @Override
    public List<InquireBoardSearchResponseDTO> search(InquireBoardSearchDTO inquireBoardSearchDTO, PageRequest pageRequest) {

        return INQUIRE_BOARD_STORAGE.stream()
                .filter((ib) -> {
                    boolean result = true;
                    if (inquireBoardSearchDTO.getKeyWord() != null) {
                        result = result && ib.getContent().contains(inquireBoardSearchDTO.getKeyWord());
                        result = result && ib.getTitle().contains(inquireBoardSearchDTO.getKeyWord());
                    }
                    if (inquireBoardSearchDTO.getStartDate() != null && inquireBoardSearchDTO.getEndDate() != null) {
                        result = result && ib.getCreatedAt().isBefore(inquireBoardSearchDTO.getEndDate());
                        result = result && ib.getCreatedAt().isAfter(inquireBoardSearchDTO.getStartDate());
                    }
                    if (inquireBoardSearchDTO.getSearchMemberId() != null) {
                        result = result && ib.getMemberId().equals(inquireBoardSearchDTO.getSearchMemberId());
                    }
                    return result;
                })

                .map(ib -> new InquireBoardSearchResponseDTO(ib.getInquireBoardId(), ib.getMemberId(), ib.getMemberName(), ib.getCreatedAt(),
                        ib.getModifiedAt(), ib.getTitle(), ib.getContent(), ib.getIsSecret(), ib.getViews(), ib.getIsSecret()))
                .collect(Collectors.toList());
    }

    @Override
    public Integer getSearchTotalCount(InquireBoardSearchDTO inquireBoardSearchDTO) {
        return (int) INQUIRE_BOARD_STORAGE.stream()
                .filter((ib) -> {
                    boolean result = true;
                    if (inquireBoardSearchDTO.getKeyWord() != null) {
                        result = result && ib.getContent().contains(inquireBoardSearchDTO.getKeyWord());
                        result = result && ib.getTitle().contains(inquireBoardSearchDTO.getKeyWord());
                    }
                    if (inquireBoardSearchDTO.getStartDate() != null && inquireBoardSearchDTO.getEndDate() != null) {
                        result = result && ib.getCreatedAt().isBefore(inquireBoardSearchDTO.getEndDate());
                        result = result && ib.getCreatedAt().isAfter(inquireBoardSearchDTO.getStartDate());
                    }
                    if (inquireBoardSearchDTO.getSearchMemberId() != null) {
                        result = result && ib.getMemberId().equals(inquireBoardSearchDTO.getSearchMemberId());
                    }
                    return result;
                })
                .count();
    }

    @Override
    public Optional<InquireBoardResponseDTO> findById(Long inquireBoardId) {
        return Optional.empty();
    }

    @Override
    public Optional<InquireBoard> findByInquireBoardIdAndMemberId(Long inquireBoardId, Long memberId) {
        return INQUIRE_BOARD_STORAGE.stream()
                .filter(inquireBoard -> inquireBoard.getInquireBoardId().equals(inquireBoardId))
                .filter(inquireBoard -> inquireBoard.getMemberId().equals(memberId))
                .findFirst();
    }

    @Override
    public void updateById(InquireBoard inquireBoard, Long inquireBoardId) {
    }

    @Override
    public void deleteById(Long inquireBoardId) {
        INQUIRE_BOARD_STORAGE
                .removeIf(inquireBoard -> inquireBoard.getInquireBoardId().equals(inquireBoardId));
    }

    @Override
    public void increaseViewsById(Long inquireBoardId) {
        INQUIRE_BOARD_STORAGE.stream()
                .filter(ib -> ib.getInquireBoardId().equals(inquireBoardId))
                .findFirst()
                .ifPresent(ib -> {
                    ib = InquireBoardBuilderMapper(ib)
                            .views(ib.getViews() + 1L)
                            .build();
                });
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
