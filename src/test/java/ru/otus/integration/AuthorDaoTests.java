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
import ru.otus.daoImpl.AuthorDaoImpl;
import ru.otus.daoImpl.BookDaoImpl;
import ru.otus.domain.Author;
import ru.otus.domain.Book;
import ru.otus.domain.Genre;

import java.sql.SQLException;
import java.util.*;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({AuthorDaoImpl.class, BookDaoImpl.class})
public class AuthorDaoTests {

	@Autowired
	private TestEntityManager testEntityManager;

	@Autowired
	private AuthorDaoImpl AuthorDaoImpl;

	@Autowired
	private BookDaoImpl BookDaoImpl;

	@Test
	public void getAll() {
		List<Author> expected = Arrays.asList(new Author("Jack London"), new Author("Mark Twain"));
		expected.forEach(e -> testEntityManager.persist(e));
		testEntityManager.flush();
		List<Author> actual = new ArrayList(AuthorDaoImpl.getAll());
		assertTrue(expected.size() + 1 == actual.size()); // + 1 book from data.sql
		expected.forEach( e -> {
			Author a = actual.stream().filter(x -> x.getId().equals(e.getId())).findFirst().orElse(null);
			if (a == null) {
				fail();
			}
			assertEquals(e.getName(), a.getName());
		});
	}

	@Test
	public void getByName() {
		Author expected = new Author("Steven King");
		testEntityManager.persistAndFlush(expected);
		Author actual = AuthorDaoImpl.getByName(expected.getName());
		assertEquals(expected, actual);
	}

	@Test
	public void getById() {
		Author expected = new Author("Steven King");
		testEntityManager.persistAndFlush(expected);
		Author actual = AuthorDaoImpl.getById(expected.getId());
		assertEquals(actual, expected);
	}

	@Test
	public void getByBook() throws SQLException {
		String bookTitle = "Book by Jack London";
		String authorName = "Jack London";
		createAndPersistBook(bookTitle, "Genre", authorName);
		Collection<Author> actual = AuthorDaoImpl.getByBook(bookTitle);
		assertTrue(actual.size() == 1);
		assertEquals(actual.iterator().next().getName(), "Jack London");
		assertTrue(true);
	}

	@Test
	public void getByGenre() throws SQLException {
		String genreName = "Thriller";
		String authorName = "Steven King";
		createAndPersistBook("Book", genreName, authorName);
		Collection<Author> actual = AuthorDaoImpl.getByGenre(genreName);
		assertTrue(actual.size() == 1);
		assertNotNull(actual.stream().map(Author::getName).filter(name -> name.equals(authorName)).findAny().orElse(null));
	}

	@Test
	public void save() {
		Author expected = new Author("Steven King");
		AuthorDaoImpl.save(expected);
		Author actual = testEntityManager.find(Author.class, expected.getId());
		assertEquals(actual, expected);
	}

	@Test
	public void delete() {
		Author expected = new Author("Steven King");
		testEntityManager.persistAndFlush(expected);
		Author actual = AuthorDaoImpl.getById(expected.getId());
		assertEquals(actual, expected);
		AuthorDaoImpl.delete(expected);
		actual = testEntityManager.find(Author.class, expected.getId());
		assertNull(actual);
	}

	@Test
	public void update() {
		String newName = "Steven Not King";
		Author expected = new Author("Steven King");
		AuthorDaoImpl.save(expected);
		Author actual = testEntityManager.find(Author.class, expected.getId());
		assertEquals(actual, expected);
		expected.setName(newName);
		AuthorDaoImpl.update(expected);
		actual = testEntityManager.find(Author.class, expected.getId());
		assertEquals(actual.getName(), expected.getName());
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
		testEntityManager.persist(book);
		testEntityManager.flush();
		return book;
	}
}
