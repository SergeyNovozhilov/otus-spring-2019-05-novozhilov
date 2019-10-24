package ru.otus.security;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(BookController.class)
public class SecurityTest {

    @MockBean
    private BookService bookService;

    @MockBean
    private UserDetailsService userDetailsService;

    @Autowired
    private MockMvc mockMvc;

    private final String STR_ID = "5fc03087-d265-11e7-b8c6-83e29cd24f4c";

    @Before
    public void setUp() throws NotFoundException {
        UUID id = UUID.fromString(STR_ID);
        BookDto book = new BookDto();
        book.setId(id);
        when(bookService.getBook(any(UUID.class))).thenReturn(book);
    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN", "ROLE_USER"}
    )
    @Test
    public void testCreateOnAdmin() throws Exception {
        mockMvc.perform(get("/create"))
                .andExpect(status().isOk());
    }

    @WithMockUser(
            username = "user",
            authorities = {"ROLE_USER"}
    )
    @Test
    public void testCreateOnUser() throws Exception {
        mockMvc.perform(get("/create"))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN", "ROLE_USER"}
    )
    @Test
    public void testListOnAdmin() throws Exception {
        mockMvc.perform(get("/list"))
                .andExpect(status().isOk());
    }

    @WithMockUser(
            username = "user",
            authorities = {"ROLE_USER"}
    )
    @Test
    public void testListOnUser() throws Exception {
        mockMvc.perform(get("/list"))
                .andExpect(status().isOk());
    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN", "ROLE_USER"}
    )
    @Test
    public void testEditOnAdmin() throws Exception {

        mockMvc.perform(post("/edit?id=" + STR_ID))
                .andExpect(status().isOk());
    }

    @WithMockUser(
            username = "user",
            authorities = {"ROLE_USER"}
    )
    @Test
    public void testEditOnUser() throws Exception {

        mockMvc.perform(post("/edit?id=" + STR_ID))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN", "ROLE_USER"}
    )
    @Test
    public void testCommentOnAdmin() throws Exception {

        mockMvc.perform(post("/comment?id=" + STR_ID))
                .andExpect(status().isOk());
    }

    @WithMockUser(
            username = "user",
            authorities = {"ROLE_USER"}
    )
    @Test
    public void testCommentOnUser() throws Exception {

        mockMvc.perform(post("/comment?id=" + STR_ID))
                .andExpect(status().isOk());
    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN", "ROLE_USER"}
    )
    @Test
    public void testDeleteOnAdmin() throws Exception {

        mockMvc.perform(post("/delete/" + STR_ID))
                .andExpect(status().is3xxRedirection());
    }

    @WithMockUser(
            username = "user",
            authorities = {"ROLE_USER"}
    )
    @Test
    public void testDeleteOnUser() throws Exception {

        mockMvc.perform(post("/delete/" + STR_ID))
                .andExpect(status().isForbidden());
    }

}
