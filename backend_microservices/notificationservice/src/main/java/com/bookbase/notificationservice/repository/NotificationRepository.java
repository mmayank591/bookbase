package com.bookbase.notificationservice.repository;

import com.bookbase.notificationservice.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {

    List<Notification> findByMemberID(Integer memberID);
    Notification findByTransactionID(Integer TransactionID);
    
}