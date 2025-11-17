package com.bookbase.memberservice.service;


import com.bookbase.memberservice.entity.Member;
import com.bookbase.memberservice.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class MyMemberDetailsServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MyMemberDetailsService myMemberDetailsService;

    private Member sampleMember;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sampleMember = new Member();
        sampleMember.setMemberID(1);
        sampleMember.setUserName("testuser");
        sampleMember.setPassword("password");
        sampleMember.setRole("USER");
    }

    @Test
    void testLoadUserByUsername_Found() {
        when(memberRepository.findByUserName("testuser")).thenReturn(sampleMember);
        UserDetails userDetails = myMemberDetailsService.loadUserByUsername("testuser");
        assertNotNull(userDetails);
        assertEquals("testuser", userDetails.getUsername());
    }

    @Test
    void testLoadUserByUsername_NotFound() {
        when(memberRepository.findByUserName("unknownuser")).thenReturn(null);
        assertThrows(UsernameNotFoundException.class, () -> myMemberDetailsService.loadUserByUsername("unknownuser"));
    }
}
