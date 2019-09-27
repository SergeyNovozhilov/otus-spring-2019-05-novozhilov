package ru.otus.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.otus.Util.BookSaver;
import ru.otus.Util.Converter;
import ru.otus.managers.BookManager;

import java.util.Collections;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest
public class BookControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookManager bookManager;
    @MockBean
    private Converter converter;
    @MockBean
    private BookSaver saver;

    @Test
    public void shouldReturnDefaultMessage() throws Exception {
        this.mockMvc.perform(get("/")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("Welcome to online library")));
    }

    @Test
    public void shouldReturnBooksPage() throws Exception {
        when(bookManager.get(null, null, null)).thenReturn(Collections.EMPTY_LIST);
        this.mockMvc.perform(get("/books")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("Books")));
    }

    @Test
    public void shouldReturnCreatePage() throws Exception {
        when(bookManager.get(null, null, null)).thenReturn(Collections.EMPTY_LIST);
        this.mockMvc.perform(get("/books")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("Create new book")));
    }

}
