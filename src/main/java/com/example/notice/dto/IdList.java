package com.example.notice.dto;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class IdList {
    List<Long> ids = new ArrayList<>();

    public void addId(Long id) {
        ids.add(id);
    }
}
