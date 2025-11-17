package com.bookbase.bookservice.service;

import com.bookbase.bookservice.entity.Book;
import com.bookbase.bookservice.exception.BookNotFoundException;
import com.bookbase.bookservice.model.BookAvailableUpdateDTO;
import com.bookbase.bookservice.model.BookDTO;
import com.bookbase.bookservice.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    private Book sampleBook;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sampleBook = new Book();
        sampleBook.setBookID(1);
        sampleBook.setTitle("Test Book");
        sampleBook.setGenre("Fiction");
        sampleBook.setAvailableCopies(5);
        sampleBook.setAuthor("Author Name");
        sampleBook.setLanguage("English");
    }

    @Test
    void testGetAllBooks() {
        when(bookRepository.findAll()).thenReturn(List.of(sampleBook));
        List<Book> books = bookService.getAllBooks();
        assertEquals(1, books.size());
        assertEquals("Test Book", books.get(0).getTitle());
    }

    @Test
    void testGetBookById_Found() {
        when(bookRepository.findById(1)).thenReturn(Optional.of(sampleBook));
        Book book = bookService.getBookById(1);
        assertEquals("Test Book", book.getTitle());
    }

    @Test
    void testGetBookById_NotFound() {
        when(bookRepository.findById(2)).thenReturn(Optional.empty());
        assertThrows(BookNotFoundException.class, () -> bookService.getBookById(2));
    }

    @Test
    void testCreateBook() {
        BookDTO dto = new BookDTO("Test Book", "Fiction", "Author Name", 5, "English");
        when(bookRepository.save(any(Book.class))).thenReturn(sampleBook);
        Book created = bookService.createBook(dto);
        assertEquals("Test Book", created.getTitle());
    }

    @Test
    void testUpdateBook() {
        when(bookRepository.findById(1)).thenReturn(Optional.of(sampleBook));
        when(bookRepository.save(any(Book.class))).thenReturn(sampleBook);

        Book updatedDetails = new Book();
        updatedDetails.setTitle("Updated Title");
        updatedDetails.setGenre("Drama");
        updatedDetails.setAvailableCopies(10);
        updatedDetails.setAuthor("New Author");
        updatedDetails.setLanguage("Hindi");

        Book updated = bookService.updateBook(1, updatedDetails);
        assertEquals("Updated Title", updated.getTitle());
        assertEquals("Drama", updated.getGenre());
    }

    @Test
    void testDeleteBook() {
        when(bookRepository.findById(1)).thenReturn(Optional.of(sampleBook));
        doNothing().when(bookRepository).delete(sampleBook);

        String result = bookService.deleteBook(1);
        assertEquals("Book Deleted with id: 1", result);
    }

    @Test
    void testUpdateAvailableBook() {
        when(bookRepository.findById(1)).thenReturn(Optional.of(sampleBook));
        when(bookRepository.save(any(Book.class))).thenReturn(sampleBook);

        BookAvailableUpdateDTO dto = new BookAvailableUpdateDTO(3);
        Book updated = bookService.updateAvailableBook(1, dto);
        assertEquals(3, updated.getAvailableCopies());
    }
}
