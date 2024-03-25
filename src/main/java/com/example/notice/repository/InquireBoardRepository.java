package com.example.notice.repository;

import com.example.notice.dto.InquireBoardSearchParam;
import com.example.notice.dto.InquireBoardSearchResponseDTO;
import com.example.notice.entity.InquireBoard;
import com.example.notice.page.PageRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 문의 게시판 repository
 */
@Mapper
public interface InquireBoardRepository {

    /**
     * 문의 게시판 게시글 생성
     *
     * @param inquireBoard 문의 게시판 게시글 생성 파라미터
     * @param memberId 게시글 작성자의 식별자
     * @return 문의 게시판 게시글 식별자
     */
    Long save(@Param("inquireBoard") InquireBoard inquireBoard, @Param("memberId") Long memberId);

    /**
     * 문의 게시판 게시글 검색
     * @param inquireBoardSearchParam 문의 게시판 게시글 검색 파라미터
     * @param pageRequest 페이징 요청 파라미터
     * @param memberId 검색자의 식별자
     * @return 검색결과
     */
    List<InquireBoardSearchResponseDTO> search(
            @Param("search") InquireBoardSearchParam inquireBoardSearchParam,
            @Param("page") PageRequest pageRequest,
            @Param("memberId") Long memberId);

    /**
     * 문의 게시판 검색 게시글 총 개수
     * @param inquireBoardSearchParam 문의 게시판 게시글 검색 파라미터
     * @param memberId 검색자의 실별자
     * @return 검색결과의 총 개수
     */
    Integer getSearchTotalCount(
            @Param("search") InquireBoardSearchParam inquireBoardSearchParam,
            @Param("memberId") Long memberId);
}



