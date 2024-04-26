package com.example.notice.restdocs;

import com.example.notice.auth.provider.AuthProvider;
import com.example.notice.auth.provider.JwtAuthProvider;
import com.example.notice.config.ConfigurationService;
import com.example.notice.config.ConfigurationServiceImpl;
import com.example.notice.mock.util.AuthenticationTestUtil;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.context.TestPropertySource;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;

@TestConfiguration
public class RestDocsConfig {

    @Bean
    public RestDocumentationResultHandler restDocumentationResultHandler() {
        return MockMvcRestDocumentation.document(
                "{class-name}/{method-name}",  // 문서 이름 설정
                preprocessRequest(
                        modifyHeaders()
                                .remove("Content-Length"),
                        prettyPrint()),
                preprocessResponse(
                        modifyHeaders()
                                .remove("Content-Length"),
                        prettyPrint())
        );
    }

    @Bean
    public AuthProvider authProvider() {
        return new JwtAuthProvider(configurationService());
    }

    @Bean
    public ConfigurationService configurationService() {
        return new ConfigurationServiceImpl();
    }

    @Bean
    public AuthenticationTestUtil authenticationTestUtil() {
        return new AuthenticationTestUtil(authProvider());
    }
}