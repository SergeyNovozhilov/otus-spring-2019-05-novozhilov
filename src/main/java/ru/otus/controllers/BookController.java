package ru.otus.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.otus.Util.BookHelper;
import ru.otus.exceptions.NotFoundException;

import java.util.UUID;

@Controller
public class BookController {

    private BookHelper bookHelper;

    public BookController(BookHelper bookHelper) {
        this.bookHelper = bookHelper;
    }

    @PostMapping(value = "/edit/{id}")
    public String editPage(@PathVariable("id") UUID id, Model model) {
        try {
            model.addAttribute("book", bookHelper.getBook(id));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }

        return "edit";
    }
}
