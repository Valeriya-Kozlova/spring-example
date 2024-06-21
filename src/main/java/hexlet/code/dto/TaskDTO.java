package hexlet.code.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.openapitools.jackson.nullable.JsonNullable;

import java.time.LocalDate;
import java.util.Set;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class TaskDTO {

    private Long id;

    @JsonProperty("assignee_id")
    private JsonNullable<Long> assigneeId;

    @JsonProperty("title")
    private JsonNullable<String> name;

    @JsonProperty("content")
    private JsonNullable<String> description;

    @JsonProperty("status")
    private JsonNullable<String> taskStatusSlug;

    private JsonNullable<Set<Long>> taskLabelIds;

    private JsonNullable<Integer> index;

    private LocalDate createdAt;
}


