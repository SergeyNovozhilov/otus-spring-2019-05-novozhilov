package ru.otus.managers;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.dao.AuthorDao;
import ru.otus.dao.BookDao;
import ru.otus.dao.GenreDao;
import ru.otus.domain.Author;
import ru.otus.domain.Book;
import ru.otus.domain.Comment;
import ru.otus.domain.Genre;
import ru.otus.exceptions.DBException;
import ru.otus.exceptions.NotFoundException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Service
public class BookManager implements Manager<Book> {
    private AuthorDao authorDao;
    private GenreDao genreDao;
    private BookDao bookDao;

    public BookManager(AuthorDao authorDao, GenreDao genreDao, BookDao bookDao) {
        this.authorDao = authorDao;
        this.genreDao = genreDao;
        this.bookDao = bookDao;
    }

    public Book get(UUID id) throws NotFoundException {
        Book book = bookDao.getById(id);
        if (book == null) {
            throw new NotFoundException("Book not found");
        }

        return book;
    }

    @Override
    public Book create(String title) {
        Book book = new Book(title);
        return bookDao.save(book);
    }

    public Book create() {
        Book book = new Book();
        return bookDao.save(book);
    }

    public Book addGenre(Book book, String genreName) {
        Genre genre = genreDao.getByName(genreName);
        if (genre == null) {
            genre = new Genre(genreName);
            genreDao.save(genre);
        }
        book.setGenre(genre);

        return bookDao.update(book);
    }

    public Book addAuthors(Book book, List<String> authors) {
        authors.forEach(a -> {
            Author author = authorDao.getByName(a);
            if (author == null) {
                author = new Author(a);
                authorDao.save(author);
            }
            book.addAuthor(author);
        });

        return bookDao.update(book);
    }

    public Book addComment(Book book, String comment) {
        book.addComment(new Comment(comment));

        return bookDao.update(book);
    }

    public Book removeComment(Book book, String comment) {
        Comment commentObj = book.getComments().stream().filter(c -> StringUtils.equalsIgnoreCase(c.getComment(), comment)).findAny().orElse(null);
        book.getComments().remove(commentObj);

        return bookDao.update(book);
    }

    @Override
    public Collection<Book> get(String title, String genre, String author) throws NotFoundException {
        Collection<Book> books = new ArrayList<>();
        Collection<Book> booksByTitle = null;
        Collection<Book> booksByGenre = null;
        Collection<Book> booksByAuthor = null;

        if (StringUtils.isBlank(title) && StringUtils.isBlank(genre) && StringUtils.isBlank(author)) {
            return returnBooks(bookDao.getAll());
        }

        if (StringUtils.isNotBlank(title) && StringUtils.isNotBlank(author) && StringUtils.isBlank(genre)) {
            booksByTitle = bookDao.getByTitle(title);
            booksByTitle.forEach(b -> {
                if (b.getAuthors().stream().filter(a -> a.getName().equals(author)).findAny().orElse(null) != null) {
                    books.add(b);
                }
            });
            return returnBooks(books);
        }

        if (StringUtils.isNotBlank(title)) {
            booksByTitle = bookDao.getByTitle(title);
            if (!booksByTitle.isEmpty() && books.isEmpty()) {
                books.addAll(booksByTitle);
            }
        }
        if (StringUtils.isNotBlank(genre)) {
            booksByGenre = bookDao.getByGenre(genre);
            if (!booksByGenre.isEmpty() && books.isEmpty()) {
                books.addAll(booksByGenre);
            }
        }
        if (StringUtils.isNotBlank(author)) {
            booksByAuthor = bookDao.getByAuthor(author);
            if (!booksByAuthor.isEmpty() && books.isEmpty()) {
                books.addAll(booksByAuthor);
            }
        }

        if (booksByTitle != null) {
            books.retainAll(booksByTitle);
        }
        if (booksByGenre != null) {
            books.retainAll(booksByGenre);
        }
        if (booksByAuthor != null) {
            books.retainAll(booksByAuthor);
        }

        return returnBooks(books);
    }

    @Override
    public Book update(Book book) {
        return bookDao.update(book);
    }

    @Override
    @Transactional
    public void delete(Book book) throws DBException {
        Collection<Author> authors = authorDao.getByBook(book.getTitle());
        authors.forEach(a -> {
            a.getBooks().remove(book);
            authorDao.update(a);
        });
        bookDao.delete(book);
    }

    private Collection<Book> returnBooks(Collection<Book> books) throws NotFoundException{
        if (books == null || books.isEmpty()) {
            throw new NotFoundException("No Books were found.");
        }
        return books;
    }
}
