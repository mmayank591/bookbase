package com.bookbase.bookservice.repository;

import com.bookbase.bookservice.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

    Optional<Book> findByTitleContainsIgnoreCase(String title);
    List<Book> findByAuthorContainsIgnoreCase(String author);
    List<Book> findByGenreContainsIgnoreCase(String genre);

}