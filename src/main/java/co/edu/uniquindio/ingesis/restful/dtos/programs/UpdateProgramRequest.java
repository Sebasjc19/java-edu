package co.edu.uniquindio.ingesis.restful.dtos.programs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateProgramRequest(
        @NotNull
        @NotBlank(message = "El título del programa no puede estar vacío")
        String title,
        @NotBlank(message = "Debe proporcionar una descripción")
        String description,
        @NotBlank(message = "El código no puede estar vacío")
        String code,
        @NotNull
        Long userId
) {
}
