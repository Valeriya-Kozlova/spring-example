package hexlet.code.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

import java.time.LocalDate;

@Getter
@Setter
public class UserDTO {
    private Long id;
    @NotBlank
    @Email
    private JsonNullable<String> email;

    private JsonNullable<String> firstName;

    private JsonNullable<String> lastName;
    @NotBlank
    @Size(min = 3, max = 100)
    private JsonNullable<String> password;

    private JsonNullable<String> encryptedPassword;

    private LocalDate createdAt;

}
