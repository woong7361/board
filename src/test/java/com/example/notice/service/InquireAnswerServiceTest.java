package com.example.notice.service;

import com.example.notice.entity.InquireAnswer;
import com.example.notice.repository.InquireAnswerRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;



class InquireAnswerServiceTest {
    InquireAnswerRepository inquireAnswerRepository = Mockito.mock(InquireAnswerRepository.class);

    private InquireAnswerService inquireAnswerService = new InquireAnswerServiceImpl(inquireAnswerRepository);


    @Nested
    @DisplayName("문의 게시판 게시글에 대한 답변 생성 테스트")
    public class InquireAnswerCreateTest {

        @DisplayName("정상 처리")
        @Test
        public void success() throws Exception {
            //given
            long memberId = 731561L;
            long inquireBoardId = 414354532L;
            InquireAnswer newAnswer = InquireAnswer.builder()
                    .inquireBoardId(inquireBoardId)
                    .answer("new answer")
                    .build();

            //when
            Long result = inquireAnswerService.createAnswer(newAnswer, inquireBoardId, memberId);

            //then
            Assertions.assertThat(result)
                    .isEqualTo(newAnswer.getInquireAnswerId());
        }
    }
}