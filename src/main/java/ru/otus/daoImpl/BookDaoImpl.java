package ru.otus.daoImpl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.dao.BookDao;
import ru.otus.domain.Book;
import ru.otus.exceptions.DBException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.*;

@Repository
public class BookDaoImpl implements BookDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Collection<Book> getAll() {
        try {
            TypedQuery<Book> query = em.createQuery("select distinct b from Book b left join fetch b.authors a", Book.class);
            Collection<Book> books = query.getResultList();
            return books;
        } catch (Exception e) {
            return Collections.EMPTY_SET;
        }
    }

    @Override
    public Collection<Book> getByTitle(String title) {
        try {
            TypedQuery<Book> query = em.createQuery("select distinct b from Book b left join fetch b.authors a where b.title = : title",
                    Book.class);
            query.setParameter("title", title);
            return query.getResultList();
        }  catch (Exception e) {
            return Collections.EMPTY_SET;
        }
    }

    @Override
    public Book getById(UUID id) {
        try {
            return em.find(Book.class, id);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Collection<Book> getByAuthor(String author) {
        try {
            TypedQuery<Book> query = em.createQuery("select distinct b from Book b left join b.authors a where a.name = :author", Book.class);
            query.setParameter("author", author);
            return query.getResultList();
        } catch (Exception e) {
            return Collections.EMPTY_SET;
        }
    }

    @Override
    public Collection<Book> getByGenre(String genre) {
        try {
            TypedQuery<Book> query = em.createQuery("SELECT distinct b FROM Book b left join fetch b.authors a join b.genre g where g.name = :genre", Book.class);
            query.setParameter("genre", genre);
            return query.getResultList();
        } catch (Exception e) {
            return Collections.EMPTY_SET;
        }
    }

    @Override
    @Transactional
    public Book save(Book book) {
        em.persist(book);
        em.flush();
        return book;
    }

    @Override
    @Transactional
    public void delete(Book book) throws DBException {
        try {
            em.remove(em.contains(book) ? book : em.merge(book));
            em.flush();
        } catch (Exception e) {
            throw new DBException("Cannot delete book");
        }
    }

    @Override
    @Transactional
    public Book update(Book book) {
        book = em.merge(book);
        em.flush();
        return book;
    }
}
