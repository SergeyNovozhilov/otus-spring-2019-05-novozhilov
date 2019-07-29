package ru.otus.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.otus.domain.Book;
import ru.otus.dtos.BookDto;
import ru.otus.exceptions.NotFoundException;
import ru.otus.managers.BookManager;

import java.util.Collection;
import java.util.List;

@Controller
public class BookController {
    private BookManager bookManager;

    @Autowired
    public BookController(BookManager bookManager) {
        this.bookManager = bookManager;
    }

    @RequestMapping(value = "/books", method = RequestMethod.GET)
    public String getBooks(Model model) {
        try {
            Collection<Book> books = bookManager.get(null, null, null);
            model.addAttribute("books", books.stream().map(BookDto::new));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        return "books";
    }
}
