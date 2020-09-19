package app.automs.automation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class SampleRecipeAutomationTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private SampleAutomation sampleRecipeAutomation;

    @Test
    public void querySampleMock() throws Exception {
        Mockito.when(sampleRecipeAutomation.run(Mockito.any()))
                .thenReturn("{\"response\":\"Não existem pendências para 01409606000148\"}");

        mvc.perform(MockMvcRequestBuilders.get("/api/automation/run/sefaz-go-pendencia/01409606000148"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json("{\"response\":\"Não existem pendências para 01409606000148\"}"));
    }

}
