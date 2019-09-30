package ru.otus.dao;

import ru.otus.domain.Author;

import java.util.Collection;
import java.util.UUID;

public interface AuthorDao extends BaseDao{
	Collection<Author> getAll();
	Author getByName(String name);
	Author getById(UUID id);
	Collection<Author> getByBook(String book);
	Collection<Author> getByGenre(String genre);
	Author save(Author author);
	void delete(Author author);
	Author update(Author author);
}
