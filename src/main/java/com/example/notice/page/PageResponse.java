package com.example.notice.page;

import lombok.Getter;

import java.util.List;

/**
 * 페이징을 하고 난 후 응답할 wrapper 객체
 * @param <T> 응답할 contents Type
 */
@Getter
public class PageResponse<T> {
    private List<T> contents;

    private Integer pageOffset;
    private Integer currentPage;
    private Integer totalCount;
    private Integer contentSize;
    private Integer pageSize;

    // 방어코드
    public PageResponse(List<T> contents, PageRequest pageRequest, Integer totalCount) {
        this.contents = contents;
        this.pageOffset = pageRequest.getOffset();
        this.currentPage = pageRequest.getCurrentPage();
        this.pageSize = pageRequest.getSize();
        this.totalCount = totalCount;
        this.contentSize = contents == null ? 0 : contents.size();
    }


}