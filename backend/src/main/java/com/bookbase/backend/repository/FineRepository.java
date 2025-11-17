package com.bookbase.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.bookbase.backend.entity.Fine;
import java.util.List;


@Repository
public interface FineRepository extends JpaRepository<Fine, Integer> {

    List<Fine> findByMemberID(Integer memberID);

    List<Fine> findByStatus(String status);

    Fine findByTransactionId(Integer transactionId);
}