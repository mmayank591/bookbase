package com.bookbase.fineservice.service;

import com.bookbase.fineservice.entity.Fine;
import com.bookbase.fineservice.exception.FineNotFoundException;
import com.bookbase.fineservice.model.FineAmountDTO;
import com.bookbase.fineservice.model.FineDTO;
import com.bookbase.fineservice.model.FineUpdateDTO;
import com.bookbase.fineservice.repository.FineRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@Service
public class FineService {

    @Autowired
    private FineRepository fineRepository;

    public List<Fine> getAllFines() {
        log.debug("Fetching all fines from repository");
        List<Fine> fines = fineRepository.findAll();
        log.info("Fetched {} fines", fines.size());
        return fines;
    }

    public Fine getFineById(Integer id) {
        log.debug("Fetching fine with id={}", id);
        return fineRepository.findById(id)
                .map(fine -> {
                    log.info("Fine found with id={} and status={}", id, fine.getStatus());
                    return fine;
                })
                .orElseThrow(() -> {
                    log.warn("Fine not found with id={}", id);
                    return new FineNotFoundException("Fine not Found with ID : " + id);
                });
    }

    public Fine createFine(FineDTO fineDTO) {
        log.info("Creating new fine for memberId={} and transactionId={} with amount={} and status={}",
                fineDTO.getMemberId(), fineDTO.getTransactionId(), fineDTO.getAmount(), fineDTO.getStatus());

        Fine fine = new Fine();
        fine.setMemberID(fineDTO.getMemberId());
        fine.setAmount(fineDTO.getAmount());
        fine.setStatus(fineDTO.getStatus());
        fine.setTransactionDate(fineDTO.getTransactionDate());
        fine.setTransactionId(fineDTO.getTransactionId());

        Fine saved = fineRepository.save(fine);
        log.info("Fine created successfully  for memberId={}", saved.getMemberID());
        return saved;
    }

    public List<Fine> getByMemberId(Integer id) {
        log.debug("Fetching fines for memberId={}", id);
        List<Fine> fines = fineRepository.findByMemberID(id);
        log.info("Found {} fines for memberId={}", fines.size(), id);
        return fines;
    }

    public List<Fine> getByStatus(String status) {
        log.debug("Fetching fines with status={}", status);
        List<Fine> fines = fineRepository.findByStatus(status);
        log.info("Found {} fines with status={}", fines.size(), status);
        return fines;
    }

    public Fine updateFine(Integer id, FineDTO fineDTO) {
        log.info("Updating fine with id={}", id);
        Fine fine = fineRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Fine not found with id={} for update", id);
                    return new FineNotFoundException("Fine not Found with ID : " + id);
                });

        fine.setStatus(fineDTO.getStatus());
        fine.setAmount(fineDTO.getAmount());
        fine.setTransactionDate(fineDTO.getTransactionDate());

        Fine updated = fineRepository.save(fine);
        log.info("Fine updated successfully and new status={}", updated.getStatus());
        return updated;
    }

    public Fine updateStatusAndTransactionDate(Integer id, FineUpdateDTO fineUpdateDTO) {
        log.info("Updating fine id={} with new status={} and transactionDate={}", id, fineUpdateDTO.getStatus(),
                fineUpdateDTO.getTransactionDate());
        Fine fine = fineRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Fine not found with id={} for status update", id);
                    return new FineNotFoundException("Fine not Found with ID : " + id);
                });

        fine.setTransactionDate(fineUpdateDTO.getTransactionDate());
        fine.setStatus(fineUpdateDTO.getStatus());

        Fine updated = fineRepository.save(fine);
        log.info("updated successfully to status={}", updated.getStatus());
        return updated;
    }

    public Fine getByTransactionId(Integer id) {
        log.debug("Fetching fine by transactionId={}", id);
        Fine fine = fineRepository.findByTransactionId(id);
        if (fine != null) {
            log.info("Fine found for transactionId={}  and status={}", id, fine.getStatus());
        } else {
            log.warn("No fine found for transactionId={}", id);
        }
        return fine;
    }

    public Fine updateAmount(Integer id, FineAmountDTO fineAmountDTO) {
        Fine fine = fineRepository.findById(id).get();
        fine.setAmount(fineAmountDTO.getAmount());
        return fineRepository.save(fine);
    }
}