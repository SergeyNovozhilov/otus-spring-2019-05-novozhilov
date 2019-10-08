package ru.otus.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.Util.BookHelper;
import ru.otus.dtos.BookDto;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest
public class BookRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookHelper bookHelper;

    @Test
    public void getAllBooksIsCalledTest() throws Exception {
        this.mockMvc.perform(get("/books")).andDo(print()).andExpect(status().isOk());
        verify(bookHelper).getAllBooks();
    }

    @Test
    public void createPerformedAndSaveIsCalledTest() throws Exception {
        this.mockMvc.perform(post("/create").contentType(MediaType.APPLICATION_JSON).content(asJsonString(new BookDto()))).andDo(print()).andExpect(status().isOk());
        verify(bookHelper).save(any(), isA(BookDto.class));
    }

    @Test
    public void deleteIsCalledTest() throws Exception {
        String  strId = "5fc03087-d265-11e7-b8c6-83e29cd24f4c";
        UUID id = UUID.fromString(strId);
        this.mockMvc.perform(post("/delete/" + strId)).andDo(print()).andExpect(status().isFound());
        verify(bookHelper).delete(id);
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
