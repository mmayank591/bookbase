package com.bookbase.backend.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FineDTO {

    @JsonProperty("memberID")
    private Integer memberId;

    @JsonProperty("amount")
    private BigDecimal amount;

    @JsonProperty("status")
    private String status;

    @JsonProperty("transactionID")
    private Integer transactionId;

    @JsonProperty("transactionDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Asia/Kolkata")
    private Date transactionDate;

    @Override
    public String toString() {
        return "FineDTO{" +
                "memberId=" + memberId +
                ", amount=" + amount +
                ", status='" + status + '\'' +
                ", transactionId=" + transactionId +
                ", transactionDate=" + transactionDate +
                '}';
    }
}
