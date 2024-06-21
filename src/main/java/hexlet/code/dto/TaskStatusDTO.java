package hexlet.code.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.openapitools.jackson.nullable.JsonNullable;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskStatusDTO {

    private Long id;

    @NotBlank
    private JsonNullable<String> name;

    @NotBlank
    private JsonNullable<String> slug;

    private LocalDate createdAt;
}


