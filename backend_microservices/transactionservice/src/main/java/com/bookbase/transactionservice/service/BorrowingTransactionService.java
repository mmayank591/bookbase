package com.bookbase.transactionservice.service;

import com.bookbase.transactionservice.entity.BorrowingTransaction;
import com.bookbase.transactionservice.exception.TransactionNotFoundException;
import com.bookbase.transactionservice.model.BorrowingTransactionDTO;
import com.bookbase.transactionservice.repository.BorrowingTransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@Service
public class BorrowingTransactionService {

    @Autowired
    private BorrowingTransactionRepository borrowingTransactionRepository;

    public List<BorrowingTransaction> getAllTransaction() {
        log.debug("Fetching all borrowing transactions from repository");
        List<BorrowingTransaction> transactions = borrowingTransactionRepository.findAll();
        log.info("Fetched {} transactions", transactions.size());
        return transactions;
    }

    public BorrowingTransaction getByTransactionId(Integer id) {
        log.debug("Fetching transaction with id={}", id);
        return borrowingTransactionRepository.findById(id)
                .map(transaction -> {
                    log.info("Transaction found with id={} and status={}", id, transaction.getStatus());
                    return transaction;
                })
                .orElseThrow(() -> {
                    log.warn("Transaction not found with id={}", id);
                    return new TransactionNotFoundException("Transaction not found with id: " + id);
                });
    }

    public List<BorrowingTransaction> getByMemberId(Integer id) {
        log.debug("Fetching transactions for memberId={}", id);
        List<BorrowingTransaction> transactions = borrowingTransactionRepository.findByMemberID(id);
        log.info("Found {} transactions for memberId={}", transactions.size(), id);
        return transactions;
    }

    public BorrowingTransaction createTransaction(BorrowingTransactionDTO borrowingTransactionDTO) {
        log.info("Creating new transaction for memberId={} and bookId={} with status={}",
                borrowingTransactionDTO.getMemberId(), borrowingTransactionDTO.getBookId(),
                borrowingTransactionDTO.getStatus());

        BorrowingTransaction transaction = new BorrowingTransaction();
        transaction.setBookID(borrowingTransactionDTO.getBookId());
        transaction.setMemberID(borrowingTransactionDTO.getMemberId());
        transaction.setStatus(borrowingTransactionDTO.getStatus());
        transaction.setBorrowDate(borrowingTransactionDTO.getBorrowDate());
        transaction.setReturnDate(borrowingTransactionDTO.getReturnDate());

        BorrowingTransaction saved = borrowingTransactionRepository.save(transaction);
        log.info("Transaction created successfully and status={}", saved.getStatus());
        return saved;
    }

    public ResponseEntity<BorrowingTransaction> updateTransactionStatus(Integer id, String status) {
        log.info("Updating transaction id={} to status={}", id, status);

        BorrowingTransaction transaction = borrowingTransactionRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Transaction not found with id={} for status update", id);
                    return new TransactionNotFoundException("Transaction not found with id: " + id);
                });

        transaction.setStatus(status);

        if ("Returned".equals(status)) {
            transaction.setReturnDate(new java.util.Date());
            log.debug("Transaction id={} marked as Returned, returnDate set to current date", id);
        }

        final BorrowingTransaction updatedTransaction = borrowingTransactionRepository.save(transaction);
        log.info("Transaction id={} updated successfully to status={}", id, updatedTransaction.getStatus());
        return ResponseEntity.ok(updatedTransaction);
    }
}