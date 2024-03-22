package com.example.notice.dto;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * request param으로 받을 공용 idList DTO
 */
@Getter
public class IdList {
    List<Long> ids = new ArrayList<>();

    //TODO 삭제 해야함 -> test 에서만 쓰는 중 - 차라리 builder를 만드는게?
    public void addId(Long id) {
        ids.add(id);
    }
}
