package com.bookbase.transactionservice.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BorrowingTransactionDTO {
    @JsonProperty("bookID")
    private Integer bookId;

    @JsonProperty("memberID")
    private Integer memberId;

    @JsonProperty("borrowDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Asia/Kolkata")
    private Date borrowDate;

    @JsonProperty("returnDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Asia/Kolkata")
    private Date returnDate;

    @JsonProperty("status")
    private String status;

}
