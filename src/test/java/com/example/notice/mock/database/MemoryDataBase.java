package com.example.notice.mock.database;

import com.example.notice.entity.AttachmentFile;
import com.example.notice.entity.FreeBoard;
import com.example.notice.entity.Member;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.example.notice.mock.repository.MockFreeBoardRepository.SAVED_FREE_BOARD;
import static com.example.notice.mock.repository.MockMemberRepository.SAVED_ADMIN_MEMBER;
import static com.example.notice.mock.repository.MockMemberRepository.SAVED_MEMBER;

public class MemoryDataBase {
    public static List<FreeBoard> freeBoardRepository = new ArrayList<>();
    public static List<AttachmentFile> attachmentFileRepository = new ArrayList<>();
    public static List<Member> memberRepository = new ArrayList<>();
    public static List<String> physicalFileRepository = new ArrayList<>();

    static {
        freeBoardRepository.add(SAVED_FREE_BOARD);
        memberRepository.add(SAVED_MEMBER);
        memberRepository.add(SAVED_ADMIN_MEMBER);
    }
}
