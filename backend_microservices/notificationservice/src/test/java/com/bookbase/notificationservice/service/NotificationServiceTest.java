package com.bookbase.notificationservice.service;

import com.bookbase.notificationservice.entity.Notification;
import com.bookbase.notificationservice.exception.NotificationNotFoundException;
import com.bookbase.notificationservice.model.NotificationDTO;
import com.bookbase.notificationservice.repository.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationService notificationService;

    private Notification sampleNotification;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sampleNotification = new Notification();
        sampleNotification.setNotificationID(1);
        sampleNotification.setMemberID(100);
        sampleNotification.setMessage("Test Message");
        sampleNotification.setTransactionID(200);
    }

    @Test
    void testGetAllNotification() {
        when(notificationRepository.findAll()).thenReturn(List.of(sampleNotification));
        List<Notification> notifications = notificationService.getAllNotification();
        assertEquals(1, notifications.size());
        assertEquals("Test Message", notifications.get(0).getMessage());
    }

    @Test
    void testGetById_Found() {
        when(notificationRepository.findById(1)).thenReturn(Optional.of(sampleNotification));
        Notification notification = notificationService.getById(1);
        assertEquals(100, notification.getMemberID());
    }

    @Test
    void testGetById_NotFound() {
        when(notificationRepository.findById(2)).thenReturn(Optional.empty());
        assertThrows(NotificationNotFoundException.class, () -> notificationService.getById(2));
    }

    @Test
    void testGetByMemberId() {
        when(notificationRepository.findByMemberID(100)).thenReturn(List.of(sampleNotification));
        List<Notification> notifications = notificationService.getByMemberId(100);
        assertEquals(1, notifications.size());
        assertEquals(100, notifications.get(0).getMemberID());
    }

    @Test
    void testCreateNotification() {
        NotificationDTO dto = new NotificationDTO();
        dto.setMemberId(100);
        dto.setMessage("Test Message");
        dto.setTransactionId(200);

        when(notificationRepository.save(any(Notification.class))).thenReturn(sampleNotification);
        Notification created = notificationService.createNotification(dto);
        assertEquals("Test Message", created.getMessage());
    }

    @Test
    void testDeleteNotification_Found() {
        when(notificationRepository.findById(1)).thenReturn(Optional.of(sampleNotification));
        doNothing().when(notificationRepository).delete(sampleNotification);
        String result = notificationService.deleteNotification(1);
        assertEquals("Notification deleted", result);
    }

    @Test
    void testDeleteNotification_NotFound() {
        when(notificationRepository.findById(2)).thenReturn(Optional.empty());
        assertThrows(NotificationNotFoundException.class, () -> notificationService.deleteNotification(2));
    }


}
