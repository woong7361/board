package com.example.notice.config;

import java.util.List;

/**
 * 환경변수 서비스
 */
public interface ConfigurationService {

    /**
     * 인증 서비스의 비밀키를 반환한다.
     * @return 비밀키
     */
    public String getSecretKey();

    /**
     * 물리적 파일 저장 경로를 반환한다
     * @return 기본 저장 경로
     */
    public String getFilePath();

    /**
     * 파일 저장시 허용하는 확장자들을 반환한다.
     * @return 허용하는 확장자들
     */
    List<String> getAllowExtension();

    /**
     * 상단 최대 공지 수를 반환한다.
     * @return 상단 고정된 최대 공지 수
     */
    Integer getMaxNoticeFixedCount();
}
