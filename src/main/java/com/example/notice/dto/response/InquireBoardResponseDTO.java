package com.example.notice.dto.response;

import com.example.notice.entity.InquireAnswer;
import com.example.notice.entity.InquireBoard;
import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class InquireBoardResponseDTO {
    private String title;

    private InquireBoard inquireBoard;

    private List<InquireAnswer> inquireAnswers;

}
