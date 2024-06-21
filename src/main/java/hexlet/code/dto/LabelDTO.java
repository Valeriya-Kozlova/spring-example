package hexlet.code.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.openapitools.jackson.nullable.JsonNullable;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LabelDTO {

    private Long id;

    private JsonNullable<String> name;

    private LocalDate createdAt;
}
