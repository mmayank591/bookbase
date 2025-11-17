package com.bookbase.notificationservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Notification")
public class Notification implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NotificationID")
    private Integer notificationID;

    @Column(name = "MemberID", nullable = false)
    private Integer memberID;

    @Column(name = "TransactionID", nullable = false)
    private Integer transactionID;

    @Column(name = "Message", nullable = false)
    private String message;

}