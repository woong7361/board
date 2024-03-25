package com.example.notice.service;

import com.example.notice.dto.InquireBoardSearchParam;
import com.example.notice.dto.InquireBoardSearchResponseDTO;
import com.example.notice.entity.InquireBoard;
import com.example.notice.mock.database.MemoryDataBase;
import com.example.notice.mock.repository.MockInquireBoardRepository;
import com.example.notice.page.PageRequest;
import com.example.notice.page.PageResponse;
import com.example.notice.repository.InquireBoardRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.example.notice.mock.database.MemoryDataBase.initRepository;

class InquireBoardServiceTest {

    private InquireBoardRepository inquireBoardRepository = new MockInquireBoardRepository();
    private InquireBoardService inquireBoardService = new InquireBoardServiceImpl(inquireBoardRepository);

    @AfterEach
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

    @Nested
    @DisplayName("문의 게시판 게시글 검색 테스트")
    public class InquireBoardSearchTest {
        @DisplayName("정상 처리")
        @Test
        public void success() throws Exception {
            //given
            InquireBoard inquireBoard = InquireBoard.builder()
                    .inquireBoardId(153L)
                    .title("title key")
                    .isSecret(true)
                    .content("key")
                    .createdAt(LocalDateTime.now())
                    .modifiedAt(LocalDateTime.now())
                    .memberId(45343L)
                    .build();

            InquireBoardSearchParam searchParam =
                    new InquireBoardSearchParam(LocalDateTime.now().minusYears(1L), LocalDateTime.now(), "key", false);
            PageRequest pageRequest = new PageRequest(10, 2, null, null);
            Long memberId = 53145L;

            //when
            MemoryDataBase.INQUIRE_BOARD_STORAGE.add(inquireBoard);
            PageResponse<InquireBoardSearchResponseDTO> result =
                    inquireBoardService.searchInquireBoard(searchParam, pageRequest, memberId);

            //then
            Assertions.assertThat(result.getTotalCount()).isEqualTo(1);
        }

        @DisplayName("저장소가 비어있을때")
        @Test
        public void emptyRepository() throws Exception{
            //given
            InquireBoardSearchParam searchParam =
                    new InquireBoardSearchParam(LocalDateTime.now().minusYears(1L), LocalDateTime.now(), "key", false);
            PageRequest pageRequest = new PageRequest(10, 2, null, null);
            Long memberId = 53145L;

            //when
            PageResponse<InquireBoardSearchResponseDTO> result =
                    inquireBoardService.searchInquireBoard(searchParam, pageRequest, memberId);

            //then
            Assertions.assertThat(result.getTotalCount()).isEqualTo(0);
            Assertions.assertThat(result.getContents().size()).isEqualTo(0);
        }
    }
}