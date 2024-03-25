package com.example.notice.mock.database;

import com.example.notice.entity.*;

import java.util.ArrayList;
import java.util.List;

import static com.example.notice.mock.repository.MockFreeBoardRepository.SAVED_FREE_BOARD;
import static com.example.notice.mock.repository.MockMemberRepository.SAVED_ADMIN_MEMBER;
import static com.example.notice.mock.repository.MockMemberRepository.SAVED_MEMBER;

public class MemoryDataBase {
    public static List<FreeBoard> FREE_BOARD_STORAGE = new ArrayList<>();
    public static List<NoticeBoard> NOTICE_BOARD_STORAGE = new ArrayList<>();
    public static List<InquireBoard> INQUIRE_BOARD_STORAGE = new ArrayList<>();
    public static List<AttachmentFile> ATTACHMENT_FILE_STORAGE = new ArrayList<>();
    public static List<Member> MEMBER_STORAGE = new ArrayList<>();
    public static List<Comment> COMMENT_STORAGE = new ArrayList<>();
    public static List<String> PHYSICAL_FILE_STORAGE = new ArrayList<>();

    static {
        FREE_BOARD_STORAGE.add(SAVED_FREE_BOARD);
        MEMBER_STORAGE.add(SAVED_MEMBER);
        MEMBER_STORAGE.add(SAVED_ADMIN_MEMBER);
    }

    public static void initRepository() {
        FREE_BOARD_STORAGE = new ArrayList<>();
        NOTICE_BOARD_STORAGE = new ArrayList<>();
        INQUIRE_BOARD_STORAGE = new ArrayList<>();
        ATTACHMENT_FILE_STORAGE = new ArrayList<>();
        MEMBER_STORAGE = new ArrayList<>();
        COMMENT_STORAGE = new ArrayList<>();
        PHYSICAL_FILE_STORAGE = new ArrayList<>();

        FREE_BOARD_STORAGE.add(SAVED_FREE_BOARD);
        MEMBER_STORAGE.add(SAVED_MEMBER);
        MEMBER_STORAGE.add(SAVED_ADMIN_MEMBER);
    }
}
