package ru.otus.dao;

import ru.otus.domain.Book;
import ru.otus.exceptions.DBException;

import java.util.Collection;
import java.util.UUID;

public interface BookDao extends BaseDao{
	Collection<Book> getAll();
	Collection<Book> getByTitle(String title);
	Book getById(UUID id);
	Collection<Book> getByAuthor(String author);
	Collection<Book> getByGenre(String genre);
	Book save(Book book);
	void delete(Book book) throws DBException;
	Book update(Book book);
}
