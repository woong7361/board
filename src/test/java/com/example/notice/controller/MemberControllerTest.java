package com.example.notice.controller;


import com.example.notice.entity.Member;
import com.example.notice.service.MockMemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.MethodArgumentNotValidException;

@WebMvcTest(MemberController.class)
class MemberControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
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
                            .post("/api/member")
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
                loginIdTest(null);
            }


            @DisplayName("회원 아이디가 요청 길이를 만족하지 못할때 ")
            @Test
            public void notQualifiedIdLength() throws Exception{
                String lessThan4Letter = "1a";
                loginIdTest(lessThan4Letter);

                String greaterThan12Letter = "1a1a1a1a1a1a1a1aa12";
                loginIdTest(greaterThan12Letter);
            }


            @DisplayName("허용되지 않은 문자가 들어있을 경우")
            @Test
            public void notAllowedCharacterInLoginId() throws Exception{
                //given
                loginIdTest("//abc13");
                loginIdTest("...abc13");
                loginIdTest("abc13../");
            }
        }

        @Nested
        @DisplayName("회원 비밀번호 검증")
        class TestMemberPassword{
            @DisplayName("비밀번호 null 일때")
            @Test
            public void nullPassword() throws Exception{
                passwordTest(null);
            }

            @DisplayName("비밀번호가 요청 길이를 만족하지 못할때")
            @Test
            public void test() throws Exception{
                String lessThan4Letter = "2b";
                passwordTest(lessThan4Letter);

                String greaterThan12Letter = "2b2b2b2b2b2b2b2b2b";
                passwordTest(greaterThan12Letter);
            }

            @DisplayName("비밀번호에 연속된 문자가 3번 나올때")
            @Test
            public void successive3Letter() throws Exception{
                String successiveCharacters = "1a2bccc";
                passwordTest(successiveCharacters);

                String successiveDigits = "222ab2a3";
                passwordTest(successiveDigits);
            }
        }

        @Nested
        @DisplayName("회원 이름 검증")
        class TestMemberName {
            @DisplayName("이름이 요청 길이를 만족하지 못할때")
            @Test
            public void notQualifiedRequestLengthName() throws Exception{
                String LessThan2Letters = "a";
                nameTest(LessThan2Letters);

                String greaterThan5Letters = "five5";
                nameTest(greaterThan5Letters);
            }
        }

        private void loginIdTest(String loginId) throws Exception {
            String successPassword = "456abc";
            String successName = "name";

            Member notAllowedCharacterName = Member
                    .builder()
                    .loginId(loginId)
                    .password(successPassword)
                    .name(successName)
                    .build();
            String notAllowedCharacterNameBody = mapper.writeValueAsString(notAllowedCharacterName);

            //then
            mockMvc.perform(
                            MockMvcRequestBuilders
                                    .post("/api/member")
                                    .content(notAllowedCharacterNameBody)
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(result -> Assertions
                            .assertThat(result.getResolvedException())
                            .isInstanceOf(MethodArgumentNotValidException.class)
                    )
                    .andExpect(MockMvcResultMatchers.status().is4xxClientError());
        }

        private void passwordTest(String password) throws Exception {
            String successLoginId = "456abc";
            String successName = "name";

            Member notAllowedCharacterName = Member
                    .builder()
                    .loginId(successLoginId)
                    .password(password)
                    .name(successName)
                    .build();
            String notAllowedCharacterNameBody = mapper.writeValueAsString(notAllowedCharacterName);

            //then
            mockMvc.perform(
                            MockMvcRequestBuilders
                                    .post("/api/member")
                                    .content(notAllowedCharacterNameBody)
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(result -> Assertions
                            .assertThat(result.getResolvedException())
                            .isInstanceOf(MethodArgumentNotValidException.class)
                    )
                    .andExpect(MockMvcResultMatchers.status().is4xxClientError());
        }

        private void nameTest(String name) throws Exception {
            String successLoginId = "456abc";
            String successPassword = "123abc";

            Member notAllowedCharacterName = Member
                    .builder()
                    .loginId(successLoginId)
                    .password(successPassword)
                    .name(name)
                    .build();
            String notAllowedCharacterNameBody = mapper.writeValueAsString(notAllowedCharacterName);

            //then
            mockMvc.perform(
                            MockMvcRequestBuilders
                                    .post("/api/member")
                                    .content(notAllowedCharacterNameBody)
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(result -> Assertions
                            .assertThat(result.getResolvedException())
                            .isInstanceOf(MethodArgumentNotValidException.class)
                    )
                    .andExpect(MockMvcResultMatchers.status().is4xxClientError());
        }
    }




}