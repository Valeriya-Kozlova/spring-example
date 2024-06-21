package hexlet.code.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.TaskDTO;
import hexlet.code.entity.Task;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.util.ModelGenerator;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class TasksControllerTest extends BaseControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper om;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private TaskStatusRepository taskStatusRepository;
    @Autowired
    private LabelRepository labelRepository;
    @Autowired
    private ModelGenerator modelGenerator;
    private Task testTask;

    @BeforeEach
    public void beforeEach() {
        var taskStatus = taskStatusRepository.findBySlug("draft").orElseThrow();
        testTask = Instancio.of(modelGenerator.getTaskModel())
                .set(Select.field(Task::getAssignee), null)
                .create();
        testTask.setTaskStatus(taskStatus);
        testTask.setLabels(Set.of());
        taskRepository.save(testTask);
    }

    @Test
    public void testIndex() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/tasks").with(jwt()))
                .andExpect(status().isOk()).andReturn();
        List list = om.readValue(mvcResult.getResponse().getContentAsString(), List.class);
        assertFalse(list.isEmpty());
    }

    @Test
    public void testShow() throws Exception {
        var request = get("/api/tasks/" + testTask.getId()).with(jwt());
        MvcResult mvcResult = mockMvc.perform(request)
                .andExpect(status().isOk()).andReturn();
        var body = getBody(mvcResult, om);
        assertEquals(testTask.getName(), body.get("title"));
        assertEquals(testTask.getDescription(), body.get("content"));
    }

    @Test
    public void testCreate() throws Exception {
        var taskStatus = taskStatusRepository.findBySlug("draft").get();
        var label = labelRepository.findByName("feature").get();
        var data = new TaskDTO();
        data.setName(JsonNullable.of("New Task Name"));
        data.setTaskStatusSlug(JsonNullable.of(taskStatus.getSlug()));
        data.setTaskLabelIds(JsonNullable.of(Set.of(label.getId())));

        var json = om.writeValueAsString(data);
        var request = post("/api/tasks").with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);
        mockMvc.perform(request)
                .andExpect(status().isCreated());

        var task = taskRepository.findByName(data.getName().get()).orElseThrow();
        assertEquals(task.getName(), data.getName().get());
        assertEquals(taskStatus.getSlug(), data.getTaskStatusSlug().get());
    }

    @Test
    public void testUpdate() throws Exception {
        var taskStatus = taskStatusRepository.findBySlug("draft").get();
        var label = labelRepository.findByName("feature").get();
        var data = new TaskDTO();
        data.setName(JsonNullable.of("New Task Name111"));
        data.setTaskStatusSlug(JsonNullable.of(taskStatus.getSlug()));
        data.setTaskLabelIds(JsonNullable.of(Set.of(label.getId())));

        var request = put("/api/tasks/" + testTask.getId()).with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));
        mockMvc.perform(request)
                .andExpect(status().isOk());

        var task = taskRepository.findByName(data.getName().get()).orElseThrow();
        assertEquals(task.getName(), data.getName().get());
        assertEquals(taskStatus.getSlug(), data.getTaskStatusSlug().get());
    }

    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(delete("/api/tasks/" + testTask.getId()).with(jwt()))
                .andExpect(status().isNoContent());
    }
}


