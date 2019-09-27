package ru.otus.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.otus.Util.BookSaver;
import ru.otus.Util.Converter;
import ru.otus.domain.Book;
import ru.otus.dtos.BookDto;
import ru.otus.exceptions.DBException;
import ru.otus.exceptions.NotFoundException;
import ru.otus.managers.BookManager;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class BookController {
    private BookManager bookManager;
    private Converter converter;
    private BookSaver saver;

    @Autowired
    public BookController(BookManager bookManager, Converter converter, BookSaver saver) {
        this.bookManager = bookManager;
        this.converter = converter;
        this.saver = saver;
    }

    @GetMapping({"/", "/home"})
    public String homePage() {
        return "index";
    }

    @GetMapping(value = "/books")
    public String listPage(Model model) {
        model.addAttribute("books", getAllBooks());
        return "list";
    }

    @PostMapping(value = "/edit")
    public String editPage(@RequestParam("id") UUID id, Model model) {
        try {
            Book book = bookManager.get(id);
            model.addAttribute("book", converter.toBookDto(book));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        return "edit";
    }

    @GetMapping(value = "/create")
    public String createPage(Model model) {
        BookDto book = new BookDto();
        model.addAttribute("book", book);

        return "create";
    }

    @PostMapping(value = {"/save", "/save/{id}"})
    public String save(@PathVariable Optional<UUID>  id, @Valid @ModelAttribute("book") BookDto book, BindingResult bindingResult, Model model) {
        String saveResult = saver.save(id, book, bindingResult);
        return saveResult == null ? "redirect:/books" : saveResult;
    }

    @PostMapping(value = "/delete/{id}")
    public String delete(@PathVariable("id") UUID id, Model model) {
        if (id != null) {
            try {
                bookManager.delete(bookManager.get(id));
            } catch (DBException | NotFoundException e) {
                e.printStackTrace();
            }
        }
        model.addAttribute("books", getAllBooks());
        return "redirect:/books";
    }

    private List<BookDto> getAllBooks() {
        List<BookDto> bookDtoList = null;
        try {
            Collection<Book> bookCollection = bookManager.get(null, null, null);
            bookDtoList = bookCollection.stream().map(b -> converter.toBookDto(b)).collect(Collectors.toList());
        } catch (NotFoundException e) {
            e.printStackTrace();
            bookDtoList = Collections.EMPTY_LIST;
        }

        return bookDtoList;
    }
}
