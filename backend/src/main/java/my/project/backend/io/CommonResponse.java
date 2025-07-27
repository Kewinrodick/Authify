package my.project.backend.io;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import my.project.backend.enumeration.ResponseStatus;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommonResponse {
    private ResponseStatus status;
    private String successMessage;
    private String errorMessage;
    private Object data;
    private int code;
}
