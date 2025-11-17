package com.bookbase.fineservice.service;

import com.bookbase.fineservice.entity.Fine;
import com.bookbase.fineservice.exception.FineNotFoundException;
import com.bookbase.fineservice.model.FineDTO;
import com.bookbase.fineservice.model.FineUpdateDTO;
import com.bookbase.fineservice.repository.FineRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

class FineServiceTest {

    @Mock
    private FineRepository fineRepository;

    @InjectMocks
    private FineService fineService;

    private Fine sampleFine;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sampleFine = new Fine();
        sampleFine.setFineID(1);
        sampleFine.setMemberID(100);
        sampleFine.setAmount(BigDecimal.valueOf(50));
        sampleFine.setStatus("Due");
        sampleFine.setTransactionDate(new Date());
        sampleFine.setTransactionId(200);
    }

    @Test
    void testGetAllFines() {
        when(fineRepository.findAll()).thenReturn(List.of(sampleFine));
        List<Fine> fines = fineService.getAllFines();
        assertEquals(1, fines.size());
        assertEquals(100, fines.get(0).getMemberID());
    }

    @Test
    void testGetFineById_Found() {
        when(fineRepository.findById(1)).thenReturn(Optional.of(sampleFine));
        Fine fine = fineService.getFineById(1);
        assertEquals("Due", fine.getStatus());
    }

    @Test
    void testGetFineById_NotFound() {
        when(fineRepository.findById(2)).thenReturn(Optional.empty());
        assertThrows(FineNotFoundException.class, () -> fineService.getFineById(2));
    }

    @Test
    void testCreateFine() {
        FineDTO dto = new FineDTO();
        dto.setMemberId(100);
        dto.setAmount(BigDecimal.valueOf(50.0));
        dto.setStatus("Due");
        dto.setTransactionDate(new Date());
        dto.setTransactionId(200);

        when(fineRepository.save(any(Fine.class))).thenReturn(sampleFine);
        Fine created = fineService.createFine(dto);
        assertEquals("Due", created.getStatus());
    }

    @Test
    void testGetByMemberId() {
        when(fineRepository.findByMemberID(100)).thenReturn(List.of(sampleFine));
        List<Fine> fines = fineService.getByMemberId(100);
        assertEquals(1, fines.size());
        assertEquals(100, fines.get(0).getMemberID());
    }

    @Test
    void testGetByStatus() {
        when(fineRepository.findByStatus("Due")).thenReturn(List.of(sampleFine));
        List<Fine> fines = fineService.getByStatus("Due");
        assertEquals(1, fines.size());
        assertEquals("Due", fines.get(0).getStatus());
    }

    @Test
    void testUpdateFine_Found() {
        when(fineRepository.findById(1)).thenReturn(Optional.of(sampleFine));
        when(fineRepository.save(any(Fine.class))).thenReturn(sampleFine);

        FineDTO dto = new FineDTO();
        dto.setStatus("Paid");
        dto.setAmount(BigDecimal.valueOf(50.0));
        dto.setTransactionDate(new Date());

        Fine updated = fineService.updateFine(1, dto);
        assertEquals("Paid", updated.getStatus());
    }

    @Test
    void testUpdateFine_NotFound() {
        when(fineRepository.findById(2)).thenReturn(Optional.empty());
        FineDTO dto = new FineDTO();
        assertThrows(FineNotFoundException.class, () -> fineService.updateFine(2, dto));
    }

    @Test
    void testUpdateStatusAndTransactionDate_Found() {
        when(fineRepository.findById(1)).thenReturn(Optional.of(sampleFine));
        when(fineRepository.save(any(Fine.class))).thenReturn(sampleFine);

        FineUpdateDTO updateDTO = new FineUpdateDTO();
        updateDTO.setStatus("Paid");
        updateDTO.setTransactionDate(new Date());

        Fine updated = fineService.updateStatusAndTransactionDate(1, updateDTO);
        assertEquals("Paid", updated.getStatus());
    }

    @Test
    void testUpdateStatusAndTransactionDate_NotFound() {
        when(fineRepository.findById(2)).thenReturn(Optional.empty());
        FineUpdateDTO updateDTO = new FineUpdateDTO();
        assertThrows(FineNotFoundException.class, () -> fineService.updateStatusAndTransactionDate(2, updateDTO));
    }

    @Test
    void testGetByTransactionId() {
        when(fineRepository.findByTransactionId(200)).thenReturn(sampleFine);
        Fine fine = fineService.getByTransactionId(200);
        assertEquals(200, fine.getTransactionId());
    }
}
