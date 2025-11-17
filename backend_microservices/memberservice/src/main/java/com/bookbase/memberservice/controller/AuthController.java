package com.bookbase.memberservice.controller;

import com.bookbase.memberservice.entity.Member;
import com.bookbase.memberservice.model.LoginRequestDTO;
import com.bookbase.memberservice.model.MemberDTO;
import com.bookbase.memberservice.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "Authentication API", description = "Endpoints for User Registration and User Login")
public class AuthController {

    @Autowired
    private MemberService memberService;

    @Operation(summary = "Create a Member", description = "Registers a new member during Sign Up")
    @PostMapping("/register")
    public ResponseEntity<Member> registerMember(@RequestBody MemberDTO newMemberDTO) {
        log.info("Registering new member with email={}", newMemberDTO.getEmail());
        Member member = memberService.registerMember(newMemberDTO);
        log.info("Member registered successfully with");
        return new ResponseEntity<>(member, HttpStatus.CREATED);
    }

    @Operation(summary = "Post the Credentials during Login", description = "Posts the credentials during Login and get the Token in return")
    @PostMapping("/login")
    public String login(@RequestBody LoginRequestDTO loginRequestDTO) {
        log.info("Login attempt ");
        String token = memberService.verify(loginRequestDTO);
        log.info("Login successful");
        return token;
    }
}
