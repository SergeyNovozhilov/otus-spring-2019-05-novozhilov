package ru.otus.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.otus.domain.Author;
import ru.otus.domain.Book;
import ru.otus.domain.Comment;
import ru.otus.domain.Genre;
import ru.otus.dtos.BookDto;
import ru.otus.exceptions.NotFoundException;
import ru.otus.managers.AuthorManager;
import ru.otus.managers.GenreManager;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class Converter {

    private AuthorManager authorManager;
    private GenreManager genreManager;

    @Autowired
    public Converter(AuthorManager authorManager, GenreManager genreManager) {
        this.authorManager = authorManager;
        this.genreManager = genreManager;
    }

    public BookDto toBookDto(Book book) {
        BookDto dto = new BookDto();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        if (book.getGenre() != null) {
            dto.setGenre(book.getGenre().getName());
        }
        if (book.getAuthors() != null && !book.getAuthors().contains(null)) {
            dto.setAuthors(String.join(",", book.getAuthors().stream().map(Author::getName).collect(Collectors.toList())));
        }
        if (book.getComments() != null && !book.getComments().contains(null)) {
            dto.setComments(String.join(",", book.getComments().stream().map(Comment::getComment).collect(Collectors.toList())));
        }

        return dto;
    }

    public Book toBook (BookDto dto) {
        Book book = new Book();
        if (dto.getId() != null) {
            book.setId(UUID.fromString(dto.getId().toString()));
        }
        book.setTitle(dto.getTitle());
        book.setAuthors(getAuthors(dto.getAuthors()));
        book.setGenre(getGenre(dto.getGenre()));
        book.setComments(Arrays.asList(new Comment(dto.getComments())));

        return book;
    }

    private Collection<Author> getAuthors (String authorsStr) {
        List<String> authorsList = Arrays.asList(authorsStr.split(","));
        Collection<Author> authors = new ArrayList<>();
        authorsList.stream().filter(a -> StringUtils.isNotBlank(a)).forEach(a -> {
            Author author = null;
            try {
                author = authorManager.get(a, null, null).iterator().next();
            } catch (NotFoundException e) {
                System.out.println("Author " + a + " not found. Creating");
                author = authorManager.create(a);
            }
            authors.add(author);
        });

        return authors;
    }

    private Genre getGenre (String genreStr) {
        Genre genre = null;
        if (StringUtils.isNotBlank(genreStr)) {
            try {
                genre = genreManager.get(genreStr, null, null).iterator().next();
            } catch (NotFoundException e) {
                System.out.println("Genre " + genreStr + " not found. Creating");
                genre = genreManager.create(genreStr);
            }
        } else {
            genre = new Genre();
        }

        return genre;
    }

}
