package ru.otus.integration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import ru.otus.daoImpl.UserDaoImpl;
import ru.otus.domain.Authority;
import ru.otus.domain.User;
import ru.otus.security.AuthorityType;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(UserDaoImpl.class)
public class UserDaoTests {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private UserDaoImpl underTest;

    @Test
    public void getByUserName() {
        Set<Authority> authorities = new HashSet<>();
        authorities.add(new Authority(AuthorityType.ROLE_USER));
        User expected = new User();
        expected.setUsername("user");
        expected.setAuthorities(authorities);

        User actual = underTest.findByUsername(expected.getUsername());
        assertEquals(actual.getUsername(), expected.getUsername());
        assertEquals(actual.getAuthorities().size(), 1);
        assertEquals(actual.getAuthorities().iterator().next().getName(), AuthorityType.ROLE_USER);
    }
}
