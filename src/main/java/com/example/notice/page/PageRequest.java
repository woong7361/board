package com.example.notice.page;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 페이지네이션 요청 파라미터
 */
@Getter
@AllArgsConstructor
public class PageRequest {
    @NotNull
    private Integer size;
    @NotNull
    private Integer currentPage;

    @Pattern(regexp = "[a-zA-z]{0,}")
    private String orderColumn;

    @Pattern(regexp = "[a-zA-z_]{0,}")
    private String orderType;

    public Integer getOffset() {
        return size != null && currentPage != null ? size * (currentPage - 1) : null;
    }
}

