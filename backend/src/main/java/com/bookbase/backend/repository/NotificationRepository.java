package com.bookbase.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.bookbase.backend.entity.Notification;
import java.util.List;


@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {

    List<Notification> findByMemberID(Integer memberID);

    Notification findByTransactionID(Integer TransactionID);
}