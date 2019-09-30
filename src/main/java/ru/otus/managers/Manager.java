package ru.otus.managers;

import ru.otus.domain.Base;
import ru.otus.exceptions.DBException;
import ru.otus.exceptions.NotFoundException;

import java.util.Collection;

public interface Manager <T extends Base>{
    T create(String name);

    Collection<T> get(String arg0, String arg1, String arg2) throws NotFoundException;

    T update(T object);

    void delete(T object) throws DBException;
}
