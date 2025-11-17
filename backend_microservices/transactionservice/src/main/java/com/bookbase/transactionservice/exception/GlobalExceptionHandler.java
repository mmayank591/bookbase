package com.bookbase.transactionservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<Map<String, String>> bookNotFoundException(BookNotFoundException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("Book Exception", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TransactionNotFoundException.class)
    public ResponseEntity<Map<String, String>> transactionNotFoundException(TransactionNotFoundException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("Transaction Exception", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<Map<String,String>> memberNotFoundException(MemberNotFoundException ex){
        Map<String, String> response = new HashMap<>();
        response.put("Member Exception",ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<String> handleUsernameAlreadyExists(UsernameAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<String> handleEmailAlreadyExists(EmailAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(FineNotFoundException.class)
    public ResponseEntity<Map<String,String>> fineNotFoundException(FineNotFoundException ex){
        Map<String,String> response = new HashMap<>();
        response.put("Fine Exception",ex.getMessage());
        return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NotificationNotFoundException.class)
    public ResponseEntity<Map<String,String>> notificationNotFoundException(NotificationNotFoundException ex){
        Map<String,String> response = new HashMap<>();
        response.put("Notification Exception",ex.getMessage());
        return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> globalExceptionHandler(Exception ex) {
        Map<String, String> response = new HashMap<>();
        response.put("Uncaught Exception", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}