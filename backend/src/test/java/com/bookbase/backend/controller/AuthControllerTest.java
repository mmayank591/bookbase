package com.bookbase.backend.controller;

import com.bookbase.backend.entity.Member;
import com.bookbase.backend.model.LoginRequestDTO;
import com.bookbase.backend.model.MemberDTO;
import com.bookbase.backend.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private MemberService memberService;

    @InjectMocks
    private AuthController authController;

    private Member sampleMember;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
        objectMapper = new ObjectMapper();

        sampleMember = new Member();
        sampleMember.setMemberID(1);
        sampleMember.setUserName("testuser");
        sampleMember.setEmail("test@example.com");
        sampleMember.setPassword("password");
        sampleMember.setRole("USER");
        sampleMember.setMembershipStatus("ACTIVE");
    }

    @Test
    void testRegisterMember() throws Exception {
        MemberDTO dto = new MemberDTO("Name", "test@example.com", "testuser", "password", "USER", "ACTIVE");
        when(memberService.registerMember(any(MemberDTO.class))).thenReturn(sampleMember);

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userName").value("testuser"));
    }

    @Test
    void testLogin() throws Exception {
        LoginRequestDTO loginDTO = new LoginRequestDTO("testuser", "password");
        when(memberService.verify(any(LoginRequestDTO.class))).thenReturn("token123");

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("token123"));
    }
}
