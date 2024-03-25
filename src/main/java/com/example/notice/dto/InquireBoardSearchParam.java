package com.example.notice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 문의게시판 검색 요청 파라미터 클래스
 */
@Getter
@AllArgsConstructor
public class InquireBoardSearchParam {

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private String keyWord;
    private Boolean onlyMine;
}
