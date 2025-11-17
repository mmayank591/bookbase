package com.bookbase.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Fine")
public class Fine implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FineID")
    private Integer fineID;

    @Column(name = "MemberID", nullable = false)
    private Integer memberID;

    @Column(name = "Amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal amount;

    @Column(name = "Status", nullable = false)
    private String status;

    @Column(name = "TransactionID", nullable = false)
    private Integer transactionId;

    @Column(name = "TransactionDate")
    @Temporal(TemporalType.DATE)
    private Date transactionDate;

    @Override
    public String toString() {
        return "Fine{" +
                "fineID=" + fineID +
                ", memberID=" + memberID +
                ", amount=" + amount +
                ", status='" + status + '\'' +
                ", transactionId=" + transactionId +
                ", transactionDate=" + transactionDate +
                '}';
    }

}