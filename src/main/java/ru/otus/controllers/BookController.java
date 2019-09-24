package ru.otus.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.otus.Util.Converter;
import ru.otus.domain.Book;
import ru.otus.dtos.BookDto;
import ru.otus.exceptions.DBException;
import ru.otus.exceptions.NotFoundException;
import ru.otus.managers.BookManager;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class BookController {
    private BookManager bookManager;
    private Converter converter;

    @Autowired
    public BookController(BookManager bookManager, Converter converter) {
        this.bookManager = bookManager;
        this.converter = converter;
    }

    @RequestMapping({"/", "/home"})
    public String homePage(Model model) {
        return "index";
    }

    @RequestMapping(value = "/books", method = RequestMethod.GET)
    public String booksList(Model model) {
        model.addAttribute("books", getAllBooks());
        return "list";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String editPage(@RequestParam("id") UUID id, Model model) {
        try {
            Book book = bookManager.get(id);
            model.addAttribute("book", converter.toBookDto(book));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        return "edit";
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(Model model) {
        Book book = bookManager.create();
        model.addAttribute("book", book);

        return "create";
    }

    @RequestMapping(value = "/save/{id}", method = RequestMethod.POST)
    public String save(@PathVariable("id") UUID id, @ModelAttribute("book") BookDto book, Model model) {
//        if (id == null) {
//            Book bookObj = bookManager.create(book.getTitle());
//            bookManager.addGenre(bookObj, book.getGenre());
//            List<String> authors = Arrays.asList(book.getAuthors().split(","));
//            bookManager.addAuthors(bookObj, authors);
//            bookManager.addComment(bookObj, book.getComments());
//        } else {
//            bookManager.update(converter.toBook(book));
//        }
        bookManager.update(converter.toBook(book));

        model.addAttribute("books", getAllBooks());

        return "list";
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public String delete(@PathVariable("id") UUID id, Model model) {
        if (id != null) {
            try {
                bookManager.delete(bookManager.get(id));
            } catch (DBException | NotFoundException e) {
                e.printStackTrace();
            }
        }
        model.addAttribute("books", getAllBooks());
        return "list";
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
