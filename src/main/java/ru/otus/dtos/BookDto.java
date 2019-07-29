package ru.otus.dtos;

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

    public BookDto(Book book) {
        this.id = book.getId();
        this.title = book.getTitle();
        this.genre = book.getGenre().getName();
        this.authors = book.getAuthors().stream().map(Author::getName).collect(Collectors.toList());
        this.comments = book.getComments().stream().map(Comment::getComment).collect(Collectors.toList());
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
        return String.join(", ", comments);
    }
}
