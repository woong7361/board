package com.example.notice.mock.service;

import com.example.notice.config.ConfigurationService;

public class MockConfigurationService implements ConfigurationService {
    @Override
    public String getSecretKey() {
        return "123456789asdfghjklzxcvbnmqwertyuio123456789123456789";
    }
}
