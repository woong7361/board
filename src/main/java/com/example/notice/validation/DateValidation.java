package com.example.notice.validation;

import com.example.notice.config.ConfigurationService;
import com.example.notice.exception.BadRequestParamException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * 복잡한 날짜 로직을 검증한다.
 */
@Component
@RequiredArgsConstructor
public class DateValidation {
    private final ConfigurationService configurationService;

    /**
     * 검색 기간이 알맞는지 검증한다.
     * @param startDate 시작 기간
     * @param endDate 만료 기간
     */

    // TODO config를 외부로 옮기는 것이 더 좋을까? -> 가독적인 부분과 응집도
    public void checkMaxSearchRange(LocalDateTime startDate, LocalDateTime endDate) {
        if (getYearPeriodBetween(startDate, endDate) > configurationService.getSearchMaxYearRange()) {
            throw new BadRequestParamException("최대 날짜 범위는 %s년 이하 입니다.".formatted(configurationService.getSearchMaxYearRange()));
        }
    }

    private Long getYearPeriodBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return ChronoUnit.YEARS.between(startDate, endDate);
    }

}
