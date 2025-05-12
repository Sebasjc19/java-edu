package co.edu.uniquindio.ingesis.restful.dtos.groups;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AddStudentToGroupRequest(
        @NotNull
        @NotBlank
        Long studentId
) {
}
