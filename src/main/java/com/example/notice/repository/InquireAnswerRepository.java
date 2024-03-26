package com.example.notice.repository;

import com.example.notice.entity.InquireAnswer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 문의 게시글에 대한 답변 repository
 */
@Mapper
public interface InquireAnswerRepository {

    /**
     * 문의 게시글에 대한 답변 저장
     * @param inquireAnswer 문의 게시글 답변 파라미터
     * @param inquireBoardId 해당 문의 게시글 식별자
     * @param memberId 답변을 작성하는 관리자 식별자
     */
    void save(@Param("inquireAnswer") InquireAnswer inquireAnswer,
            @Param("inquireBoardId") String inquireBoardId,
            @Param("memberId") Long memberId);
}
