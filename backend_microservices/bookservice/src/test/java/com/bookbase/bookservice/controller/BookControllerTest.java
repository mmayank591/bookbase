package com.bookbase.bookservice.controller;

import com.bookbase.bookservice.entity.Book;
import com.bookbase.bookservice.model.BookAvailableUpdateDTO;
import com.bookbase.bookservice.model.BookDTO;
import com.bookbase.bookservice.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class BookControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    private Book sampleBook;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
        objectMapper = new ObjectMapper();

        sampleBook = new Book();
        sampleBook.setBookID(1);
        sampleBook.setTitle("Test Book");
        sampleBook.setGenre("Fiction");
        sampleBook.setAvailableCopies(5);
        sampleBook.setAuthor("Author Name");
        sampleBook.setLanguage("English");
    }

    @Test
    void testGetAllBooks() throws Exception {
        when(bookService.getAllBooks()).thenReturn(List.of(sampleBook));

        mockMvc.perform(get("/book/getallbooks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test Book"));
    }

    @Test
    void testGetBookById() throws Exception {
        when(bookService.getBookById(1)).thenReturn(sampleBook);

        mockMvc.perform(get("/book/getbyid/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Book"));
    }

    @Test
    void testAddBook() throws Exception {
        BookDTO dto = new BookDTO("Test Book", "Fiction", "Author Name", 5, "English");
        when(bookService.createBook(any(BookDTO.class))).thenReturn(sampleBook);

        mockMvc.perform(post("/book/createnew")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Test Book"));
    }

    @Test
    void testUpdateBook() throws Exception {
        when(bookService.updateBook(eq(1), any(Book.class))).thenReturn(sampleBook);

        mockMvc.perform(put("/book/updateput/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleBook)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Book"));
    }

    @Test
    void testUpdateBookCount() throws Exception {
        BookAvailableUpdateDTO dto = new BookAvailableUpdateDTO(3);
        sampleBook.setAvailableCopies(3);
        when(bookService.updateAvailableBook(eq(1), any(BookAvailableUpdateDTO.class))).thenReturn(sampleBook);

        mockMvc.perform(patch("/book/availablecount/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.availableCopies").value(3));
    }

    @Test
    void testDeleteBook() throws Exception {
        when(bookService.deleteBook(1)).thenReturn("Book Deleted with id: 1");

        mockMvc.perform(delete("/book/deletebook/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Book Deleted with id: 1"));
    }
}
