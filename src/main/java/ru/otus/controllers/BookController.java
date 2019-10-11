package ru.otus.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.otus.util.BookService;
import ru.otus.dtos.BookDto;
import ru.otus.exceptions.DBException;
import ru.otus.exceptions.NotFoundException;

import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

@Controller
public class BookController {
    private BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping({"/", "/home"})
    public String homePage() {
        return "index";
    }

    @GetMapping(value = "/books")
    public String listPage(Model model) {
        model.addAttribute("books", bookService.getAllBooks());

        return "list";
    }

    @PostMapping(value = "/edit")
    public String editPage(@RequestParam("id") UUID id, Model model) {
        try {
            model.addAttribute("book", bookService.getBook(id));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }

        return "edit";
    }

    @GetMapping(value = "/create")
    public String createPage(Model model) {
        model.addAttribute("book", new BookDto());

        return "create";
    }

    @PostMapping(value = {"/save", "/save/{id}"})
    public String save(@PathVariable Optional<UUID>  id, @Valid @ModelAttribute("book") BookDto book, BindingResult bindingResult) {
        String saveResult = bookService.save(id, book, bindingResult);

        return saveResult == null ? "redirect:/books" : saveResult;
    }

    @PostMapping(value = "/delete/{id}")
    public String delete(@PathVariable("id") UUID id, Model model) {
        if (id != null) {
            try {
                bookService.delete(id);
            } catch (DBException | NotFoundException e) {
                e.printStackTrace();
            }
        }
        model.addAttribute("books", bookService.getAllBooks());

        return "redirect:/books";
    }
}
