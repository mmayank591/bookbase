package com.bookbase.transactionservice.service;

import com.bookbase.transactionservice.entity.BorrowingTransaction;
import com.bookbase.transactionservice.exception.TransactionNotFoundException;
import com.bookbase.transactionservice.model.BorrowingTransactionDTO;
import com.bookbase.transactionservice.repository.BorrowingTransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

class BorrowingTransactionServiceTest {

    @Mock
    private BorrowingTransactionRepository borrowingTransactionRepository;

    @InjectMocks
    private BorrowingTransactionService borrowingTransactionService;

    private BorrowingTransaction sampleTransaction;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sampleTransaction = new BorrowingTransaction();
        sampleTransaction.setTransactionID(1);
        sampleTransaction.setBookID(101);
        sampleTransaction.setMemberID(202);
        sampleTransaction.setStatus("Borrowed");
        sampleTransaction.setBorrowDate(new Date());
        sampleTransaction.setReturnDate(null);
    }

    @Test
    void testGetAllTransaction() {
        when(borrowingTransactionRepository.findAll()).thenReturn(List.of(sampleTransaction));
        List<BorrowingTransaction> transactions = borrowingTransactionService.getAllTransaction();
        assertEquals(1, transactions.size());
        assertEquals(101, transactions.get(0).getBookID());
    }

    @Test
    void testGetByTransactionId_Found() {
        when(borrowingTransactionRepository.findById(1)).thenReturn(Optional.of(sampleTransaction));
        BorrowingTransaction transaction = borrowingTransactionService.getByTransactionId(1);
        assertEquals("Borrowed", transaction.getStatus());
    }

    @Test
    void testGetByTransactionId_NotFound() {
        when(borrowingTransactionRepository.findById(2)).thenReturn(Optional.empty());
        assertThrows(TransactionNotFoundException.class, () -> borrowingTransactionService.getByTransactionId(2));
    }

    @Test
    void testGetByMemberId() {
        when(borrowingTransactionRepository.findByMemberID(202)).thenReturn(List.of(sampleTransaction));
        List<BorrowingTransaction> transactions = borrowingTransactionService.getByMemberId(202);
        assertEquals(1, transactions.size());
        assertEquals(202, transactions.get(0).getMemberID());
    }

    @Test
    void testCreateTransaction() {
        BorrowingTransactionDTO dto = new BorrowingTransactionDTO();
        dto.setBookId(101);
        dto.setMemberId(202);
        dto.setStatus("Borrowed");
        dto.setBorrowDate(new Date());
        dto.setReturnDate(null);

        when(borrowingTransactionRepository.save(any(BorrowingTransaction.class))).thenReturn(sampleTransaction);
        BorrowingTransaction created = borrowingTransactionService.createTransaction(dto);
        assertEquals("Borrowed", created.getStatus());
    }

    @Test
    void testUpdateTransactionStatus_Returned() {
        when(borrowingTransactionRepository.findById(1)).thenReturn(Optional.of(sampleTransaction));
        when(borrowingTransactionRepository.save(any(BorrowingTransaction.class))).thenReturn(sampleTransaction);

        ResponseEntity<BorrowingTransaction> response = borrowingTransactionService.updateTransactionStatus(1, "Returned");
        assertEquals("Returned", response.getBody().getStatus());
        assertNotNull(response.getBody().getReturnDate());
    }

    @Test
    void testUpdateTransactionStatus_NotFound() {
        when(borrowingTransactionRepository.findById(2)).thenReturn(Optional.empty());
        assertThrows(TransactionNotFoundException.class, () -> borrowingTransactionService.updateTransactionStatus(2, "Returned"));
    }
}
