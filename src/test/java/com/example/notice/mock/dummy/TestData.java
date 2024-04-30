package com.example.notice.mock.dummy;

import com.example.notice.entity.*;

import java.time.LocalDateTime;

public class TestData {
    public static FreeBoard getSavedFreeBoard() {
        return getSavedFreeBoard(64678561L);
    }

    public static FreeBoard getSavedFreeBoard(Long freeBoardId) {
            return   FreeBoard.builder()
                    .freeBoardId(freeBoardId)
                    .member(Member.builder()
                            .memberId(75634864L)
                            .name("mNa")
                            .build())
                    .createdAt(LocalDateTime.now().minusMonths(1L))
                    .modifiedAt(LocalDateTime.now())
                    .views(456341L)
                    .title("title")
                    .content("content")
                    .category("category")
                    .build();
    }

    public static InquireBoard getInquireBoardCreateRequest(boolean isSecret) {
        return InquireBoard.builder()
                .title("title")
                .content("content")
                .isSecret(isSecret)
                .build();
    }

    public static InquireBoard getSavedInquireBoard() {
        return getSavedInquireBoard(56341L);
    }
    public static InquireBoard getSavedInquireBoard(Long inquireBoardId) {
        return getSavedInquireBoard(56341L, false);
    }

    public static InquireBoard getSavedInquireBoard(Long inquireBoardId, boolean isSecret) {
        return InquireBoard.builder()
                .inquireBoardId(inquireBoardId)
                .title("title")
                .isSecret(isSecret)
                .content("key")
                .createdAt(LocalDateTime.now().minusMonths(1L))
                .modifiedAt(LocalDateTime.now())
                .memberId(45646L)
                .memberName("mam")
                .build();
    }


    public static InquireAnswer getSavedInquireAnswer(Long inquireBoardId) {
        return InquireAnswer.builder()
                .inquireAnswerId(43416445L)
                .inquireBoardId(inquireBoardId)
                .answer("answer content")
                .memberId(45341132L)
                .memberName("pan")
                .createdAt(LocalDateTime.now().minusMonths(6L))
                .modifiedAt(LocalDateTime.now())
                .build();
    }

    public static NoticeBoard getSavedNoticeBoard(Long noticeBoardId, Long memberId, Boolean isFixed) {
        return NoticeBoard.builder()
                .noticeBoardId(noticeBoardId)
                .category("category")
                .title("notice title")
                .content("notice content")
                .isFixed(isFixed)
                .views(4563153L)
                .memberId(memberId)
                .memberName("pnf")
                .createdAt(LocalDateTime.now().minusMonths(4L))
                .modifiedAt(LocalDateTime.now())
                .build();
    }

    public static NoticeBoard getSavedNoticeBoard(Long noticeBoardId) {
        return getSavedNoticeBoard(noticeBoardId, 46341L, false);
    }


}
