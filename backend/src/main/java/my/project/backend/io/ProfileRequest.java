package my.project.backend.io;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileRequest {

    @NotBlank(message = "Name should not be empty")
    private String name;
    @NotNull(message = "Email should not be null")
    @Email(message = "Enter valid email")
    private String email;
    @Size(min = 6,message = "Password must be 6 characters")
    private String password;

}
