package com.example.notice.dto.response;

import com.example.notice.dto.common.SuccessesAndFails;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class FreeBoardResponseDTO {
    private Long freeBoardId;
    private SuccessesAndFails<String> fileResult;
}
