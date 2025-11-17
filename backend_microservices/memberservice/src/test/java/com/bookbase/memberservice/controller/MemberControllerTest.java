package com.bookbase.memberservice.controller;


import com.bookbase.memberservice.entity.Member;
import com.bookbase.memberservice.model.MemberDTO;
import com.bookbase.memberservice.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class MemberControllerTest {

    private MockMvc mockMvc;

    @Mock
    private MemberService memberService;

    @InjectMocks
    private MemberController memberController;

    private Member sampleMember;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(memberController).build();
        objectMapper = new ObjectMapper();

        sampleMember = new Member();
        sampleMember.setMemberID(1);
        sampleMember.setUserName("testuser");
        sampleMember.setEmail("test@example.com");
        sampleMember.setPassword("password");
        sampleMember.setRole("USER");
        sampleMember.setMembershipStatus("Active");
    }

    @Test
    void testGetAllMembers() throws Exception {
        when(memberService.getAllMembers()).thenReturn(List.of(sampleMember));

        mockMvc.perform(get("/member/getallusers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userName").value("testuser"));
    }

    @Test
    void testGetMemberById() throws Exception {
        when(memberService.getMemberById(1)).thenReturn(sampleMember);

        mockMvc.perform(get("/member/getbyid/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value("testuser"));
    }

    @Test
    void testGetByUsername() throws Exception {
        when(memberService.getByUsername("testuser")).thenReturn(sampleMember);

        mockMvc.perform(get("/member/getbyusername/testuser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value("testuser"));
    }

    @Test
    void testRegisterMember() throws Exception {
        MemberDTO dto = new MemberDTO("Name", "test@example.com", "testuser", "password", "USER", "Active");
        when(memberService.registerMember(any(MemberDTO.class))).thenReturn(sampleMember);

        mockMvc.perform(post("/member/createnew")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userName").value("testuser"));
    }

    @Test
    void testUpdateMemberPut() throws Exception {
        MemberDTO dto = new MemberDTO("Updated Name", "updated@example.com", "updateduser", "newpass", "ADMIN", "Inactive");
        sampleMember.setUserName("updateduser");
        when(memberService.updateMember(eq(1), any(MemberDTO.class))).thenReturn(sampleMember);

        mockMvc.perform(put("/member/updateput/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value("updateduser"));
    }

    @Test
    void testUpdateMemberPatch() throws Exception {
        MemberDTO dto = new MemberDTO("Patched Name", null, null, null, null, null);
        sampleMember.setName("Patched Name");
        when(memberService.updateMember(eq(1), any(MemberDTO.class))).thenReturn(sampleMember);

        mockMvc.perform(patch("/member/updatepatch/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Patched Name"));
    }

    @Test
    void testDeleteMember() throws Exception {
        when(memberService.deleteMember(1)).thenReturn("Member deleted with id: 1");

        mockMvc.perform(delete("/member/deleteuser/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Member deleted with id: 1"));
    }
}
