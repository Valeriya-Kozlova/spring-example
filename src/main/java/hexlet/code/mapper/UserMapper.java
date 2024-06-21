package hexlet.code.mapper;


import hexlet.code.dto.UserDTO;
import hexlet.code.entity.User;
import org.mapstruct.BeforeMapping;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;


@Mapper(
        uses = {JsonNullableMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING
)
public abstract class UserMapper {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JsonNullableMapper jsonNullableMapper;

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    // @Mapping(target = "password", ignore = true)
    @Mapping(target = "password", source = "encryptedPassword")
    public abstract User map(UserDTO model);

    @InheritInverseConfiguration(name = "map")
    @Mapping(target = "encryptedPassword", ignore = true)
    @Mapping(target = "password", ignore = true)
    public abstract UserDTO map(User model);

    @InheritConfiguration
    public abstract void update(UserDTO update, @MappingTarget User destination);

    @BeforeMapping
    public void encryptPassword(UserDTO data) {
        if (jsonNullableMapper.isPresent(data.getPassword())) {
            var password = data.getPassword().get();
            data.setEncryptedPassword(JsonNullable.of(passwordEncoder.encode(password)));
        }
    }
}

