package hexlet.code.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.entity.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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


public class TaskStatusesControllerTest extends BaseControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper om;
    @Autowired
    private TaskStatusRepository taskStatusRepository;
    private TaskStatus testTaskStatus;

    @BeforeEach
    public void beforeEach() {
        testTaskStatus = Instancio.of(TaskStatus.class)
                .ignore(Select.field(TaskStatus.class, "id"))
                .create();
        taskStatusRepository.save(testTaskStatus);
    }

    @Test
    public void testIndex() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/task_statuses").with(jwt()))
                .andExpect(status().isOk()).andReturn();
        List list = om.readValue(mvcResult.getResponse().getContentAsString(), List.class);
        assertFalse(list.isEmpty());
    }

    @Test
    public void testShow() throws Exception {
        var request = get("/api/task_statuses/" + testTaskStatus.getId()).with(jwt());
        MvcResult mvcResult = mockMvc.perform(request)
                .andExpect(status().isOk()).andReturn();
        var body = getBody(mvcResult, om);
        assertEquals(testTaskStatus.getSlug(), body.get("slug"));
        assertEquals(testTaskStatus.getName(), body.get("name"));
    }

    @Test
    public void testCreate() throws Exception {
        var data = Instancio.of(TaskStatus.class)
                .ignore(Select.field(TaskStatus.class, "id"))
                .create();

        var request = post("/api/task_statuses").with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));
        mockMvc.perform(request)
                .andExpect(status().isCreated());

        var task = taskStatusRepository.findBySlug(data.getSlug()).orElseThrow();
        assertEquals(data.getName(), task.getName());
        assertEquals(data.getSlug(), task.getSlug());
    }

    @Test
    public void testUpdate() throws Exception {
        var data = Instancio.of(TaskStatus.class)
                .ignore(Select.field(TaskStatus.class, "id"))
                .create();

        var request = put("/api/task_statuses/" + testTaskStatus.getId()).with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));
        mockMvc.perform(request)
                .andExpect(status().isOk());

        var task = taskStatusRepository.findBySlug(data.getSlug()).orElseThrow();
        assertEquals(data.getName(), task.getName());
        assertEquals(data.getSlug(), task.getSlug());
    }

    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(delete("/api/task_statuses/" + testTaskStatus.getId()).with(jwt()))
                .andExpect(status().isNoContent());
    }
}

