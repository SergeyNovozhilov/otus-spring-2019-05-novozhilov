package ru.otus.managers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;
import ru.otus.dao.GenreDao;
import ru.otus.domain.Genre;
import ru.otus.exceptions.DBException;
import ru.otus.exceptions.NotFoundException;

import java.util.Collection;
import java.util.Collections;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class GenreManagerTest {
	@MockBean
	private GenreDao genreDao;

	@Configuration
	static class GenreManagerConfiguration {
		@Autowired
		private GenreDao genreDao;

		@Bean
		public GenreManager getGenreManager() {
			return new GenreManager(genreDao);
		}
	}


	@Autowired
	private GenreManager underTest;

	private String genreName = "Genre";
	private Genre expected;


	@Before
	public void setUp() {
		expected = new Genre(genreName);
	}

	@Test
	public void createTest() {
		Genre actual = underTest.create(genreName);
		assertEquals(actual, expected);
	}

	@Test
	public void getByNameTest() {
		try {
			when(genreDao.getByName(genreName)).thenReturn(expected);
			Collection<Genre> actual = underTest.get(genreName, "", "");
			assertTrue(actual.contains(expected));
		} catch (NotFoundException e) {
			fail();
		}
	}

	@Test
	public void getByBookTest() {
		String title = "Book";
		try {
			when(genreDao.getByBook(title)).thenReturn(expected);
			Collection<Genre> actual = underTest.get("", title, "");
			assertTrue(actual.contains(expected));
		} catch (NotFoundException e) {
			fail();
		}
	}

	@Test
	public void getByAuthorTest() {
		String author = "Author";
		try {
			when(genreDao.getByAuthor(author)).thenReturn(Collections.singleton(expected));
			Collection<Genre> actual = underTest.get("", "", author);
			assertTrue(actual.contains(expected));
		} catch (NotFoundException e) {
			fail();
		}
	}

	@Test
	public void updateTest() {
		underTest.update(expected);
		verify(genreDao).update(expected);
	}


	@Test
	public void deleteTest() {
		try {
			underTest.delete(expected);
			verify(genreDao).delete(expected);
		} catch (DBException e) {
			e.printStackTrace();
		}
	}
}