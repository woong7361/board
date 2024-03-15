package com.example.notice.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 공용 외부 property 서비스
 */
@Component
@Getter
public class ConfigurationServiceImpl implements ConfigurationService{
    @Value("${secret}")
    private String secretKey;

    @Value("${setting.file-path}")
    private String filePath;

    @Value("${setting.allow-extension}")
    private String allowExtension;

    @Value("${setting.notice-board.max-fixed-size}")
    private Integer maxNoticeFixedCount;


    @Override
    public List<String> getAllowExtension() {
        String[] extensions = allowExtension.split(",");
        return Arrays.stream(extensions)
                .map((extension) -> extension.strip())
                .collect(Collectors.toList());


    }

    @Override
    public Integer getMaxFixedNoticeCount() {
        return maxNoticeFixedCount;
    }
}
