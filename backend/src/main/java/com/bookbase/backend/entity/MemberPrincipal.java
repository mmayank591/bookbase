package com.bookbase.backend.entity;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class MemberPrincipal implements UserDetails {

    private Member member;

    public MemberPrincipal(Member member) {
        this.member = member;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String roleName = member.getRole();
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_" + roleName));
    }


    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getUserName();
    }
}
