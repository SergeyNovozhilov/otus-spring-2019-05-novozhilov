package ru.otus.managers;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ru.otus.dao.GenreDao;
import ru.otus.domain.Genre;
import ru.otus.exceptions.DBException;
import ru.otus.exceptions.NotFoundException;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class GenreManager implements Manager<Genre> {
    private GenreDao genreDao;

    public GenreManager(GenreDao genreDao) {
        this.genreDao = genreDao;
    }

    @Override
    public Genre create(String name) {
        Genre genre = new Genre(name);
        genreDao.save(genre);
        return genre;
    }

    @Override
    public Collection<Genre> get(String name, String book, String author) throws NotFoundException {
        Collection<Genre> genres = new ArrayList<>();
        if (StringUtils.isBlank(name) && StringUtils.isBlank(author) && StringUtils.isBlank(book)) {
            genres = genreDao.getAll();
        } else {
            if (StringUtils.isNotBlank(name)) {
                Genre genre = genreDao.getByName(name);
                if (genre == null) {
                    throw new NotFoundException("Genre with name: " + name + " not found.");
                }
                genres.add(genre);
            } else if (StringUtils.isNotBlank(book)) {
                Genre genre = genreDao.getByBook(book);
                if (genre == null) {
                    throw new NotFoundException("Genre of book: " + name + " not found");
                }
                genres.add(genre);
            } else if (StringUtils.isNotBlank(author)) {
                genres.addAll(genreDao.getByAuthor(author));
                if (genres.isEmpty()) {
                    throw new NotFoundException("Genre of author: " + author + " not found");
                }
            }
        }
        if (genres.isEmpty()) {
            throw new NotFoundException("Genres not found");
        }
        return genres;
    }

    @Override
    public Genre update(Genre genre) {
        return genreDao.update(genre);
    }

    @Override
    public void delete(Genre genre) throws DBException {
        genreDao.delete(genre);
    }
}
