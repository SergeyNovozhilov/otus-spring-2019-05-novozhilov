package ru.otus.Util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import ru.otus.domain.Book;
import ru.otus.dtos.BookDto;
import ru.otus.exceptions.DBException;
import ru.otus.exceptions.NotFoundException;
import ru.otus.managers.BookManager;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class BookHelper {
    private BookManager bookManager;
    private Converter converter;

    @Autowired
    public BookHelper(BookManager bookManager, Converter converter) {
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

    public List<BookDto> getAllBooks() {
        List<BookDto> bookDtoList;
        try {
            Collection<Book> bookCollection = bookManager.get(null, null, null);
            bookDtoList = bookCollection.stream().map(b -> converter.toBookDto(b)).collect(Collectors.toList());
        } catch (NotFoundException e) {
            System.out.println("No found.");
            bookDtoList = Collections.EMPTY_LIST;
        }

        return bookDtoList;
    }

    public BookDto getBook(UUID id) throws NotFoundException {
        Book book = bookManager.get(id);
        return converter.toBookDto(book);
    }

    public void delete (UUID id) throws NotFoundException, DBException {
        bookManager.delete(bookManager.get(id));
    }
}
