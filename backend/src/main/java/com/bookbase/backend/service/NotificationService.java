package com.bookbase.backend.service;

import com.bookbase.backend.entity.Notification;
import com.bookbase.backend.exception.NotificationNotFoundException;
import com.bookbase.backend.model.NotificationDTO;
import com.bookbase.backend.repository.NotificationRepository;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    public List<Notification> getAllNotification() {
        log.debug("Fetching all notifications from repository");
        List<Notification> notifications = notificationRepository.findAll();
        log.info("Fetched {} notifications", notifications.size());
        return notifications;
    }

    public Notification getById(Integer id) {
        log.debug("Fetching notification with id={}", id);
        return notificationRepository.findById(id)
                .map(notification -> {
                    log.info("Notification found with id={} and message='{}'", id, notification.getMessage());
                    return notification;
                })
                .orElseThrow(() -> {
                    log.warn("Notification not found with id={}", id);
                    return new NotificationNotFoundException("Notification not Found with ID : " + id);
                });
    }

    public List<Notification> getByMemberId(Integer id) {
        log.debug("Fetching notifications for memberId={}", id);
        List<Notification> notifications = notificationRepository.findByMemberID(id);
        log.info("Found {} notifications for memberId={}", notifications.size(), id);
        return notifications;
    }

    public Notification createNotification(NotificationDTO notificationDTO) {
        log.info("Creating new notification for memberId={} with message='{}'",
                notificationDTO.getMemberId(), notificationDTO.getMessage());

        Notification notification = new Notification();
        notification.setMemberID(notificationDTO.getMemberId());
        notification.setMessage(notificationDTO.getMessage());
        notification.setTransactionID(notificationDTO.getTransactionId());

        Notification saved = notificationRepository.save(notification);
        log.info("Notification created successfully ");
        return saved;
    }

    public String deleteNotification(Integer id) {
        log.info("Deleting notification with id={}", id);

        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Notification not found with id={} for deletion", id);
                    return new NotificationNotFoundException(
                            "Notification trying to be deleted is not Found with ID : " + id);
                });

        notificationRepository.delete(notification);
        log.info("Notification deleted successfully with id={}", id);
        return "Notification deleted";
    }

    public Notification updateNotificationMessage(Integer id, NotificationDTO notificationDTO) {
        Notification updatedNotification = notificationRepository.findById(id).get();
        updatedNotification.setTransactionID(notificationDTO.getTransactionId());
        updatedNotification.setMemberID(notificationDTO.getMemberId());
        updatedNotification.setMessage(notificationDTO.getMessage());
        return notificationRepository.save(updatedNotification);

    }

    public String deleteNotificationByTransactionId(Integer id) {
        Notification notification = notificationRepository.findByTransactionID(id);
        notificationRepository.delete(notification);
        return "Deleted notification";
    }
}