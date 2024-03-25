package com.example.notice.service;

import com.example.notice.entity.InquireBoard;
import com.example.notice.mock.database.MemoryDataBase;
import com.example.notice.mock.repository.MockInquireBoardRepository;
import com.example.notice.repository.InquireBoardRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.example.notice.mock.database.MemoryDataBase.initRepository;

class InquireBoardServiceTest {

    private InquireBoardRepository inquireBoardRepository = new MockInquireBoardRepository();
    private InquireBoardService inquireBoardService = new InquireBoardServiceImpl(inquireBoardRepository);

    @BeforeEach
    public void clearRepository() {
        initRepository();
    }

    @Nested
    @DisplayName("문의게시판 생성 테스트")
    public class InquireBoardCreateTest {


        @DisplayName("정상 처리")
        @Test
        public void success() throws Exception {
            //given
            InquireBoard inquireBoard = InquireBoard.builder()
                    .inquireBoardId(5654L)
                    .build();

            Long memberId = 3545631L;

            //when
            inquireBoardService.createBoard(inquireBoard, memberId);

            //then
            Optional<InquireBoard> findBoard = MemoryDataBase.INQUIRE_BOARD_STORAGE.stream()
                    .filter((b) -> b.getInquireBoardId().equals(inquireBoard.getInquireBoardId()))
                    .findFirst();

            Assertions.assertThat(findBoard.isPresent()).isTrue();
        }
    }
}