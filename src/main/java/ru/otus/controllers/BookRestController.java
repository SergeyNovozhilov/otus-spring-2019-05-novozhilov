package ru.otus.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.otus.Util.BookService;
import ru.otus.dtos.BookDto;
import ru.otus.exceptions.DBException;
import ru.otus.exceptions.NotFoundException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
public class BookRestController {
    private BookService bookService;

    @Autowired
    public BookRestController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping(value = "/books")
    public List<BookDto> listPage() {
        return bookService.getAllBooks();
    }

    @PostMapping(value = "/books")
    public void create(@RequestBody  BookDto book, HttpServletResponse response) throws IOException {
        bookService.save(null, book);
    }

    @PutMapping(value = {"/books/{id}"})
    public void save(@PathVariable UUID id, @ModelAttribute  BookDto book, HttpServletResponse response) throws IOException {
        bookService.save(id, book);
        response.sendRedirect("/list.html");
    }

    @DeleteMapping(value = "/books/{id}")
    public void delete(@PathVariable UUID id, HttpServletResponse response) throws IOException {
        if (id != null) {
            try {
                bookService.delete(id);
            } catch (DBException | NotFoundException e) {
                e.printStackTrace();
            }
        }
        response.sendRedirect("/list.html");
    }
}
