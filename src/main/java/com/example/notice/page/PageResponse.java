package com.example.notice.page;

import lombok.Getter;

import java.util.List;

@Getter
public class PageResponse<T> {
    private List<T> contents;

    private Integer pageOffset;
    private Integer currentPage;
    private Integer totalCount;
    private Integer contentSize;
    private Integer pageSize;

    public PageResponse(List<T> contents, PageRequest pageRequest, Integer totalCount) {
        this.contents = contents;
        this.pageOffset = pageRequest.getOffset();
        this.currentPage = pageRequest.getCurrentPage();
        this.pageSize = pageRequest.getSize();
        this.totalCount = totalCount;
        this.contentSize = contents == null ? 0 : contents.size();
    }


}