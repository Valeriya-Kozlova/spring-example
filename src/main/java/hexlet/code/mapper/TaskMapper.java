package hexlet.code.mapper;

import hexlet.code.dto.TaskDTO;
import hexlet.code.entity.Label;
import hexlet.code.entity.Task;
import hexlet.code.entity.TaskStatus;
import hexlet.code.entity.User;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(
        uses = {JsonNullableMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING
)
public abstract class TaskMapper {
    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private UserRepository userRepository;

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(source = "taskStatusSlug", target = "taskStatus")
    @Mapping(source = "assigneeId", target = "assignee")
    @Mapping(source = "taskLabelIds", target = "labels")
    public abstract Task map(TaskDTO model);

    @InheritInverseConfiguration(name = "map")
    @Mapping(source = "taskStatus.slug", target = "taskStatusSlug")
    @Mapping(source = "assignee.id", target = "assigneeId")
    @Mapping(source = "labels", target = "taskLabelIds")
    public abstract TaskDTO map(Task model);

    @InheritConfiguration
    public abstract void update(TaskDTO update, @MappingTarget Task destination);

    public TaskStatus toEntity(String slug) {
        return taskStatusRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Task Status has not found"));
    }

    public Set<Label> toEntity(Set<Long> labelIds) {
        if (labelIds == null) {
            return null;
        }
        return labelIds.stream()
                .map(labelId -> labelRepository.findById(labelId)
                        .orElseThrow(() -> new ResourceNotFoundException("Label with id: " + labelId + " not found")))
                .collect(Collectors.toSet());
    }

    public User toEntity(Long id) {
        if (id == null) {
            return null;
        }
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User has not found"));
    }

    public Set<Long> toDto(Set<Label> labels) {
        return labels.stream().map(Label::getId).collect(Collectors.toSet());
    }
}

