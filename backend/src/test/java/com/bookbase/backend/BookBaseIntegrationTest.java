package com.bookbase.backend;

import com.bookbase.backend.entity.Book;
import com.bookbase.backend.entity.BorrowingTransaction;
import com.bookbase.backend.entity.Fine;
import com.bookbase.backend.entity.Member;
import com.bookbase.backend.entity.Notification;
import com.bookbase.backend.model.*;

import org.springframework.security.test.context.support.WithMockUser;

import com.bookbase.backend.service.BookService;
import com.bookbase.backend.service.BorrowingTransactionService;
import com.bookbase.backend.service.FineService;
import com.bookbase.backend.service.MemberService;
import com.bookbase.backend.service.NotificationService;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;


@SpringBootTest
@AutoConfigureMockMvc
public class BookBaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean private BookService bookService;
    @MockitoBean private MemberService memberService;
    @MockitoBean private BorrowingTransactionService borrowingTransactionService;
    @MockitoBean private FineService fineService;
    @MockitoBean private NotificationService notificationService;

    private Book book1;
    private BookDTO bookDTO;
    private Member member1;
    private MemberDTO memberDTO;
    private LoginRequestDTO loginRequestDTO;
    private BorrowingTransaction transaction1;
    private BorrowingTransactionDTO transactionDTO;
    private Fine fine1;
    private FineDTO fineDTO;
    private Notification notification1;
    private NotificationDTO notificationDTO;

    @BeforeEach
    void setUp() {
        book1 = new Book(1, "The Great Gatsby", "F. Scott Fitzgerald", "Classic", "English", 10);
        
        bookDTO = new BookDTO();
        bookDTO.setTitle("New Book");
        bookDTO.setAuthor("New Author");
        bookDTO.setGenre("Fiction");
        bookDTO.setAvailableCopies(5);
        bookDTO.setLang("English");

        member1 = new Member();
        member1.setMemberID(1);
        member1.setUserName("johndoe");
        member1.setRole("ADMIN");
        
        memberDTO = new MemberDTO();
        memberDTO.setName("John Doe");
        memberDTO.setEmail("john.doe@example.com");
        memberDTO.setUsername("johndoe");
        memberDTO.setPassword("password123");
        memberDTO.setRole("ADMIN");
        memberDTO.setMembershipStatus("Active");

        loginRequestDTO = new LoginRequestDTO();
        loginRequestDTO.setUserName("johndoe");
        loginRequestDTO.setPassword("password123");

        transaction1 = new BorrowingTransaction();
        transaction1.setTransactionID(1);
        transaction1.setBookID(book1.getBookID());
        transaction1.setMemberID(member1.getMemberID());
        transaction1.setBorrowDate(new Date());
        transaction1.setReturnDate(new Date(System.currentTimeMillis() + 14 * 24 * 60 * 60 * 1000));
        transaction1.setStatus("Borrowed");

        transactionDTO = new BorrowingTransactionDTO();
        transactionDTO.setBookId(1);
        transactionDTO.setMemberId(1);
        transactionDTO.setBorrowDate(new Date());
        transactionDTO.setReturnDate(new Date(System.currentTimeMillis() + 14 * 24 * 60 * 60 * 1000));
        transactionDTO.setStatus("Borrowed");

        fine1 = new Fine();
        fine1.setFineID(1);
        fine1.setTransactionId(1);
        fine1.setMemberID(member1.getMemberID());
        fine1.setAmount(BigDecimal.valueOf(5.00));
        fine1.setStatus("Due");

        fineDTO = new FineDTO();
        fineDTO.setMemberId(1);
        fineDTO.setTransactionId(1);
        fineDTO.setAmount(BigDecimal.valueOf(5.00));
        fineDTO.setStatus("Due");
        fineDTO.setTransactionDate(new Date());

        notification1 = new Notification();
        notification1.setNotificationID(1);
        notification1.setMemberID(member1.getMemberID());
        notification1.setMessage("Your book is overdue.");
        
        notificationDTO = new NotificationDTO();
        notificationDTO.setMemberId(1);
        notificationDTO.setTransactionId(1);
        notificationDTO.setMessage("Test Notification");
    }

    // AuthController Tests

    @Test
    public void testRegisterMember_Success() throws Exception {
        when(memberService.registerMember(any(MemberDTO.class))).thenReturn(member1);

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(memberDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userName", is("johndoe")));
    }

    @Test
    public void testLogin_Success() throws Exception {
        String mockToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzdXJkLTI1IiwidXNlcklEIjoxMDA5LCJ1c2VyTmFtZSI6InN1cmQtMjUiLCJ1c2VyUm9sZSI6Ik1FTUJFUiIsImlhdCI6MTc2MTczNjQ0MiwiZXhwIjoxNzYxNzQzNjQyfQ.ivMRrFsxdu5479xcmdyFQsPaXSXSfW_tHzy9ryuc2QU";
        when(memberService.verify(any(LoginRequestDTO.class))).thenReturn(mockToken);

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string(mockToken));
    }

    // BookController Test

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetAllBooks_Success() throws Exception {
        List<Book> allBooks = Arrays.asList(book1);
        when(bookService.getAllBooks()).thenReturn(allBooks);

        mockMvc.perform(get("/book/getallbooks")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is("The Great Gatsby")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testCreateBook_Success() throws Exception {
        when(bookService.createBook(any(BookDTO.class))).thenReturn(book1);

        mockMvc.perform(post("/book/createnew")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is("The Great Gatsby")));
    }

    // BorrowingTransactionController Test
    @Test
    @WithMockUser(roles = "ADMIN")
    public void testCreateTransaction_Success() throws Exception {
        when(borrowingTransactionService.createTransaction(any(BorrowingTransactionDTO.class))).thenReturn(transaction1);

        mockMvc.perform(post("/transaction/createnew")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transactionDTO)))
                .andExpect(status().isOk()) // Controller returns OK
                .andExpect(jsonPath("$.transactionID", is(1)));
    }

    // FineController Test
    
    @Test
    @WithMockUser(roles = "ADMIN")
    public void testCreateFine_Success() throws Exception {
        when(fineService.createFine(any(FineDTO.class))).thenReturn(fine1);
        
        mockMvc.perform(post("/fine/createnew")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(fineDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status", is("Due")));
    }

    // MemberController Test

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetMemberById_Success() throws Exception {
        when(memberService.getMemberById(1)).thenReturn(member1);

        mockMvc.perform(get("/member/getbyid/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.memberID", is(1)))
                .andExpect(jsonPath("$.userName", is("johndoe")));
    }

    // NotificationController Test

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetNotificationsByMemberId_Success() throws Exception {
        List<Notification> notifications = Arrays.asList(notification1);
        when(notificationService.getByMemberId(1)).thenReturn(notifications);

        mockMvc.perform(get("/notification/getbymemberid/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].message", is("Your book is overdue.")));
    }
    
    @Test
    @WithMockUser(roles = "ADMIN")
    public void testCreateNotification_Success() throws Exception {
        when(notificationService.createNotification(any(NotificationDTO.class))).thenReturn(notification1);
        
        mockMvc.perform(post("/notification/createnew")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(notificationDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message", is("Your book is overdue.")));
    }
}

