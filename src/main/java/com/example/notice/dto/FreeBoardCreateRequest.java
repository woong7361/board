package com.example.notice.dto;

import com.example.notice.entity.FreeBoard;
import com.example.notice.entity.Member;
import jakarta.validation.Valid;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * 자유게시판 게시글 생성 REQUEST DTO
 */
@Getter
@Builder
public class FreeBoardCreateRequest {
    @Valid
    private FreeBoard freeBoard;
    private List<Long> fileIds;

    public void setEmptyFileIds() {
        this.fileIds = new ArrayList<>();
    }

    /**
     * 게시글의 주인을 설정해준다.
     * @param member 인증된 회원
     */
    public void setOwner(Member member) {
        freeBoard.setOwner(member);
    }

    //TODO 이런 경우 네이밍을 어떻게 해야할지 -> withDefaultValue? or NotNullable? or origin 그대로
    public List<Long> getFileIds() {
        if (fileIds == null) {
            return new ArrayList<>();
        }

        return this.fileIds;
    }
}
