package com.bookbase.backend.controller;

import com.bookbase.backend.entity.BorrowingTransaction;
import com.bookbase.backend.model.BookStatusUpdateDTO;
import com.bookbase.backend.model.BorrowingTransactionDTO;
import com.bookbase.backend.service.BorrowingTransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

class BorrowingTransactionControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BorrowingTransactionService borrowingTransactionService;

    @InjectMocks
    private BorrowingTransactionController borrowingTransactionController;

    private BorrowingTransaction sampleTransaction;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(borrowingTransactionController).build();
        objectMapper = new ObjectMapper();

        sampleTransaction = new BorrowingTransaction();
        sampleTransaction.setTransactionID(1);
        sampleTransaction.setBookID(101);
        sampleTransaction.setMemberID(202);
        sampleTransaction.setStatus("Borrowed");
        sampleTransaction.setBorrowDate(new Date());
        sampleTransaction.setReturnDate(null);
    }

    @Test
    void testGetAllTransactions() throws Exception {
        when(borrowingTransactionService.getAllTransaction()).thenReturn(List.of(sampleTransaction));

        mockMvc.perform(get("/transaction/getalltransactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].bookID").value(101));
    }

    @Test
    void testGetTransactionById() throws Exception {
        when(borrowingTransactionService.getByTransactionId(1)).thenReturn(sampleTransaction);

        mockMvc.perform(get("/transaction/getbyid/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("Borrowed"));
    }

    @Test
    void testGetByMemberId() throws Exception {
        when(borrowingTransactionService.getByMemberId(202)).thenReturn(List.of(sampleTransaction));

        mockMvc.perform(get("/transaction/getbymemberid/202"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].memberID").value(202));
    }

    @Test
    void testCreateTransaction() throws Exception {
        BorrowingTransactionDTO dto = new BorrowingTransactionDTO();
        dto.setBookId(101);
        dto.setMemberId(202);
        dto.setStatus("Borrowed");
        dto.setBorrowDate(new Date());
        dto.setReturnDate(null);

        when(borrowingTransactionService.createTransaction(any(BorrowingTransactionDTO.class))).thenReturn(sampleTransaction);

        mockMvc.perform(post("/transaction/createnew")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("Borrowed"));
    }

    @Test
    void testUpdateTransactionStatus() throws Exception {
        BookStatusUpdateDTO dto = new BookStatusUpdateDTO();
        dto.setStatus("Returned");
        sampleTransaction.setStatus("Returned");

        when(borrowingTransactionService.updateTransactionStatus(eq(1), anyString())).thenReturn(ResponseEntity.ok(sampleTransaction));

        mockMvc.perform(patch("/transaction/updatestatus/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("Returned"));
    }
}
