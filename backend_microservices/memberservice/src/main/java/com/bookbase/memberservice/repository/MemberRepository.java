package com.bookbase.memberservice.repository;

import com.bookbase.memberservice.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {

    Member findByEmail(String email);
    List<Member> findByMembershipStatus(String status);
    Member findByUserName(String userName);
    Member findByUserNameAndPassword(String userName, String password);
    boolean existsByUserName(String userName);
    boolean existsByEmail(String email);

}