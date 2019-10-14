package ru.otus.integration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import ru.otus.daoImpl.UserDaoImpl;
import ru.otus.domain.User;

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
        User expected = new User();
        expected.setUsername("user");
        expected.setPassword("password");
        testEntityManager.persistAndFlush(expected);
        User actual = underTest.findByUsername(expected.getUsername());
        assertEquals(actual, expected);
    }

    @Test
    public void addUser() {
        User expected = new User();
        expected.setUsername("user");
        expected.setPassword("password");
        testEntityManager.persistAndFlush(expected);
        underTest.addUser(expected);
        User actual = testEntityManager.find(User.class, expected.getId());
        assertEquals(actual, expected);
    }
}
