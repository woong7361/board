package com.example.notice.controller;


import com.example.notice.entity.Member;
import com.example.notice.mock.service.MockMemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.MethodArgumentNotValidException;

@WebMvcTest(MemberController.class)
class MemberControllerTest {
    public static final String REGISTER_URI = "/api/member";

    @Autowired
    private MockMvc mockMvc;
    @SpyBean
    private MockMemberService memberService;
    private ObjectMapper mapper = new ObjectMapper();


    @Nested
    @DisplayName("회원 가입")
    class Register {

        @DisplayName("정상 처리")
        @Test
        public void success() throws Exception{
            //given
            Member member = Member
                    .builder()
                    .loginId("123abc")
                    .password("456abc")
                    .name("name")
                    .build();

            String body = mapper.writeValueAsString(member);

            //then
            mockMvc
                    .perform(MockMvcRequestBuilders
                            .post(REGISTER_URI)
                            .content(body)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(
                            MockMvcResultMatchers.status().isOk()
                    );
        }

        @Nested
        @DisplayName("회원 아이디 검증")
        class TestMemberLoginId{

            @DisplayName("회원 아이디가 null일때")
            @Test
            public void nullMemberName() throws Exception{
                loginIdTest(null, MethodArgumentNotValidException.class, MockMvcResultMatchers.status().isBadRequest());
            }


            @DisplayName("회원 아이디가 요청 길이를 만족하지 못할때 ")
            @Test
            public void notQualifiedIdLength() throws Exception{
                String lessThan4Letter = "1a";
                loginIdTest(lessThan4Letter, MethodArgumentNotValidException.class, MockMvcResultMatchers.status().isBadRequest());

                String greaterThan12Letter = "1a1a1a1a1a1a1a1aa12";
                loginIdTest(greaterThan12Letter, MethodArgumentNotValidException.class, MockMvcResultMatchers.status().isBadRequest());
            }


            @DisplayName("허용되지 않은 문자가 들어있을 경우")
            @Test
            public void notAllowedCharacterInLoginId() throws Exception{
                //given
                loginIdTest("//abc13", MethodArgumentNotValidException.class, MockMvcResultMatchers.status().isBadRequest());
                loginIdTest("...abc13", MethodArgumentNotValidException.class, MockMvcResultMatchers.status().isBadRequest());
                loginIdTest("abc13../", MethodArgumentNotValidException.class, MockMvcResultMatchers.status().isBadRequest());
            }
        }

        @Nested
        @DisplayName("회원 비밀번호 검증")
        class TestMemberPassword{
            @DisplayName("비밀번호 null 일때")
            @Test
            public void nullPassword() throws Exception{
                passwordTest(null, MethodArgumentNotValidException.class, MockMvcResultMatchers.status().isBadRequest());
            }

            @DisplayName("비밀번호가 요청 길이를 만족하지 못할때")
            @Test
            public void test() throws Exception{
                String lessThan4Letter = "2b";
                passwordTest(lessThan4Letter, MethodArgumentNotValidException.class, MockMvcResultMatchers.status().isBadRequest());

                String greaterThan12Letter = "2b2b2b2b2b2b2b2b2b";
                passwordTest(greaterThan12Letter, MethodArgumentNotValidException.class, MockMvcResultMatchers.status().isBadRequest());
            }

            @DisplayName("비밀번호에 연속된 문자가 3번 나올때")
            @Test
            public void successive3Letter() throws Exception{
                String successiveCharacters = "1a2bccc";
                passwordTest(successiveCharacters, MethodArgumentNotValidException.class, MockMvcResultMatchers.status().isBadRequest());

                String successiveDigits = "222ab2a3";
                passwordTest(successiveDigits, MethodArgumentNotValidException.class, MockMvcResultMatchers.status().isBadRequest());
            }

            @DisplayName("비밀번호가 아이디와 같을때")
            @Test
            public void loginIdSameAsPassword() throws Exception{
                //given
                String sameString = "same123";
                Member member = Member
                        .builder()
                        .loginId(sameString)
                        .password(sameString)
                        .name("name")
                        .build();
                String body = mapper.writeValueAsString(member);

                //then
                mockMvc
                        .perform(MockMvcRequestBuilders
                                .post(REGISTER_URI)
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(result -> Assertions
                                .assertThat(result.getResolvedException())
                                .isInstanceOf(IllegalArgumentException.class))
                        .andExpect(MockMvcResultMatchers.status().isBadRequest());
            }
        }

        @Nested
        @DisplayName("회원 이름 검증")
        class TestMemberName {
            @DisplayName("이름이 요청 길이를 만족하지 못할때")
            @Test
            public void notQualifiedRequestLengthName() throws Exception{
                String LessThan2Letters = "a";
                nameTest(LessThan2Letters,
                        MethodArgumentNotValidException.class,
                        MockMvcResultMatchers.status().isBadRequest());

                String greaterThan5Letters = "five5";
                nameTest(greaterThan5Letters,
                        MethodArgumentNotValidException.class,
                        MockMvcResultMatchers.status().isBadRequest());
            }
        }

        private void loginIdTest(String loginId, Class<?> exceptionClass, ResultMatcher statusMatcher) throws Exception {
            registerTest(loginId, "password123", "name",
                    exceptionClass,
                    statusMatcher);
        }

        private void passwordTest(String password, Class<?> exceptionClass, ResultMatcher statusMatcher) throws Exception {
            registerTest("loginId123", password, "name",
                    exceptionClass,
                    statusMatcher);
        }

        private void nameTest(String name, Class<?> exceptionClass, ResultMatcher statusMatcher) throws Exception {
            registerTest("loginId123", "password123", name,
                    exceptionClass,
                    statusMatcher);
        }

        private void registerTest(
                String loginId, String password, String name,
                Class<?> exceptionClass, ResultMatcher statusMatcher
        ) throws Exception {
            Member notAllowedCharacterName = Member
                    .builder()
                    .loginId(loginId)
                    .password(password)
                    .name(name)
                    .build();
            String notAllowedCharacterNameBody = mapper.writeValueAsString(notAllowedCharacterName);

            //then
            mockMvc.perform(
                            MockMvcRequestBuilders
                                    .post(REGISTER_URI)
                                    .content(notAllowedCharacterNameBody)
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(result -> Assertions
                            .assertThat(result.getResolvedException())
                            .isInstanceOf(exceptionClass)
                    )
                    .andExpect(statusMatcher);
        }
    }




}