package hexlet.code.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.UserDTO;
import hexlet.code.entity.User;
import hexlet.code.repository.UserRepository;
import hexlet.code.util.ModelGenerator;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class UsersControllerTest extends BaseControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper om;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelGenerator modelGenerator;

    private User testUser;

    private SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor token;

    @BeforeEach
    public void beforeEach() {
        testUser = Instancio.of(modelGenerator.getUserModel())
                .create();
        userRepository.save(testUser);
        token = jwt().jwt(builder -> builder.subject(testUser.getEmail()));
    }

    @Test
    public void testIndex() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/users").with(token))
                .andExpect(status().isOk()).andReturn();
        List list = om.readValue(mvcResult.getResponse().getContentAsString(), List.class);
        assertFalse(list.isEmpty());
    }

    @Test
    void testCreate() throws Exception {
        var data = Instancio.of(modelGenerator.getUserModel())
                .create();

        var request = post("/api/users")
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));
        mockMvc.perform(request)
                .andExpect(status().isCreated());
        User user = userRepository.findByEmail(data.getEmail()).orElseThrow();
        assertEquals(data.getFirstName(), user.getFirstName());
    }

    @Test
    public void testShow() throws Exception {
        var request = get("/api/users/" + testUser.getId()).with(token);
        MvcResult mvcResult = mockMvc.perform(request)
                .andExpect(status().isOk()).andReturn();
        var body = getBody(mvcResult, om);
        assertEquals(testUser.getFirstName(), body.get("firstName"));
        assertEquals(testUser.getEmail(), body.get("email"));
    }

    @Test
    public void testUpdate() throws Exception {
        var data = new UserDTO();
        data.setFirstName(JsonNullable.of("New name"));

        var json = om.writeValueAsString(data);
        var request = put("/api/users/" + testUser.getId()).with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);
        mockMvc.perform(request)
                .andExpect(status().isOk());
        User user = userRepository.findByEmail(testUser.getEmail()).orElseThrow();
        assertEquals(data.getFirstName().get(), user.getFirstName());
    }

    @Test
    public void testUpdateAnother() throws Exception {
        var data = new UserDTO();
        data.setFirstName(JsonNullable.of("New name"));

        var json = om.writeValueAsString(data);
        var request = put("/api/users/" + 1).with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);
        mockMvc.perform(request)
                .andExpect(status().isForbidden());
    }

    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(delete("/api/users/" + testUser.getId()).with(token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteAnother() throws Exception {
        mockMvc.perform(delete("/api/users/" + 1).with(token))
                .andExpect(status().isForbidden());
    }
}


