package com.bookbase.bookservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookAvailableUpdateDTO {

    @JsonProperty("availableCopies")
    private Integer avail;

}