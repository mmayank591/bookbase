package com.bookbase.backend.service;

import com.bookbase.backend.entity.Book;
import com.bookbase.backend.exception.BookNotFoundException;
import com.bookbase.backend.model.BookAvailableUpdateDTO;
import com.bookbase.backend.model.BookDTO;
import com.bookbase.backend.repository.BookRepository;

import lombok.extern.slf4j.Slf4j; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public List<Book> getAllBooks() {
        log.debug("Fetching all books from repository");
        List<Book> books = bookRepository.findAll();
        log.info("Fetched {} books from repository", books.size());
        return books;
    }

    public Book getBookById(int id) {
        log.debug("Fetching book with id={}", id);
        return bookRepository.findById(id)
                .map(book -> {
                    log.info("Book found with id={} and title={}", id, book.getTitle());
                    return book;
                })
                .orElseThrow(() -> {
                    log.warn("Book not found with id={}", id);
                    return new BookNotFoundException("Book not found with id: " + id);
                });
    }

    public Book createBook(BookDTO newBookDTO) {
        log.info("Creating new book with title={}, author={}", newBookDTO.getTitle(), newBookDTO.getAuthor());
        Book newBook = new Book();
        newBook.setTitle(newBookDTO.getTitle());
        newBook.setGenre(newBookDTO.getGenre());
        newBook.setAvailableCopies(newBookDTO.getAvailableCopies());
        newBook.setAuthor(newBookDTO.getAuthor());
        newBook.setLanguage(newBookDTO.getLang());

        Book saved = bookRepository.save(newBook);
        log.info("Book created successfully ");
        return saved;
    }

    public Book updateBook(Integer id, Book bookDetails) {
        log.info("Updating book with id={}", id);

        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Book not found with id={} for update", id);
                    return new BookNotFoundException("Book not found with id: " + id);
                });

        existingBook.setTitle(bookDetails.getTitle());
        existingBook.setLanguage(bookDetails.getLanguage());
        existingBook.setAuthor(bookDetails.getAuthor());
        existingBook.setGenre(bookDetails.getGenre());
        existingBook.setAvailableCopies(bookDetails.getAvailableCopies());

        Book updated = bookRepository.save(existingBook);
        log.info("Book updated successfully ");
        return updated;
    }

    public String deleteBook(int id) {
        log.info("Deleting book with id={}", id);

        Book book = bookRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Book not found with id={} for deletion", id);
                    return new BookNotFoundException("Book not found with id: " + id);
                });

        bookRepository.delete(book);
        log.info("Book deleted successfully with id={}", id);
        return "Book Deleted with id: " + id;
    }

    public Book updateAvailableBook(Integer id, BookAvailableUpdateDTO availableUpdateDTO) {
        log.info("Updating available copies for book id={} to {}", id, availableUpdateDTO.getAvail());

        Book book = bookRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Book not found with id={} for available count update", id);
                    return new BookNotFoundException("Book not found with id: " + id);
                });

        book.setAvailableCopies(availableUpdateDTO.getAvail());
        Book updated = bookRepository.save(book);
        log.info("Available copies updated for book id={} to {}", id, updated.getAvailableCopies());
        return updated;
    }
}