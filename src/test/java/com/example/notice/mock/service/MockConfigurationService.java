package com.example.notice.mock.service;

import com.example.notice.config.ConfigurationService;

import java.util.ArrayList;
import java.util.List;

public class MockConfigurationService implements ConfigurationService {

    public static final String SECRET_KEY = "123456789asdfghjklzxcvbnmqwertyuio123456789123456789";
    public static ArrayList<String> ALLOW_EXTENSIONS;

    static{
        ALLOW_EXTENSIONS = new ArrayList<>();
        ALLOW_EXTENSIONS.add("jpg");
        ALLOW_EXTENSIONS.add("png");
        ALLOW_EXTENSIONS.add("gif");
        ALLOW_EXTENSIONS.add("zip");
    }

    @Override
    public String getSecretKey() {
        return SECRET_KEY;
    }

    @Override
    public String getFilePath() {
        return "/home/woong/workspace/project/notice/file";
    }

    @Override
    public List<String> getAllowExtension() {
        return ALLOW_EXTENSIONS;
    }

}
