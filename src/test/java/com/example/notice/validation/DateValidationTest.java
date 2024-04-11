package com.example.notice.validation;

import com.example.notice.config.ConfigurationService;
import com.example.notice.exception.BadRequestParamException;
import com.example.notice.mock.service.MockConfigurationService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

class DateValidationTest {

    private DateValidation dateValidation = new DateValidation();


    @Nested
    @DisplayName("날짜 검증기 테스트")
    public class DateValidateTest {
        @DisplayName("정상 처리")
        @Test
        public void success() throws Exception {
            //given
            LocalDateTime startDate = LocalDateTime.now().minusMonths(3L);
            LocalDateTime endDate = LocalDateTime.now().minusMonths(2L);

            //when
            //then
            dateValidation.checkMaxRange(startDate, endDate, 1L);
        }

        @DisplayName("지정한 최대 검색 범위를 넘어갈때")
        @Test
        public void beyondMaxRange() throws Exception{
            //given
            LocalDateTime startDate = LocalDateTime.now().minusYears(2L);
            LocalDateTime endDate = LocalDateTime.now();

            //when
            //then
            Assertions.assertThatThrownBy(() -> dateValidation.checkMaxRange(startDate, endDate, 1L))
                    .isInstanceOf(BadRequestParamException.class);
        }
    }

}