package com.bookbase.notificationservice.controller;

import com.bookbase.notificationservice.entity.Notification;
import com.bookbase.notificationservice.model.NotificationDTO;
import com.bookbase.notificationservice.service.NotificationService;
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
@RequestMapping("/notification")
@Tag(name = "Notification API", description = "CRUD operation for Notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Operation(summary = "Fetch all the Notifications", description = "Returns the details of all the notifications stored in the database")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEMBER')")
    @GetMapping("/getallnotifications")
    public List<Notification> getAllNotifications() {
        log.info("Fetching all notifications");
        List<Notification> notifications = notificationService.getAllNotification();
        log.info("Found {} notifications", notifications.size());
        return notifications;
    }

    @Operation(summary = "Fetch the details of a Notification by NotificationID", description = "Returns the details of a notification using the NotificationID")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEMBER')")
    @GetMapping("/getbyid/{id}")
    public ResponseEntity<Notification> getNotificationById(@PathVariable Integer id) {
        log.info("Fetching notification with id={}", id);
        Notification notification = notificationService.getById(id);
        if (notification != null) {
            log.info("Notification found with id={} ", id);
            return new ResponseEntity<>(notification, HttpStatus.OK);
        } else {
            log.warn("Notification with id={} not found", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Fetch the list of Notifications of a Member by MemberID", description = "Returns the list of all notifications of a particular member using the MemberID")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEMBER')")
    @GetMapping("/getbymemberid/{memberId}")
    public List<Notification> getNotificationsByMemberId(@PathVariable Integer memberId) {
        log.info("Fetching notifications for memberId={}", memberId);
        List<Notification> notifications = notificationService.getByMemberId(memberId);
        log.info("Found {} notifications for memberId={}", notifications.size(), memberId);
        return notifications;
    }

    @Operation(summary = "Create a Notification", description = "Creates a notification when a borrowed book gets overdue")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEMBER')")
    @PostMapping("/createnew")
    public ResponseEntity<Notification> createNotification(@RequestBody NotificationDTO newNotificationDTO) {
        log.info("Creating new notification for memberId={} with message='{}'",
                newNotificationDTO.getMemberId(), newNotificationDTO.getMessage());
        Notification notification = notificationService.createNotification(newNotificationDTO);
        log.info("Notification created successfully");
        return new ResponseEntity<>(notification, HttpStatus.CREATED);
    }

    @Operation(summary = "Delete a Notification using the NotificationID", description = "Delete a notification from the database using the NotificationID")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEMBER')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteNotification(@PathVariable Integer id) {
        log.info("Deleting notification with id={}", id);
        String result = notificationService.deleteNotification(id);
        log.info("Delete operation result for notification id={}: {}", id, result);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Operation(summary = "Delete a Notification using the TransactionID", description = "Delete a notification from the database using the TransactionID")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEMBER')")
    @DeleteMapping("/deleteByTransactionId/{transactionId}")
    public ResponseEntity<String> deleteNotificationByTransactionId(@PathVariable Integer transactionId){
        log.info("Deleting notification with TransactionId={}", transactionId);
        String result = notificationService.deleteNotificationByTransactionId(transactionId);
        log.info("Delete operation result for notification with TransactionId={}: {}", transactionId, result);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Operation(summary = "Updating a Notification using the NotificationID", description = "Updating a notification using PUT method using the NotificationID")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEMBER')")
    @PutMapping("/update/{id}")
    public ResponseEntity<Notification> updateNotificationMessage(@PathVariable Integer id, @RequestBody NotificationDTO notificationDTO){
        log.info("Updating notification with id={}", id);
        log.info("Notification updated successfully!");
        return new ResponseEntity<>(notificationService.updateNotificationMessage(id, notificationDTO), HttpStatus.OK);
    }
}