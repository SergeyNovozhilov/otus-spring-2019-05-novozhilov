package ru.otus.dtos;

import org.apache.commons.lang3.StringUtils;
import ru.otus.domain.Author;
import ru.otus.domain.Book;
import ru.otus.domain.Comment;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class BookDto {
    private UUID id;
    @NotBlank(message = "Title is mandatory")
    private String title;
    @NotBlank(message = "Author is mandatory")
    private String authors;
    private String genre;
    private String comments;

    public void setId(UUID id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthors() {
        return authors;
    }

    public String getGenre() {
        return genre;
    }

    public String getComments() {
        if (comments == null) {
            return "";
        }
        return String.join(", ", comments);
    }
}
