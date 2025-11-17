package com.bookbase.backend.service;

import com.bookbase.backend.entity.Member;
import com.bookbase.backend.exception.EmailAlreadyExistsException;
import com.bookbase.backend.exception.MemberNotFoundException;
import com.bookbase.backend.exception.UsernameAlreadyExistsException;
import com.bookbase.backend.model.LoginRequestDTO;
import com.bookbase.backend.model.MemberDTO;
import com.bookbase.backend.repository.MemberRepository;

import lombok.extern.slf4j.Slf4j;   

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j   
@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    AuthenticationManager authmanager;

    @Autowired
    private JWTService jwtService;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public List<Member> getAllMembers() {
        log.debug("Fetching all members from repository");
        List<Member> members = memberRepository.findAll();
        log.info("Fetched {} members", members.size());
        return members;
    }

    public Member getMemberById(int id) {
        log.debug("Fetching member with id={}", id);
        return memberRepository.findById(id)
                .map(member -> {
                    log.info("Member found with id={} and username={}", id, member.getUserName());
                    return member;
                })
                .orElseThrow(() -> {
                    log.warn("Member not found with id={}", id);
                    return new MemberNotFoundException("Member not found with id: " + id);
                });
    }

    public Member registerMember(MemberDTO newMemberDTO) {
        log.info("Attempting to register new member with email={} and username={}", 
                 newMemberDTO.getEmail(), newMemberDTO.getUsername());

        if (memberRepository.existsByUserName(newMemberDTO.getUsername())) {
            log.warn("Registration failed: username={} already exists", newMemberDTO.getUsername());
            throw new UsernameAlreadyExistsException("Username already exists!");
        }
        if (memberRepository.existsByEmail(newMemberDTO.getEmail())) {
            log.warn("Registration failed: email={} already exists", newMemberDTO.getEmail());
            throw new EmailAlreadyExistsException("An account with this email already exists!");
        }

        Member newMember = new Member();
        newMember.setName(newMemberDTO.getName());
        newMember.setEmail(newMemberDTO.getEmail());
        newMember.setUserName(newMemberDTO.getUsername());
        newMember.setPassword(encoder.encode(newMemberDTO.getPassword()));
        newMember.setRole(newMemberDTO.getRole().toUpperCase());
        newMember.setMembershipStatus(newMemberDTO.getMembershipStatus());

        Member saved = memberRepository.save(newMember);
        log.info("Member registered successfully with id={} and username={}", saved.getMemberID(), saved.getUserName());
        return saved;
    }

    public Member updateMember(int id, MemberDTO memberDTO) {
        log.info("Updating member with id={}", id);

        Member existingMember = memberRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Member not found with id={} for update", id);
                    return new MemberNotFoundException("Member not found with id: " + id);
                });

        if (memberDTO.getName() != null) existingMember.setName(memberDTO.getName());
        if (memberDTO.getEmail() != null) existingMember.setEmail(memberDTO.getEmail());
        if (memberDTO.getUsername() != null) existingMember.setUserName(memberDTO.getUsername());
        if (memberDTO.getPassword() != null) existingMember.setPassword(encoder.encode(memberDTO.getPassword()));
        if (memberDTO.getRole() != null) existingMember.setRole(memberDTO.getRole());
        if (memberDTO.getMembershipStatus() != null) existingMember.setMembershipStatus(memberDTO.getMembershipStatus());

        Member updated = memberRepository.save(existingMember);
        log.info("Member updated successfully with id={} and username={}", updated.getMemberID(), updated.getUserName());
        return updated;
    }

    public String deleteMember(int id) {
        log.info("Deleting member with id={}", id);
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Member not found with id={} for deletion", id);
                    return new MemberNotFoundException("Member not found with id: " + id);
                });
        memberRepository.delete(member);
        log.info("Member deleted successfully with id={}", id);
        return "Member deleted with id: " + id;
    }

    public String verify(LoginRequestDTO loginRequestDTO) {
        log.info("Login attempt for username={}", loginRequestDTO.getUserName());

        Authentication authentication = authmanager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDTO.getUserName(), loginRequestDTO.getPassword()));

        if (authentication.isAuthenticated()) {
            Member member = getByUsername(loginRequestDTO.getUserName());
            log.info("Login successful for username={} (id={})", member.getUserName(), member.getMemberID());
            return jwtService.generateToken(member);
        }

        log.warn("Login failed for username={}", loginRequestDTO.getUserName());
        return "Failed";
    }

    public Member getByUsername(String username) {
        log.debug("Fetching member by username={}", username);
        Member member = memberRepository.findByUserName(username);
        if (member != null) {
            log.info("Member found with username={} and id={}", username, member.getMemberID());
        } else {
            log.warn("Member not found with username={}", username);
        }
        return member;
    }
}