package com.bookbase.backend.controller;

import com.bookbase.backend.entity.Fine;
import com.bookbase.backend.model.FineDTO;
import com.bookbase.backend.model.FineUpdateDTO;
import com.bookbase.backend.service.FineService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

class FineControllerTest {

    private MockMvc mockMvc;

    @Mock
    private FineService fineService;

    @InjectMocks
    private FineController fineController;

    private Fine sampleFine;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(fineController).build();
        objectMapper = new ObjectMapper();

        sampleFine = new Fine();
        sampleFine.setFineID(1);
        sampleFine.setMemberID(100);
        sampleFine.setAmount(BigDecimal.valueOf(50.0));
        sampleFine.setStatus("Unpaid");
        sampleFine.setTransactionDate(new Date());
        sampleFine.setTransactionId(200);
    }

    @Test
    void testGetAllFines() throws Exception {
        when(fineService.getAllFines()).thenReturn(List.of(sampleFine));

        mockMvc.perform(get("/fine/getallfines"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("Unpaid"));
    }

    @Test
    void testGetFineById() throws Exception {
        when(fineService.getFineById(1)).thenReturn(sampleFine);

        mockMvc.perform(get("/fine/getbyid/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("Unpaid"));
    }

    @Test
    void testGetByMemberId() throws Exception {
        when(fineService.getByMemberId(100)).thenReturn(List.of(sampleFine));

        mockMvc.perform(get("/fine/getbymemberid/100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].memberID").value(100));
    }

    @Test
    void testGetByStatus() throws Exception {
        when(fineService.getByStatus("Unpaid")).thenReturn(List.of(sampleFine));

        mockMvc.perform(get("/fine/getbystatus/Unpaid"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("Unpaid"));
    }

    @Test
    void testGetByTransactionId() throws Exception {
        when(fineService.getByTransactionId(200)).thenReturn(sampleFine);

        mockMvc.perform(get("/fine/getbytransactionid/200"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionId").value(200));
    }

    @Test
    void testCreateFine() throws Exception {
        FineDTO dto = new FineDTO();
        dto.setMemberId(100);
        dto.setAmount(BigDecimal.valueOf(50.0));
        dto.setStatus("Unpaid");
        dto.setTransactionDate(new Date());
        dto.setTransactionId(200);

        when(fineService.createFine(any(FineDTO.class))).thenReturn(sampleFine);

        mockMvc.perform(post("/fine/createnew")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("Unpaid"));
    }

    @Test
    void testUpdateFine() throws Exception {
        FineDTO dto = new FineDTO();
        dto.setStatus("Paid");
        dto.setAmount(BigDecimal.valueOf(50.0));
        dto.setTransactionDate(new Date());

        sampleFine.setStatus("Paid");

        when(fineService.updateFine(eq(1), any(FineDTO.class))).thenReturn(sampleFine);

        mockMvc.perform(put("/fine/updateput/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("Paid"));
    }

    @Test
    void testUpdateStatusAndTransactionDate() throws Exception {
        FineUpdateDTO updateDTO = new FineUpdateDTO();
        updateDTO.setStatus("Paid");
        updateDTO.setTransactionDate(new Date());

        sampleFine.setStatus("Paid");

        when(fineService.updateStatusAndTransactionDate(eq(1), any(FineUpdateDTO.class))).thenReturn(sampleFine);

        mockMvc.perform(patch("/fine/updatepatch/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("Paid"));
    }
}
