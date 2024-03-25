package com.example.notice.dto;

import com.example.notice.entity.InquireBoard;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class InquireBoardSearchResponseDTO extends InquireBoard {
    private Boolean isAnswered;
}
