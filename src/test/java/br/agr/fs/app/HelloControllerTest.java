package br.agr.fs.app;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

// Teste unitário puro (sem subir contexto Spring): rápido, roda no surefire.
class HelloControllerTest {

    private final MockMvc mvc = MockMvcBuilders.standaloneSetup(new HelloController()).build();

    @Test
    void helloReturnsMessage() throws Exception {
        mvc.perform(get("/hello"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("hello"));
    }
}
