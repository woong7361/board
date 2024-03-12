package com.example.notice.page;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class PageResponse<T> {
    private List<T> content;

    private Integer pageOffset;
    private Integer currentPage;
    private Integer totalCount;
    private Integer contentSize;
    private Integer pageSize;

    public PageResponse(List<T> content, PageRequest pageRequest, Integer totalCount) {
        this.content = content;
        this.pageOffset = pageRequest.getOffset();
        this.currentPage = pageRequest.getCurrentPage();
        this.pageSize = pageRequest.getSize();
        this.totalCount = totalCount;
        this.contentSize = content == null ? 0 : content.size();
    }


}