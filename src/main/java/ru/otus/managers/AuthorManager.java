package ru.otus.managers;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ru.otus.dao.AuthorDao;
import ru.otus.dao.BookDao;
import ru.otus.domain.Author;
import ru.otus.domain.Book;
import ru.otus.exceptions.DBException;
import ru.otus.exceptions.NotFoundException;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class AuthorManager implements Manager<Author> {
    private AuthorDao authorDao;
    private BookDao bookDao;

    public AuthorManager(AuthorDao authorDao, BookDao bookDao) {
        this.bookDao = bookDao;
        this.authorDao = authorDao;
    }

    @Override
    public Author create(String name) {
        Author author = new Author(name);
        return authorDao.save(author);
    }

    @Override
    public Collection<Author> get(String name, String genre, String book) throws NotFoundException {
        Collection<Author> authors = new ArrayList<>();
        if (StringUtils.isBlank(name) && StringUtils.isBlank(genre) && StringUtils.isBlank(book)) {
            authors.addAll(authorDao.getAll());
            if (authors == null || authors.isEmpty()) {
                throw new NotFoundException("No Authors not found");
            }
            return authors;
        } else {
            if (StringUtils.isNotBlank(name)) {
                Author author = authorDao.getByName(name);
                if (author == null) {
                    throw new NotFoundException("Author with name: " + name + " not found");
                }
                authors.add(author);
            } else if (StringUtils.isNotBlank(book)) {
                authors.addAll(authorDao.getByBook(book));
                if (authors.isEmpty()) {
                    throw new NotFoundException("Authors of book: " + book + " not found");
                }
            } else if (StringUtils.isNotBlank(genre)) {
                authors.addAll(authorDao.getByGenre(genre));
                if (authors.isEmpty()) {
                    throw new NotFoundException("No authors with genre: " + genre);
                }
            }
        }
        return authors;
    }

    @Override
    public Author update(Author author) {
        return authorDao.update(author);
    }

    @Override
    public void delete(Author author) throws DBException {
        Collection<Book> book = bookDao.getByAuthor(author.getName());
        if (!book.isEmpty()) {
            throw new DBException("Cannot delete Author. Delete Books first");
        }
        authorDao.delete(author);
    }
}
