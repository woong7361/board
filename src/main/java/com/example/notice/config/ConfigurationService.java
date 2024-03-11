package com.example.notice.config;

import java.util.List;

/**
 * 환경변수 서비스
 */
public interface ConfigurationService {
    public String getSecretKey();

    public String getFilePath();

    List<String> getAllowExtension();
}
