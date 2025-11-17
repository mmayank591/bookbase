package com.bookbase.transactionservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class FineNotFoundException extends RuntimeException{
    public FineNotFoundException(String message){
        super(message);
    }
}