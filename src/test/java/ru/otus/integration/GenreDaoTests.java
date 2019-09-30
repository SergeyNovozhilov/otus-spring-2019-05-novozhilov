package ru.otus.integration;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import ru.otus.daoImpl.GenreDaoImpl;
import ru.otus.domain.Author;
import ru.otus.domain.Book;
import ru.otus.domain.Genre;
import ru.otus.exceptions.DBException;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(GenreDaoImpl.class)
public class GenreDaoTests {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private GenreDaoImpl GenreDaoImpl;

    @Test
    public void getAll() {
        List<Genre> expected = Arrays.asList(new Genre("Novel"), new Genre("Humor"));
        expected.forEach(e -> testEntityManager.persist(e));
        testEntityManager.flush();
        Collection<Genre> actual = GenreDaoImpl.getAll();
        assertEquals(actual, expected);
    }

    @Test
    public void getByName() {
        Genre expected = new Genre("Novel");
        testEntityManager.persistAndFlush(expected);
        Genre actual = GenreDaoImpl.getByName(expected.getName());
        assertEquals(actual, expected);
    }

    @Test
    public void getById() {
        Genre expected = new Genre("Novel");
        testEntityManager.persistAndFlush(expected);
        Genre actual = GenreDaoImpl.getById(expected.getId());
        assertEquals(actual, expected);
    }

    @Test
    public void getByAuthor() {
        String expectedGenreName = "Novel";
        String bookTitle = "Book by Jack London";
        String author = "Jack London";
        Book book = createBook(bookTitle, expectedGenreName, author);
        book = testEntityManager.persistAndFlush(book);
        Book b = testEntityManager.find(Book.class, book.getId());
        Collection<Genre> actual = GenreDaoImpl.getByAuthor(author);
        assertTrue(actual.size() == 1);
        assertEquals(actual.iterator().next().getName(), expectedGenreName);
    }

    @Test
    public void getByBook() {
        String expectedGenreName = "Novel";
        String book = "Book by Jack London";
        testEntityManager.persistAndFlush(createBook(book, expectedGenreName, ""));
        Genre actual = GenreDaoImpl.getByBook(book);
        assertEquals(actual.getName(), expectedGenreName);
    }

    @Test
    public void save() {
        Genre expected = new Genre("Humor");
        GenreDaoImpl.save(expected);
        Genre actual = testEntityManager.find(Genre.class, expected.getId());
        assertEquals(actual, expected);
    }

    @Test
    public void delete() {
        Genre expected = new Genre("Novel");
        testEntityManager.persistAndFlush(expected);
        Genre actual = GenreDaoImpl.getById(expected.getId());
        assertEquals(actual, expected);
        try {
            GenreDaoImpl.delete(expected);
        } catch (DBException e) {
            fail();
        }
        actual = testEntityManager.find(Genre.class, expected.getId());
        assertNull(actual);
    }

    @Test
    public void update() {
        String newName = "Romantic";
        Genre expected = new Genre("Novel");
        GenreDaoImpl.save(expected);
        Genre actual = testEntityManager.find(Genre.class, expected.getId());
        assertEquals(actual, expected);
        expected.setName(newName);
        GenreDaoImpl.update(expected);
        actual = testEntityManager.find(Genre.class, expected.getId());
        assertEquals(actual.getName(), expected.getName());
    }

    private Book createBook(String bookStr, String genreStr, String authorStr) {
        Book book = new Book(bookStr);
        Genre genre = null;
        if (StringUtils.isNotBlank(genreStr)) {
            genre = new Genre(genreStr);
            testEntityManager.persistAndFlush(genre);
            book.setGenre(genre);
        }
        if (StringUtils.isNotBlank(authorStr)) {
            Author author = new Author(authorStr);
            testEntityManager.persistAndFlush(author);
            book.addAuthor(author);
        }
        return book;
    }
}