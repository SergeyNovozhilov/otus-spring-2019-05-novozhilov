package ru.otus.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.Util.BookService;
import ru.otus.dtos.BookDto;

import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@WebMvcTest
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BookService bookService;

    @Test
    public void shouldReturnEditPage() throws Exception {
        String  strId = "5fc03087-d265-11e7-b8c6-83e29cd24f4c";
        UUID id = UUID.fromString(strId);
        BookDto book = new BookDto();
        when(bookService.getBook(id)).thenReturn(book);
        this.mockMvc.perform(get("/books/" + strId)).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("edit"));
    }
}
