package com.bookbase.transactionservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "BorrowingTransaction")
public class BorrowingTransaction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TransactionID")
    private Integer transactionID;

    @Column(name = "BookID", nullable = false)
    private Integer bookID;

    @Column(name = "MemberID", nullable = false)
    private Integer memberID;

    @Column(name = "BorrowDate", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date borrowDate;

    @Column(name = "ReturnDate")
    @Temporal(TemporalType.DATE)
    private Date returnDate;

    @Column(name = "Status", nullable = false)
    private String status;

}