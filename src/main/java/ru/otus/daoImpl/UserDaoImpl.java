package ru.otus.daoImpl;

import org.springframework.stereotype.Repository;
import ru.otus.dao.UserDao;
import ru.otus.domain.User;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Repository
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public User findByUsername(String username) {
        try {
            TypedQuery<User> query = em.createQuery("select u from User u where u.username = :username", User.class);
            query.setParameter("username", username);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return  null;
        }
    }

    @Override
    public void addUser(User user) {
        em.persist(user);
        em.flush();
    }
}
