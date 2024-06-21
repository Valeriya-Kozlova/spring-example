package hexlet.code.service;

import hexlet.code.dto.TaskDTO;
import hexlet.code.entity.Task;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.TaskMapper;
import hexlet.code.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskMapper taskMapperImpl;

    @Transactional
    public Task create(TaskDTO data) {
        var task = taskMapperImpl.map(data);
        return taskRepository.save(task);
    }

    @Transactional
    public Task update(long id, TaskDTO data) {
        var task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task has not found"));
        taskMapperImpl.update(data, task);
        return taskRepository.save(task);
    }
}
