package hexlet.code.component;

import hexlet.code.dto.UserDTO;
import hexlet.code.entity.Label;
import hexlet.code.entity.TaskStatus;
import hexlet.code.entity.User;
import hexlet.code.mapper.UserMapper;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import org.apache.commons.text.CaseUtils;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class Initializer implements ApplicationRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private UserMapper userMapper;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        addModels();
    }

    private void addModels() {
        var userData = new UserDTO();
        var email = "test@test.com";
        userData.setEmail(JsonNullable.of(email));
        userData.setPassword(JsonNullable.of("test"));
        var user = userMapper.map(userData);
        Optional<User> byEmail = userRepository.findByEmail(email);
        if (!byEmail.isPresent()) {
            userRepository.save(user);
        }

        var labelNames = Arrays.asList("bug", "feature");
        var labels = labelNames.stream()
                .map(name -> {
                    var label = new Label();
                    label.setName(name);
                    labelRepository.save(label);
                    return label;
                }).toList();

        var statusNames = Arrays.asList("draft", "to_review", "to_be_fixed", "to_publish", "published");
        var taskStatuses = statusNames.stream()
                .map(name -> {
                    var taskStatus = new TaskStatus();
                    taskStatus.setSlug(name);
                    taskStatus.setName(CaseUtils.toCamelCase(name, true, '_'));
                    taskStatusRepository.save(taskStatus);
                    return taskStatus;
                }).collect(Collectors.toSet());

    }
}


