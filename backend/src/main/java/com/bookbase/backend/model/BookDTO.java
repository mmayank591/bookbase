package com.bookbase.backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDTO {

    @JsonProperty("title")
    private String title;

    @JsonProperty("author")
    private String author;

    @JsonProperty("genre")
    private String genre;

    @JsonProperty("availableCopies")
    private int availableCopies;

    @JsonProperty("language")
    private String lang;

    @Override
    public String toString() {
        return "BookDTO{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", genre='" + genre + '\'' +
                ", availableCopies=" + availableCopies +
                ", lang='" + lang + '\'' +
                '}';
    }
}
