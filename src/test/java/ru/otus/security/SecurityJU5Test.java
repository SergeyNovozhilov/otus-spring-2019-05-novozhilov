package ru.otus.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.thymeleaf.util.StringUtils;
import ru.otus.controllers.BookController;
import ru.otus.dtos.BookDto;
import ru.otus.exceptions.NotFoundException;
import ru.otus.security.userDetails.UserDetailsService;
import ru.otus.util.BookService;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
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

    @BeforeEach
    public void setUp() throws NotFoundException {
        UUID id = UUID.fromString(STR_ID);
        BookDto book = new BookDto();
        book.setId(id);
        when(bookService.getBook(any(UUID.class))).thenReturn(book);
    }

    @ParameterizedTest
    @ValueSource(strings = {"/create", "/list", "/edit?id=", "/comment?id=", "/delete/"})
    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN", "ROLE_USER"}
    )
    public void testAdmin (String url) throws Exception {
        if (StringUtils.equalsIgnoreCase(url, "/create") || StringUtils.equalsIgnoreCase(url, "/list")) {
            mockMvc.perform(get(url))
                    .andExpect(status().isOk());
        } else if (StringUtils.equalsIgnoreCase(url, "/delete/")){
            mockMvc.perform(post(url + STR_ID))
                    .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/list"));
        } else {
            mockMvc.perform(post(url + STR_ID))
                    .andExpect(status().isOk());
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"/create", "/list", "/edit?id=", "/comment?id=", "/delete/"})
    @WithMockUser(
            username = "user",
            authorities = {"ROLE_USER"}
    )
    public void testUser (String url) throws Exception {
        if (StringUtils.equalsIgnoreCase(url, "/list")) {
            mockMvc.perform(get(url))
                    .andExpect(status().isOk());
        } else if (StringUtils.equalsIgnoreCase(url, "/comment?id=")) {
            mockMvc.perform(post(url + STR_ID))
                    .andExpect(status().isOk());
        } else if (StringUtils.equalsIgnoreCase(url, "/create")) {
            mockMvc.perform(get(url))
                    .andExpect(status().isForbidden());
        } else {
            mockMvc.perform(post(url + STR_ID))
                    .andExpect(status().isForbidden());
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"/create", "/list", "/edit?id=", "/comment?id=", "/delete/"})
    public void testNoUser (String url) throws Exception {
        if (StringUtils.equalsIgnoreCase(url, "/create") || StringUtils.equalsIgnoreCase(url, "/list")) {
            mockMvc.perform(get(url))
                    .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("http://localhost/login"));
        } else {
            mockMvc.perform(post(url + STR_ID))
                    .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("http://localhost/login"));
        }

    }
}
