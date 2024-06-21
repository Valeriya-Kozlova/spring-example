package hexlet.code.mapper;

import hexlet.code.dto.LabelDTO;
import hexlet.code.entity.Label;
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
public abstract class LabelMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    public abstract Label map(LabelDTO model);

    @InheritInverseConfiguration(name = "map")
    public abstract LabelDTO map(Label model);

    @InheritConfiguration
    public abstract void update(LabelDTO update, @MappingTarget Label destination);
}
