package com.bookbase.transactionservice.controller;

import com.bookbase.transactionservice.entity.BorrowingTransaction;
import com.bookbase.transactionservice.model.BookStatusUpdateDTO;
import com.bookbase.transactionservice.model.BorrowingTransactionDTO;
import com.bookbase.transactionservice.service.BorrowingTransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;   
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j   
@RestController
@RequestMapping("/transaction")
@Tag(name = "Transactions API", description = "Create, Read, Update Operations for Transactions")
public class BorrowingTransactionController {

    @Autowired
    private BorrowingTransactionService borrowingTransactionService;

    @Operation(summary = "Fetch the details of all Transactions", description = "Returns the details of all the transactions stored in the database")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEMBER')")
    @GetMapping("/getalltransactions")
    public ResponseEntity<List<BorrowingTransaction>> getAllTransactions() {
        log.info("Fetching all borrowing transactions");
        List<BorrowingTransaction> transactions = borrowingTransactionService.getAllTransaction();
        log.info("Found {} transactions", transactions.size());
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @Operation(summary = "Fetch the details of a Transaction by TransactionID", description = "Returns the details of all the transactions of a particular transaction using the TransactionID")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEMBER')")
    @GetMapping("/getbyid/{id}")
    public ResponseEntity<BorrowingTransaction> getTransactionById(@PathVariable Integer id) {
        log.info("Fetching transaction with id={}", id);
        BorrowingTransaction transaction = borrowingTransactionService.getByTransactionId(id);
        if (transaction != null) {
            log.info("Transaction found with id={} and status={}", id, transaction.getStatus());
            return new ResponseEntity<>(transaction, HttpStatus.OK);
        } else {
            log.warn("Transaction with id={} not found", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Fetch the list of details of all Transactions of a Member by MemberID", description = "Returns the details of all the transactions made by a member using MemberID")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEMBER')")
    @GetMapping("/getbymemberid/{id}")
    public ResponseEntity<List<BorrowingTransaction>> getByMemberId(@PathVariable Integer id) {
        log.info("Fetching transactions for member id={}", id);
        List<BorrowingTransaction> transactions = borrowingTransactionService.getByMemberId(id);
        log.info("Found {} transactions for member id={}", transactions.size(), id);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @Operation(summary = "Create a new Transaction", description = "Creates a new transaction when a user borrows a book")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEMBER')")
    @PostMapping("/createnew")
    public ResponseEntity<BorrowingTransaction> createTransaction(@RequestBody BorrowingTransactionDTO newTransaction) {
        log.info("Creating new transaction for memberId={} and bookId={}", 
                 newTransaction.getMemberId(), newTransaction.getBookId());
        BorrowingTransaction created = borrowingTransactionService.createTransaction(newTransaction);
        log.info("Transaction created successfully with status={}", created.getStatus());
        return new ResponseEntity<>(created, HttpStatus.OK);
    }

    @Operation(summary = "Update the Status of a Transaction", description = "Updates the status of a transaction to 'Returned' or 'Overdue'")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEMBER')")
    @PatchMapping("/updatestatus/{id}")
    public ResponseEntity<BorrowingTransaction> updateTransactionStatus(@PathVariable Integer id, @RequestBody BookStatusUpdateDTO bookStatusDTO) {
        log.info("Updating transaction id={} to status={}", id, bookStatusDTO.getStatus());
        ResponseEntity<BorrowingTransaction> response = borrowingTransactionService.updateTransactionStatus(id, bookStatusDTO.getStatus());
        log.info("Transaction id={} updated to status={}", id, response.getBody() != null ? response.getBody().getStatus() : "UNKNOWN");
        return response;
    }
}