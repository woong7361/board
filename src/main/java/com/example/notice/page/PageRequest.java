package com.example.notice.page;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 페이지네이션 요청 파라미터
 */
@Getter
@AllArgsConstructor
public class PageRequest {
    private Integer size;
    private Integer currentPage;

    @Pattern(regexp = "[a-zA-z]")
    private String orderColumn;
    @Pattern(regexp = "[a-zA-z]")
    private String orderType;

    public int getOffset() {
        return size * currentPage;
    }
}

