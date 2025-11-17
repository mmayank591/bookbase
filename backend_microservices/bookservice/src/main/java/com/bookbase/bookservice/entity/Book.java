package com.bookbase.bookservice.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Books")
public class Book implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BookID")
    private Integer bookID;

    @Column(name = "Title", nullable = false)
    private String title;

    @Column(name = "Author", nullable = false)
    private String author;

    @Column(name = "Genre")
    private String genre;

    @Column(name = "Language")
    private String language;

    @Column(name = "AvailableCopies")
    private Integer availableCopies;
    
}