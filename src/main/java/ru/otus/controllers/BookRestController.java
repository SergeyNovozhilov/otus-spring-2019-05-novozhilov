package ru.otus.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.otus.Util.BookHelper;
import ru.otus.dtos.BookDto;
import ru.otus.exceptions.DBException;
import ru.otus.exceptions.NotFoundException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
public class BookRestController {
    private BookHelper bookHelper;

    @Autowired
    public BookRestController(BookHelper bookHelper) {
        this.bookHelper = bookHelper;
    }

    @GetMapping(value = "/books")
    public List<BookDto> listPage() {
        return bookHelper.getAllBooks();
    }

    @PostMapping(value = "/create")
    public void create(@RequestBody  BookDto book, HttpServletResponse response) throws IOException {
        bookHelper.save(null, book);
    }

    @PostMapping(value = {"/save/{id}"})
    public void save(@PathVariable UUID id, @ModelAttribute  BookDto book, HttpServletResponse response) throws IOException {
        bookHelper.save(id, book);
        response.sendRedirect("/list.html");
    }

    @PostMapping(value = "/delete/{id}")
    public void delete(@PathVariable UUID id, HttpServletResponse response) throws IOException {
        if (id != null) {
            try {
                bookHelper.delete(id);
            } catch (DBException | NotFoundException e) {
                e.printStackTrace();
            }
        }
        response.sendRedirect("/list.html");
    }
}
