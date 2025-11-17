package com.bookbase.fineservice.controller;

import com.bookbase.fineservice.entity.Fine;
import com.bookbase.fineservice.model.FineAmountDTO;
import com.bookbase.fineservice.model.FineDTO;
import com.bookbase.fineservice.model.FineUpdateDTO;
import com.bookbase.fineservice.service.FineService;
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
@RequestMapping("/fine")
@Tag(name = "Fines API", description = "Create, Read, Update Operations for Fines")
public class FineController {

    @Autowired
    private FineService fineService;

    @Operation(summary = "Fetch the details of all Fines", description = "Returns the details of all the fines stored in the database")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEMBER')")
    @GetMapping("/getallfines")
    public ResponseEntity<List<Fine>> getAllFines() {
        log.info("Fetching all fines");
        List<Fine> fines = fineService.getAllFines();
        log.info("Found {} fines", fines.size());
        return new ResponseEntity<>(fines, HttpStatus.OK);
    }

    @Operation(summary = "Fetch the details of a Fine by FineID", description = "Returns the details of a fine using the FineID")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEMBER')")
    @GetMapping("/getbyid/{id}")
    public ResponseEntity<Fine> getFineById(@PathVariable Integer id) {
        log.info("Fetching fine with id={}", id);
        Fine fine = fineService.getFineById(id);
        if (fine != null) {
            log.info("Fine found with id={} and status={}", id, fine.getStatus());
            return new ResponseEntity<>(fine, HttpStatus.OK);
        } else {
            log.warn("Fine with id={} not found", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Fetch the Fines for a Member using MemberID", description = "Returns the list of all the fines of a member using MemberID")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEMBER')")
    @GetMapping("/getbymemberid/{id}")
    public ResponseEntity<List<Fine>> getByMemberId(@PathVariable Integer id) {
        log.info("Fetching fines for member id={}", id);
        List<Fine> fines = fineService.getByMemberId(id);
        log.info("Found {} fines for member id={}", fines.size(), id);
        return new ResponseEntity<>(fines, HttpStatus.OK);
    }

    @Operation(summary = "Fetch the list of Fines by Status", description = "Returns a list of fines by the status 'Due' or 'Paid'")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEMBER')")
    @GetMapping("/getbystatus/{status}")
    public ResponseEntity<List<Fine>> getByStatus(@PathVariable String status) {
        log.info("Fetching fines with status={}", status);
        List<Fine> fines = fineService.getByStatus(status);
        log.info("Found {} fines with status={}", fines.size(), status);
        return new ResponseEntity<>(fines, HttpStatus.OK);
    }

    @Operation(summary = "Fetch a Fine using TransactionID", description = "Returns the fine associated with a transaction using the TransactionID")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEMBER')")
    @GetMapping("/getbytransactionid/{id}")
    public ResponseEntity<Fine> getByTransactionId(@PathVariable Integer id) {
        log.info("Fetching fine for transaction id={}", id);
        Fine fine = fineService.getByTransactionId(id);
        if (fine != null) {
            log.info("Fine found for transaction id={} with status={}", id, fine.getStatus());
            return new ResponseEntity<>(fine, HttpStatus.OK);
        } else {
            log.warn("No fine found for transaction id={}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Create a Fine", description = "Creates a fine if a borrowed book gets overdue")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEMBER')")
    @PostMapping("/createnew")
    public ResponseEntity<Fine> createFine(@RequestBody FineDTO newFineDTO) {
        log.info("Creating new fine for memberId={} and transactionId={}", newFineDTO.getMemberId(),
                newFineDTO.getTransactionId());
        Fine fine = fineService.createFine(newFineDTO);
        log.info("Fine created successfully with   status={}", fine.getStatus());
        return new ResponseEntity<>(fine, HttpStatus.CREATED);
    }

    @Operation(summary = "Update a Fine using FineID", description = "Updates all the details of a fine")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEMBER')")
    @PutMapping("/updateput/{id}")
    public ResponseEntity<Fine> updateFine(@PathVariable Integer id, @RequestBody FineDTO fineDetailsDTO) {
        log.info("Updating fine with id={}", id);
        Fine updated = fineService.updateFine(id, fineDetailsDTO);
        log.info("Fine updated successfully with  status={}", updated.getStatus());
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @Operation(summary = "Update the Status and Transaction Date of a Fine", description = "Updates the status and transaction date of a fine when it is paid")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEMBER')")
    @PatchMapping("/updatepatch/{id}")
    public ResponseEntity<Fine> updateStatusAndTransactionDate(@PathVariable Integer id,
            @RequestBody FineUpdateDTO fineUpdateDTO) {
        log.info("Updating fine id={} with new status={} and transactionDate={}", id, fineUpdateDTO.getStatus(),
                fineUpdateDTO.getTransactionDate());
        Fine updated = fineService.updateStatusAndTransactionDate(id, fineUpdateDTO);
        log.info("Fine id={} updated successfully to status={}", id, updated.getStatus());
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @Operation(summary = "Update the Amount of a Fine", description = "Updates the fine amount of a fine until it is paid")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEMBER')")
    @PatchMapping("/updateamount/{id}")
    public ResponseEntity<Fine> updateAmount(@PathVariable Integer id, @RequestBody FineAmountDTO fineAmountDTO){
        return new ResponseEntity<>(fineService.updateAmount(id, fineAmountDTO), HttpStatus.OK);
    }

}