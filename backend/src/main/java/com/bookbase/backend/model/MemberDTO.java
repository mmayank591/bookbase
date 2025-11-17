package com.bookbase.backend.model;
 
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
 
public class MemberDTO {
 
    @JsonProperty("name")
    private String name;
 
    @JsonProperty("email")
    private String email;
 
    @JsonProperty("userName")
    private String username;
 
    @JsonProperty("password")
    private String password;
 
    @JsonProperty("role")
    private String role;

    @JsonProperty("membershipStatus")
    private String membershipStatus;
 
    @Override
    public String toString() {
        return "MemberDTO{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", membershipStatus='" + membershipStatus + '\'' +
                '}';
    }
}