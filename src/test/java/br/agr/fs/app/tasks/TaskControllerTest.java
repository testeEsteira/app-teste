package br.agr.fs.app.tasks;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class TaskControllerTest {

    private final MockMvc mvc = MockMvcBuilders
            .standaloneSetup(new TaskController(new TaskService()))
            .setControllerAdvice(new ApiExceptionHandler())
            .build();

    private void create(String title) throws Exception {
        mvc.perform(post("/tasks").contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"" + title + "\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    void createReturns201() throws Exception {
        mvc.perform(post("/tasks").contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"estudar\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.done").value(false));
    }

    @Test
    void blankTitleReturns400() throws Exception {
        mvc.perform(post("/tasks").contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"  \"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void listAndGet() throws Exception {
        create("a");
        mvc.perform(get("/tasks")).andExpect(status().isOk()).andExpect(jsonPath("$[0].title").value("a"));
        mvc.perform(get("/tasks/1")).andExpect(status().isOk()).andExpect(jsonPath("$.title").value("a"));
    }

    @Test
    void completeAndDelete() throws Exception {
        create("a");
        mvc.perform(put("/tasks/1/done")).andExpect(status().isOk()).andExpect(jsonPath("$.done").value(true));
        mvc.perform(delete("/tasks/1")).andExpect(status().isNoContent());
        mvc.perform(get("/tasks/1")).andExpect(status().isNotFound());
    }

    @Test
    void unknownTaskReturns404() throws Exception {
        mvc.perform(get("/tasks/42"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Task 42 not found"));
    }
}
