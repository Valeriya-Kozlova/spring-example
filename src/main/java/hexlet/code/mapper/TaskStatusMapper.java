package hexlet.code.mapper;

import hexlet.code.dto.TaskStatusDTO;
import hexlet.code.entity.TaskStatus;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;


@Mapper(
        uses = {JsonNullableMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING
)
public abstract class TaskStatusMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    public abstract TaskStatus map(TaskStatusDTO model);

    @InheritInverseConfiguration(name = "map")
    public abstract TaskStatusDTO map(TaskStatus model);

    @InheritConfiguration
    public abstract void update(TaskStatusDTO update, @MappingTarget TaskStatus destination);
}


