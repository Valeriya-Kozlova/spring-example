package hexlet.code.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.entity.Label;
import hexlet.code.repository.LabelRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Map;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class LabelsControllerTest extends BaseControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper om;
    @Autowired
    private LabelRepository labelRepository;

    private Label testLabel;

    @BeforeEach
    public void beforeEach() {
        testLabel = Instancio.of(Label.class)
                .ignore(field(Label.class, "id"))
                .create();
        labelRepository.save(testLabel);
    }

    @Test
    public void testIndex() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/labels").with(jwt()))
                .andExpect(status().isOk()).andReturn();
        List list = om.readValue(mvcResult.getResponse().getContentAsString(), List.class);
        assertFalse(list.isEmpty());
    }

    @Test
    public void testShow() throws Exception {
        var request = get("/api/labels/" + testLabel.getId()).with(jwt());
        MvcResult mvcResult = mockMvc.perform(request)
                .andExpect(status().isOk()).andReturn();
        Map<String, Object> body = getBody(mvcResult, om);
        assertEquals(body.get("name"), testLabel.getName());
    }


    @Test
    public void testCreate() throws Exception {
        var data = Instancio.of(Label.class)
                .ignore(field(Label.class, "id"))
                .create();

        var request = post("/api/labels").with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));
        MvcResult mvcResult = mockMvc.perform(request)
                .andExpect(status().isCreated()).andReturn();

        var label = labelRepository.findByName(data.getName());
        assertEquals(data.getName(), label.orElseThrow().getName());
    }

    @Test
    public void testUpdate() throws Exception {
        var data = Instancio.of(Label.class)
                .ignore(field(Label.class, "id"))
                .create();

        var request = put("/api/labels/" + testLabel.getId()).with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));
        mockMvc.perform(request)
                .andExpect(status().isOk());
        var label = labelRepository.findByName(data.getName());
        assertEquals(data.getName(), label.orElseThrow().getName());
    }

    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(delete("/api/labels/" + testLabel.getId()).with(jwt()))
                .andExpect(status().isNoContent());
    }
}


