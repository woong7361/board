package com.example.notice.advice;

import com.example.notice.controller.FreeBoardController;
import com.example.notice.exception.AuthenticationException;
import com.example.notice.exception.AuthorizationException;
import com.example.notice.exception.BadRequestParamException;
import com.example.notice.exception.EntityNotExistException;
import com.example.notice.restdocs.RestDocsHelper;
import com.example.notice.service.FreeBoardService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(FreeBoardController.class)
class JavaControllerAdviceTest extends RestDocsHelper {
    @MockBean
    FreeBoardService freeBoardService;


    private final String GET_FREE_BOARD_URI = "/api/boards/free/{freeBoardId}";

    @DisplayName("BadRequestParamException")
    @Test
    public void BadRequestParamException() throws Exception{
        //given
        String message = "message";

        //when
        Mockito.when(freeBoardService.getBoardById(any()))
                .thenThrow(new BadRequestParamException(message));

        ResultActions action = mockMvc.perform(MockMvcRequestBuilders.get(GET_FREE_BOARD_URI, 3L));

        //then
        action
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.message").value(message));
    }

    @DisplayName("AuthorizationException")
    @Test
    public void AuthorizationException() throws Exception{
        //given
        String message = "message";

        //when
        Mockito.when(freeBoardService.getBoardById(any()))
                .thenThrow(new AuthorizationException(message));

        ResultActions action = mockMvc.perform(MockMvcRequestBuilders.get(GET_FREE_BOARD_URI, 3L));

        //then
        action
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(jsonPath("$.message").value(message));
    }

    @DisplayName("AuthenticationException")
    @Test
    public void AuthenticationException() throws Exception{
        //given
        String message = "message";

        //when
        Mockito.when(freeBoardService.getBoardById(any()))
                .thenThrow(new AuthenticationException(message));

        ResultActions action = mockMvc.perform(MockMvcRequestBuilders.get(GET_FREE_BOARD_URI, 3L));

        //then
        action
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(jsonPath("$.message").value(message));
    }


    @DisplayName("ExpiredJwtException")
    @Test
    public void ExpiredJwtException() throws Exception{
        //given
        String message = "message";

        //when
        Mockito.when(freeBoardService.getBoardById(any()))
                .thenThrow(new ExpiredJwtException(null, null, "message"));

        ResultActions action = mockMvc.perform(MockMvcRequestBuilders.get(GET_FREE_BOARD_URI, 3L));

        //then
        action
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("JWT EXPIRED"));
    }

    @DisplayName("JwtException")
    @Test
    public void JwtException() throws Exception{
        //given
        String message = "message";

        //when
        Mockito.when(freeBoardService.getBoardById(any()))
                .thenThrow(new JwtException(message));

        ResultActions action = mockMvc.perform(MockMvcRequestBuilders.get(GET_FREE_BOARD_URI, 3L));

        //then
        action
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("JWT EXCEPTION"));
    }


    @DisplayName("EntityNotExistException")
    @Test
    public void EntityNotExistException() throws Exception{
        //given
        String message = "message";

        //when
        Mockito.when(freeBoardService.getBoardById(any()))
                .thenThrow(new EntityNotExistException(message));

        ResultActions action = mockMvc.perform(MockMvcRequestBuilders.get(GET_FREE_BOARD_URI, 3L));

        //then
        action
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.message").value(message));
    }

}