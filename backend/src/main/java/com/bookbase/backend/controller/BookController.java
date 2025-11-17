package com.bookbase.backend.controller;

import com.bookbase.backend.entity.Book;
import com.bookbase.backend.model.BookAvailableUpdateDTO;
import com.bookbase.backend.model.BookDTO;
import com.bookbase.backend.service.BookService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.extern.slf4j.Slf4j;   

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j   
@RestController
@RequestMapping("/book")
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "Books API", description = "CRUD operation for Books")
public class BookController {

    @Autowired
    private BookService bookService;

    @Operation(summary = "Fetch the details of all the books", description = "Returns the details of all the books stored in the database")
    @GetMapping("/getallbooks")
    public List<Book> getAllBooks() {
        log.info("Fetching all books");
        List<Book> books = bookService.getAllBooks();
        log.info("Found {} books", books.size());
        return books;
    }

    @Operation(summary = "Fetch the details of a book by BookID", description = "Returns the details of all the transactions of a particular book using the BookID")
    @GetMapping("/getbyid/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Integer id) {
        log.info("Fetching book with id={}", id);
        Book book = bookService.getBookById(id);
        if (book != null) {
            log.info("Book found with id={} and title={}", id, book.getTitle());
            return new ResponseEntity<>(book, HttpStatus.OK);
        } else {
            log.warn("Book with id={} not found", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Register a new Book", description = "Creates a new book when an admin creates a book")
    @PostMapping("/createnew")
    public ResponseEntity<Book> addBook(@RequestBody BookDTO newBookDTO){
        log.info("Creating new book with title={}", newBookDTO.getTitle());
        Book book = bookService.createBook(newBookDTO);
        log.info("Book created successfully ");
        return new ResponseEntity<>(book, HttpStatus.CREATED);
    }

    @Operation(summary = "Update all the details of a particular Book", description = "Updates all the details of a book")
    @PutMapping("/updateput/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Integer id, @RequestBody Book book){
        log.info("Updating book with id={}", id);
        Book updated = bookService.updateBook(id, book);
        if (updated != null) {
            log.info("Book updated successfully with id={}", id);
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } else {
            log.warn("Book with id={} not found for update", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Update the available count of a particular Book", description = "Updates the available count of a particular book")
    @PatchMapping("/availablecount/{id}")
    public ResponseEntity<Book> updateBookCount(@PathVariable Integer id,@RequestBody BookAvailableUpdateDTO availableUpdateDTO){
        log.info("Updating available count for book id={} ", id);
        Book updated = bookService.updateAvailableBook(id, availableUpdateDTO);
        log.info("Available count updated for book id={}", id);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @Operation(summary = "Delete a book by BookID", description = "Deletes a book from the database using the BookID")
    @DeleteMapping("/deletebook/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Integer id) {
        log.info("Deleting book with id={}", id);
        String status = bookService.deleteBook(id);
        log.info("Delete operation result for book id={}: {}", id, status);
        return new ResponseEntity<>(status,  HttpStatus.OK);
    }
}