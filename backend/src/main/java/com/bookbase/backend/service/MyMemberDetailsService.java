package com.bookbase.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.bookbase.backend.entity.Member;
import com.bookbase.backend.entity.MemberPrincipal;
import com.bookbase.backend.repository.MemberRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MyMemberDetailsService implements UserDetailsService {

    @Autowired
    private MemberRepository repo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Attempting to load user by username={}", username);

        Member member = repo.findByUserName(username);

        if (member == null) {
            log.warn("User not found with username={}", username);
            throw new UsernameNotFoundException("User not found");
        }

        log.info("User loaded successfully with username={} and id={}", member.getUserName(), member.getMemberID());
        return new MemberPrincipal(member);
    }
}