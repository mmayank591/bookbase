package com.bookbase.backend.entity;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "Member")
public class Member implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MemberID")
    private Integer memberID;

    @Column(name = "Name", nullable = false)
    private String name;

    @Column(name = "Email", unique = true, nullable = false)
    private String email;

    @Column(name = "MembershipStatus")
    private String membershipStatus;

    @Column(name = "UserName", unique = true, nullable = false)
    private String userName;

    @Column(name = "Password", nullable = false)
    private String password;

    @Column(name = "Role")
    private String role;

    public Member() {
    }

    public Integer getMemberID() {
        return memberID;
    }

    public void setMemberID(Integer memberID) {
        this.memberID = memberID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMembershipStatus() {
        return membershipStatus;
    }

    public void setMembershipStatus(String membershipStatus) {
        this.membershipStatus = membershipStatus;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
