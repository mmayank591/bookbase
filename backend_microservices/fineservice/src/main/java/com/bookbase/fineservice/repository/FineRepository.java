package com.bookbase.fineservice.repository;

import com.bookbase.fineservice.entity.Fine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FineRepository extends JpaRepository<Fine, Integer> {

    List<Fine> findByMemberID(Integer memberID);
    List<Fine> findByStatus(String status);
    Fine findByTransactionId(Integer transactionId);
    
}