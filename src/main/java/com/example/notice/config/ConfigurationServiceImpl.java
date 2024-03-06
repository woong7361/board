package com.example.notice.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class ConfigurationServiceImpl implements ConfigurationService{
    @Value("${secret}")
    private String secretKey;
}
