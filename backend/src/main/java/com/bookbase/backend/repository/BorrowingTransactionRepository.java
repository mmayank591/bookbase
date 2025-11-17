package com.bookbase.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.bookbase.backend.entity.BorrowingTransaction;
import java.util.List;


@Repository
public interface BorrowingTransactionRepository extends JpaRepository<BorrowingTransaction, Integer> {

    List<BorrowingTransaction> findByStatus(String status);

    List<BorrowingTransaction> findByMemberID(Integer memberID);

    List<BorrowingTransaction> findByBookID(Integer bookID);
}