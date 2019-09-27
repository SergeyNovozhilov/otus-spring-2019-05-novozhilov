package ru.otus.Util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import ru.otus.domain.Book;
import ru.otus.dtos.BookDto;
import ru.otus.managers.BookManager;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class BookSaver {
    private BookManager bookManager;
    private Converter converter;

    @Autowired
    public BookSaver(BookManager bookManager, Converter converter) {
        this.bookManager = bookManager;
        this.converter = converter;
    }

    public String save (Optional<UUID>  id, BookDto book, BindingResult bindingResult) {
        String result = null;

        if (id == null) {
            if (bindingResult.hasErrors()) {
                result = "create";
            }
            Book bookObj = bookManager.create(book.getTitle());
            bookManager.addGenre(bookObj, book.getGenre());
            List<String> authors = Arrays.asList(book.getAuthors().split(","));
            bookManager.addAuthors(bookObj, authors);
        } else {
            if (bindingResult.hasErrors()) {
                result = "edit";
            }
            bookManager.update(converter.toBook(book));
        }

        return result;
    }
}
