package com.bookbase.backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO {

    @JsonProperty("memberID")
    private Integer memberId;

    @JsonProperty("transactionID")
    private Integer transactionId;

    @JsonProperty("message")
    private String message;

    @Override
    public String toString() {
        return "NotificationDTO{" +
                "memberId=" + memberId +
                ", message='" + message + '\'' +
                '}';
    }
}
