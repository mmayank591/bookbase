package com.bookbase.memberservice.service;

import com.bookbase.memberservice.entity.Member;
import com.bookbase.memberservice.entity.MemberPrincipal;
import com.bookbase.memberservice.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
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