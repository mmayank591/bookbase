package com.bookbase.memberservice.service;


import com.bookbase.memberservice.entity.Member;
import com.bookbase.memberservice.exception.EmailAlreadyExistsException;
import com.bookbase.memberservice.exception.MemberNotFoundException;
import com.bookbase.memberservice.exception.UsernameAlreadyExistsException;
import com.bookbase.memberservice.model.LoginRequestDTO;
import com.bookbase.memberservice.model.MemberDTO;
import com.bookbase.memberservice.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private AuthenticationManager authManager;

    @Mock
    private JWTService jwtService;

    @InjectMocks
    private MemberService memberService;

    private Member sampleMember;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sampleMember = new Member();
        sampleMember.setMemberID(1);
        sampleMember.setUserName("testuser");
        sampleMember.setEmail("test@example.com");
        sampleMember.setPassword("password");
        sampleMember.setRole("USER");
        sampleMember.setMembershipStatus("Active");
    }

    @Test
    void testGetAllMembers() {
        when(memberRepository.findAll()).thenReturn(List.of(sampleMember));
        List<Member> members = memberService.getAllMembers();
        assertEquals(1, members.size());
        assertEquals("testuser", members.get(0).getUserName());
    }

    @Test
    void testGetMemberById_Found() {
        when(memberRepository.findById(1)).thenReturn(Optional.of(sampleMember));
        Member member = memberService.getMemberById(1);
        assertEquals("testuser", member.getUserName());
    }

    @Test
    void testGetMemberById_NotFound() {
        when(memberRepository.findById(2)).thenReturn(Optional.empty());
        assertThrows(MemberNotFoundException.class, () -> memberService.getMemberById(2));
    }

    @Test
    void testRegisterMember_Success() {
        MemberDTO dto = new MemberDTO("Name", "test@example.com", "testuser", "password", "USER", "Active");
        when(memberRepository.existsByUserName("testuser")).thenReturn(false);
        when(memberRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(memberRepository.save(any(Member.class))).thenReturn(sampleMember);

        Member registered = memberService.registerMember(dto);
        assertEquals("testuser", registered.getUserName());
    }

    @Test
    void testRegisterMember_UsernameExists() {
        MemberDTO dto = new MemberDTO("Name", "test@example.com", "testuser", "password", "USER", "Active");
        when(memberRepository.existsByUserName("testuser")).thenReturn(true);
        assertThrows(UsernameAlreadyExistsException.class, () -> memberService.registerMember(dto));
    }

    @Test
    void testRegisterMember_EmailExists() {
        MemberDTO dto = new MemberDTO("Name", "test@example.com", "testuser", "password", "USER", "Active");
        when(memberRepository.existsByUserName("testuser")).thenReturn(false);
        when(memberRepository.existsByEmail("test@example.com")).thenReturn(true);
        assertThrows(EmailAlreadyExistsException.class, () -> memberService.registerMember(dto));
    }

    @Test
    void testUpdateMember_Found() {
        MemberDTO dto = new MemberDTO("Updated Name", "updated@example.com", "updateduser", "newpass", "ADMIN", "Inactive");
        when(memberRepository.findById(1)).thenReturn(Optional.of(sampleMember));
        when(memberRepository.save(any(Member.class))).thenReturn(sampleMember);

        Member updated = memberService.updateMember(1, dto);
        assertEquals("updateduser", updated.getUserName());
    }

    @Test
    void testUpdateMember_NotFound() {
        MemberDTO dto = new MemberDTO();
        when(memberRepository.findById(2)).thenReturn(Optional.empty());
        assertThrows(MemberNotFoundException.class, () -> memberService.updateMember(2, dto));
    }

    @Test
    void testDeleteMember_Found() {
        when(memberRepository.findById(1)).thenReturn(Optional.of(sampleMember));
        doNothing().when(memberRepository).delete(sampleMember);
        String result = memberService.deleteMember(1);
        assertEquals("Member deleted with id: 1", result);
    }

    @Test
    void testDeleteMember_NotFound() {
        when(memberRepository.findById(2)).thenReturn(Optional.empty());
        assertThrows(MemberNotFoundException.class, () -> memberService.deleteMember(2));
    }

    @Test
    void testVerify_Success() {
        LoginRequestDTO loginDTO = new LoginRequestDTO("testuser", "password");
        Authentication auth = mock(Authentication.class);
        when(auth.isAuthenticated()).thenReturn(true);
        when(authManager.authenticate(any())).thenReturn(auth);
        when(memberRepository.findByUserName("testuser")).thenReturn(sampleMember);
        when(jwtService.generateToken(sampleMember)).thenReturn("token123");

        String token = memberService.verify(loginDTO);
        assertEquals("token123", token);
    }

    @Test
    void testVerify_Failure() {
        LoginRequestDTO loginDTO = new LoginRequestDTO("testuser", "password");
        Authentication auth = mock(Authentication.class);
        when(auth.isAuthenticated()).thenReturn(false);
        when(authManager.authenticate(any())).thenReturn(auth);

        String result = memberService.verify(loginDTO);
        assertEquals("Failed", result);
    }

    @Test
    void testGetByUsername() {
        when(memberRepository.findByUserName("testuser")).thenReturn(sampleMember);
        Member member = memberService.getByUsername("testuser");
        assertEquals("testuser", member.getUserName());
    }
}
