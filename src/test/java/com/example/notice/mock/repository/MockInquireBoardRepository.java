package com.example.notice.mock.repository;

import com.example.notice.entity.InquireBoard;
import com.example.notice.mock.database.MemoryDataBase;
import com.example.notice.repository.InquireBoardRepository;

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
