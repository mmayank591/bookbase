package com.bookbase.memberservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.io.Serializable;

@Entity
@AllArgsConstructor
@Data
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

}
