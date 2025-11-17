package com.bookbase.fineservice.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FineUpdateDTO {

    @JsonProperty("status")
    private String status;

    @JsonProperty("transactionDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Asia/Kolkata")
    private Date transactionDate;

    @Override
    public String toString() {
        return "FineUpdateDTO{" +
                "status='" + status + '\'' +
                ", transactionDate=" + transactionDate +
                '}';
    }
}
