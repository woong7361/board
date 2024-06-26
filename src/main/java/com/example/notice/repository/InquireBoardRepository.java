package com.example.notice.repository;

import com.example.notice.dto.request.InquireBoardSearchDTO;
import com.example.notice.dto.response.InquireBoardResponseDTO;
import com.example.notice.dto.response.InquireBoardSearchResponseDTO;
import com.example.notice.entity.InquireBoard;
import com.example.notice.page.PageRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

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
     */
    void save(@Param("inquireBoard") InquireBoard inquireBoard, @Param("memberId") Long memberId);

    /**
     * 문의 게시판 게시글 검색
     * @param inquireBoardSearchDTO 문의 게시판 게시글 검색 파라미터
     * @param pageRequest 페이징 요청 파라미터
     * @return 검색결과
     */
    List<InquireBoardSearchResponseDTO> search(
            @Param("search") InquireBoardSearchDTO inquireBoardSearchDTO,
            @Param("page") PageRequest pageRequest);

    /**
     * 문의 게시판 검색 게시글 총 개수
     * @param inquireBoardSearchDTO 문의 게시판 게시글 검색 파라미터
     * @return 검색결과의 총 개수
     */
    Integer getSearchTotalCount(
            @Param("search") InquireBoardSearchDTO inquireBoardSearchDTO);

    //TODO 도저히 모르겠다. mybatis collection -> collection만 있으면 에러
    /**
     * 게시글 식별자에 해당하는 문의 게시판 게시글 가져오기
     * @param inquireBoardId 게시글 식별자
     * @return 게시글
     */
    Optional<InquireBoardResponseDTO> findById(@Param("inquireBoardId") Long inquireBoardId);

    /**
     * 회원 식별자와 게시글 식별자를 통해 게시글을 검색한다.
     * @param inquireBoardId 게시글 식별자
     * @param memberId 회원 식별자
     * @return 검색된 게시글
     */
    Optional<InquireBoard> findByInquireBoardIdAndMemberId(
            @Param("inquireBoardId") Long inquireBoardId,
            @Param("memberId") Long memberId);

    /**
     * 게시글을 업데이트 한다.
     * @param inquireBoard 게시글 업데이트 요청 파라미터
     * @param inquireBoardId 해당하는 게시글 식별자
     */
    void updateById(@Param("inquireBoard") InquireBoard inquireBoard, @Param("inquireBoardId") Long inquireBoardId);

    /**
     * 게시글 식별자로 게시글을 삭제한다.
     * @param inquireBoardId 삭제하려는 게시글 식별자
     */
    void deleteById(@Param("inquireBoardId") Long inquireBoardId);


    /**
     * 게시글의 조회수를 올린다.
     * @param inquireBoardId 게시글 식별자
     */
    void increaseViewsById(@Param("inquireBoardId")Long inquireBoardId);
}



