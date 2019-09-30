package ru.otus.dao;

import ru.otus.domain.Genre;
import ru.otus.exceptions.DBException;

import java.util.Collection;
import java.util.UUID;

public interface GenreDao extends BaseDao{
	Collection<Genre> getAll();
	Genre getByName(String name);
	Genre getById(UUID id);
	Collection<Genre> getByAuthor(String author);
	Genre getByBook(String book);
	Genre save(Genre genre);
	void delete(Genre genre) throws DBException;
	Genre update(Genre genre);
}
