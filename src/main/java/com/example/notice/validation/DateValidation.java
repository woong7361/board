package com.example.notice.validation;

import com.example.notice.exception.BadRequestParamException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static com.example.notice.constant.ErrorMessageConstant.BEYOND_MAX_SEARCH_RANGE_MESSAGE;

/**
 * 복잡한 날짜 로직을 검증한다.
 */
@Component
@RequiredArgsConstructor
public class DateValidation {

    /**
     * 검색 기간이 알맞는지 검증한다.
     * @param startDate 시작 기간
     * @param endDate 만료 기간
     */
    public void checkMaxRange(LocalDateTime startDate, LocalDateTime endDate, Long maxRange) {
        if (getYearPeriodBetween(startDate, endDate) > maxRange) {
            throw new BadRequestParamException(BEYOND_MAX_SEARCH_RANGE_MESSAGE);
        }
    }

    private Long getYearPeriodBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return ChronoUnit.YEARS.between(startDate, endDate);
    }

}
