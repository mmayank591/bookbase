package com.bookbase.backend.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "Books")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookID")
    private Integer bookID;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "author", nullable = false)
    private String author;

    @Column(name = "genre")
    private String genre;

    @Column(name = "language")
    private String language;

    @Column(name = "availableCopies")
    private Integer availableCopies;
    
}