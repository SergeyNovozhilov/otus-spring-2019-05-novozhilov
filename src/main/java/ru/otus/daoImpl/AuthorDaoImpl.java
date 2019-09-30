package ru.otus.daoImpl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.dao.AuthorDao;
import ru.otus.domain.Author;

import javax.persistence.*;
import java.util.*;

@Repository
public class AuthorDaoImpl implements AuthorDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Collection<Author> getAll() {
        try {
            TypedQuery<Author> query = em.createQuery(
                    "select a from Author a left join fetch a.books b", Author.class);
            return query.getResultList();
        } catch (NoResultException e) {
            return  Collections.EMPTY_SET;
        }
    }

    @Override
    public Author getByName(String name) {
        try {
            TypedQuery<Author> query = em.createQuery("select a from Author a where a.name = :name", Author.class);
            query.setParameter("name", name);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return  null;
        }
    }

    @Override
    public Author getById(UUID id) {
        return em.find(Author.class, id);
    }

    @Override
    public Collection<Author> getByBook(String book) {
        try {
            TypedQuery<Author> query = em.createQuery("select a from Author a join a.books b where b.title = :title", Author.class);
            query.setParameter("title", book);
            return query.getResultList();
        } catch (NoResultException e) {
            return  Collections.EMPTY_SET;
        }
    }

    @Override
    public Collection<Author> getByGenre(String genre) {
        try {
            Query query = em.createQuery("select distinct a from Author a join a.books b join Genre g on g.id=b.genre where a in (select a from Author a join a.books b join Genre g on g.id=b.genre where g.name=:genre)");
            query.setParameter("genre", genre);
            return query.getResultList();
        } catch (NoResultException e) {
            return  Collections.EMPTY_SET;
        }
    }

    @Override
    @Transactional
    public Author save(Author author) {
        em.persist(author);
        em.flush();
        return author;
    }

    @Override
    @Transactional
    public void delete(Author author) {
        em.remove(em.contains(author) ? author : em.merge(author));
        em.flush();
    }

    @Override
    @Transactional
    public Author update(Author author) {
        author = em.merge(author);
        em.flush();
        return author;
    }

}
