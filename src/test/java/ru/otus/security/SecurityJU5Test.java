package ru.otus.security;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.controllers.BookController;
import ru.otus.security.userDetails.UserDetailsService;
import ru.otus.util.BookService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
public class SecurityJU5Test {

    @MockBean
    private BookService bookService;

    @MockBean
    private UserDetailsService userDetailsService;

    @Autowired
    private MockMvc mockMvc;

    private final String STR_ID = "5fc03087-d265-11e7-b8c6-83e29cd24f4c";

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN", "ROLE_USER"}
    )
    @ParameterizedTest
    @ValueSource(strings = {"/create", "/list"}) // , "/edit?id=", "/comment?id=", "/delete/"
    public void test (String url) throws Exception {
        mockMvc.perform(get(url))
                .andExpect(status().isOk());
    }

}
