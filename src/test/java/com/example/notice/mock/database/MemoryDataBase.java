package com.example.notice.mock.database;

import com.example.notice.entity.AttachmentFile;
import com.example.notice.entity.Comment;
import com.example.notice.entity.FreeBoard;
import com.example.notice.entity.Member;

import java.util.ArrayList;
import java.util.List;

import static com.example.notice.mock.repository.MockFreeBoardRepository.SAVED_FREE_BOARD;
import static com.example.notice.mock.repository.MockMemberRepository.SAVED_ADMIN_MEMBER;
import static com.example.notice.mock.repository.MockMemberRepository.SAVED_MEMBER;

public class MemoryDataBase {
    public static List<FreeBoard> FREE_BOARD_STORAGE = new ArrayList<>();
    public static List<AttachmentFile> ATTACHMENT_FILE_STORAGE = new ArrayList<>();
    public static List<Member> MEMBER_STORAGE = new ArrayList<>();
    public static List<Comment> COMMENT_STORAGE = new ArrayList<>();
    public static List<String> PHYSICAL_FILE_STORAGE = new ArrayList<>();

    static {
        FREE_BOARD_STORAGE.add(SAVED_FREE_BOARD);
        MEMBER_STORAGE.add(SAVED_MEMBER);
        MEMBER_STORAGE.add(SAVED_ADMIN_MEMBER);
    }

    public static void clearCommentRepository() {
        COMMENT_STORAGE = new ArrayList<>();
    }
}
