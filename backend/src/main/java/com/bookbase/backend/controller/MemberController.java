package com.bookbase.backend.controller;

import com.bookbase.backend.entity.Member;
import com.bookbase.backend.model.MemberDTO;
import com.bookbase.backend.service.MemberService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.extern.slf4j.Slf4j;   

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j   
@RestController
@RequestMapping("/member")
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "Members API", description = "CRUD operation for Members")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @Operation(summary = "Fetch all the Member Details", description = "Returns the details of all the members stored in the database")
    @GetMapping("/getallusers")
    public List<Member> getAllMembers() {
        log.info("Fetching all members");
        List<Member> members = memberService.getAllMembers();
        log.info("Found {} members", members.size());
        return members;
    }

    @Operation(summary = "Fetch the details of a Member by MemberID", description = "Returns the details of a member using the MemberID")
    @GetMapping("/getbyid/{id}")
    public ResponseEntity<Member> getMemberById(@PathVariable Integer id) {
        log.info("Fetching member with id={}", id);
        Member member = memberService.getMemberById(id);
        if (member != null) {
            log.info("Member found with id={} ", id);
            return new ResponseEntity<>(member, HttpStatus.OK);
        } else {
            log.warn("Member with id={} not found", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Fetch the details of a Member by Username", description = "Returns the details of a member using the Username")
    @GetMapping("/getbyusername/{username}")
    public ResponseEntity<Member> getByUsername(@PathVariable String username){
        log.info("Fetching member by username={}", username);
        Member member = memberService.getByUsername(username);
        if (member != null) {
            log.info("Member found with username={} ", username);
            return new ResponseEntity<>(member, HttpStatus.OK);
        } else {
            log.warn("Member with username={} not found", username);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Create a Member", description = "Creates a member when signing up")
    @PostMapping("/createnew")
    public ResponseEntity<Member> registerMember(@RequestBody MemberDTO newMemberDTO) {
        log.info("Registering new member with email={} and username={}", newMemberDTO.getEmail(), newMemberDTO.getUsername());
        Member member = memberService.registerMember(newMemberDTO);
        log.info("Member registered successfully ");
        return new ResponseEntity<>(member, HttpStatus.CREATED);
    }

    @Operation(summary = "Update a Member using MemberID", description = "Updates all the details of a member or admin")
    @PutMapping("/updateput/{id}")
    public ResponseEntity<Member> updateMember(@PathVariable Integer id, @RequestBody MemberDTO memberDTO) {
        log.info("Updating member with id={}", id);
        Member member = memberService.updateMember(id, memberDTO);
        log.info("Member updated successfully with id={}", id);
        return new ResponseEntity<>(member, HttpStatus.OK);
    }

    @Operation(summary = "Update a Member Partially using MemberID", description = "Updates only some parts of a member using the MemberID")
    @PatchMapping("/updatepatch/{id}")
    public ResponseEntity<Member> patchMember(@PathVariable Integer id, @RequestBody MemberDTO memberDTO) {
        log.info("Partially updating member with id={}", id);
        Member member = memberService.updateMember(id, memberDTO);
        log.info("Member partially updated with id={}", id);
        return new ResponseEntity<>(member, HttpStatus.OK);
    }

    @Operation(summary = "Delete a Member using the MemberID", description = "Delete a member from the database using the MemberID")
    @DeleteMapping("/deleteuser/{id}")
    public ResponseEntity<String> deleteMember(@PathVariable Integer id) {
        log.info("Deleting member with id={}", id);
        String result = memberService.deleteMember(id);
        log.info("Delete operation result for member id={}: {}", id, result);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}