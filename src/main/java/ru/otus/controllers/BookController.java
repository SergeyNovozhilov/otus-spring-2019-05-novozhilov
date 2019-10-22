package ru.otus.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.otus.util.BookService;
import ru.otus.dtos.BookDto;
import ru.otus.exceptions.DBException;
import ru.otus.exceptions.NotFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    @GetMapping(value = "/list")
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

        return saveResult == null ? "redirect:/list" : saveResult;
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

        return "redirect:/list";
    }

    @PostMapping(value = "/comment")
    public String addCommentPage(@RequestParam("id") UUID id, Model model) {
        try {
            model.addAttribute("book", bookService.getBook(id));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }

        return "comment";
    }

    @GetMapping(value="/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/home?logout=true";
    }
}
