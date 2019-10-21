package ru.otus.dao;

import ru.otus.domain.User;

public interface UserDao extends BaseDao {
    User findByUsername(String username);
    void addUser(User user);
}
