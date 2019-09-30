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
import ru.otus.daoImpl.BookDaoImpl;
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
@Import(BookDaoImpl.class)
public class BookDaoTests {

	@Autowired
	private TestEntityManager testEntityManager;

	@Autowired
	private BookDaoImpl BookDaoImpl;

	@Test
	public void getAll() {
		List<Book> expected = Arrays.asList(new Book("Book"), new Book("Book1"));
		expected.forEach(e -> testEntityManager.persist(e));
		testEntityManager.flush();
		Collection<Book> actual = BookDaoImpl.getAll();
		expected.forEach( e -> {
			Book b = actual.stream().filter(x -> x.getId().equals(e.getId())).findFirst().orElse(null);
			if (b == null) {
				fail();
			}
			assertEquals(e.getTitle(), b.getTitle());
			assertEquals(e.getGenre(), b.getGenre());
		});
	}

	@Test
	public void getByTitle() {
		Book expected = new Book("Book by Steven King");
		testEntityManager.persistAndFlush(expected);
		Collection<Book> actual = BookDaoImpl.getByTitle(expected.getTitle());
		assertTrue(actual.size() == 1);
		assertTrue(actual.contains(expected));
	}

	@Test
	public void getById() {
		Book expected = new Book("Book by Steven King");
		testEntityManager.persistAndFlush(expected);
		Book actual = BookDaoImpl.getById(expected.getId());
		assertEquals(actual, expected);
	}

	@Test
	public void getByAuthor() {
		String bookTitle = "Book by Jack London";
		String authorName = "Jack London";
		createAndPersistBook(bookTitle, "Genre", authorName);
		Collection<Book> actual = BookDaoImpl.getByAuthor(authorName);
		assertTrue(actual.size() == 1);
		assertEquals(actual.iterator().next().getTitle(), "Book by Jack London");
		assertTrue(true);
	}

	@Test
	public void getByGenre() {
		String genreName = "Thriller";
		String authorName = "Steven King";
		String bookTitle = "Book";
		createAndPersistBook(bookTitle, genreName, authorName);
		Collection<Book> actual = BookDaoImpl.getByGenre(genreName);
		assertTrue(actual.size() == 1);
		assertNotNull(actual.stream().map(Book::getTitle).filter(name -> name.equals(bookTitle)).findAny().orElse(null));
	}

	@Test
	public void save() {
		Book expected = new Book("Book by Steven King");
		BookDaoImpl.save(expected);
		Book actual = testEntityManager.find(Book.class, expected.getId());
		assertEquals(actual, expected);
	}

	@Test
	public void delete() {
		Book expected = createAndPersistBook("Book", "Thriller", "Steven King");
		Book expected1 = createAndPersistBook("Book 1", "Thriller", "Steven King");
		Book actual = BookDaoImpl.getById(expected.getId());
		assertEquals(actual, expected);
		try {
			BookDaoImpl.delete(expected);
		} catch (DBException e) {
			fail();
		}
		actual = testEntityManager.find(Book.class, expected.getId());
		assertNull(actual);
		actual = testEntityManager.find(Book.class, expected1.getId());
		assertEquals(actual, expected1);
	}

	@Test
	public void update() {
		String newTitle = "Book by Steven Not King";
		Book expected = new Book("Book by Steven King");
		BookDaoImpl.save(expected);
		Book actual = testEntityManager.find(Book.class, expected.getId());
		assertEquals(actual, expected);
		expected.setTitle(newTitle);
		BookDaoImpl.update(expected);
		actual = testEntityManager.find(Book.class, expected.getId());
		assertEquals(actual.getTitle(), expected.getTitle());
	}

	private Book createAndPersistBook(String bookStr, String genreStr, String authorStr) {
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
		testEntityManager.persistAndFlush(book);
		return book;
	}
}