package ru.otus.daoImpl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.dao.GenreDao;
import ru.otus.domain.Genre;
import ru.otus.exceptions.DBException;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

@Repository
public class GenreDaoImpl implements GenreDao {
    @PersistenceContext
    private EntityManager em;

    @Override
    public Collection<Genre> getAll() {
        try {
            TypedQuery<Genre> query = em.createQuery("select g from Genre g", Genre.class);
            return query.getResultList();
        } catch (NoResultException e) {
            return  Collections.EMPTY_SET;
        }
    }

    @Override
    public Genre getByName(String name) {
        try {
            TypedQuery<Genre> query = em.createQuery("select g from Genre g where g.name = :name", Genre.class);
            query.setParameter("name", name);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return  null;
        }
    }

    @Override
    public Genre getById(UUID id) {
        try {
            return em.find(Genre.class, id);
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Collection<Genre> getByAuthor(String author) {
        try {
            TypedQuery<Genre> query = em.createQuery("SELECT b.genre FROM Book b JOIN b.authors a where a.name = :name", Genre.class);
            query.setParameter("name", author);
            return query.getResultList();
        } catch (NoResultException e) {
            return  Collections.EMPTY_SET;
        }
    }

    @Override
    public Genre getByBook(String book) {
        try {
            TypedQuery<Genre> query = em.createQuery("select g from Genre g JOIN Book b on b.genre=g.id where b.title = :title", Genre.class);
            query.setParameter("title", book);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    @Transactional
    public Genre save(Genre genre) {
        em.persist(genre);
        em.flush();
        return genre;
    }

    @Override
    @Transactional
    public void delete(Genre genre) throws DBException {
        try {
            em.remove(em.contains(genre) ? genre : em.merge(genre));
            em.flush();
        } catch (Exception e) {
            throw new DBException("Cannot delete genre");
        }
    }

    @Override
    @Transactional
    public Genre update(Genre genre) {
        genre  = em.merge(genre);
        em.flush();
        return genre;
    }
}
