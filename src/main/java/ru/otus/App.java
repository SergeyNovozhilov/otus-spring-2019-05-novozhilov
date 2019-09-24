package ru.otus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import ru.otus.domain.Author;
import ru.otus.domain.Book;
import ru.otus.domain.Genre;
import ru.otus.managers.AuthorManager;
import ru.otus.managers.BookManager;
import ru.otus.managers.GenreManager;

import java.util.*;

@SpringBootApplication
public class App {

    public static void main(String[] args) {

        ApplicationContext ac = SpringApplication.run(App.class, args);
        BookManager bm = ac.getBean(BookManager.class);

        Book book = bm.create("Book 1");
        List<String> authors = Arrays.asList("Author 1");
        bm.addAuthors(book, authors);
        bm.addGenre(book, "Genre 1");
        bm.update(book);
    }
}
