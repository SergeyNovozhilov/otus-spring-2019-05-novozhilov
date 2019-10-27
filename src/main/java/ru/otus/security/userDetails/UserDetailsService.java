package ru.otus.security.userDetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.otus.dao.UserDao;
import ru.otus.domain.Authority;
import ru.otus.domain.User;
import ru.otus.security.AuthorityType;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private UserDao userDao;

    @Autowired
    public UserDetailsService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = Optional.of(userDao.findByUsername(username));
        User user = userOptional.orElseThrow(() -> new UsernameNotFoundException(username));

        return new UserPrincipal(user);
    }
}
