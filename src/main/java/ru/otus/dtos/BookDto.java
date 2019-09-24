package ru.otus.dtos;

import org.apache.commons.lang3.StringUtils;
import ru.otus.domain.Author;
import ru.otus.domain.Book;
import ru.otus.domain.Comment;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class BookDto {
    private UUID id;
    private String title;
    private List<String> authors;
    private String genre;
    private List<String> comments;

    public void setId(UUID id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setComments(List<String> comments) {
        this.comments = comments;
    }

    public String getId() {
        return id.toString();
    }

    public String getTitle() {
        return title;
    }

    public String getAuthors() {
        return String.join(", ", authors);
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
