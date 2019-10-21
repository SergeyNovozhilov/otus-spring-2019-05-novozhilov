package ru.otus.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.otus.Util.BookService;
import ru.otus.exceptions.NotFoundException;

import java.util.UUID;

@Controller
public class BookController {

    private BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping(value = "/books/{id}")
    public String editPage(@PathVariable("id") UUID id, Model model) {
        try {
            model.addAttribute("book", bookService.getBook(id));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }

        return "edit";
    }

    @GetMapping(value = "/comment/{id}")
    public String addCommentPage(@PathVariable("id") UUID id, Model model) {
        try {
            model.addAttribute("book", bookService.getBook(id));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }

        return "comment";
    }
}
