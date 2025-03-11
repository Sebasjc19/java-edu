package co.edu.uniquindio.ingesis.restful.dtos.programs;

import co.edu.uniquindio.ingesis.restful.domain.Type;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProgramCreationRequest(
        @NotNull
        @NotBlank(message = "El título del programa no puede estar vacío")
        String title,
        @NotBlank(message = "Debe proporcionar una descripción")
        String description,
        @NotBlank(message = "El código no puede estar vacío")
        String code,
        @NotNull(message = "Debe proporcionar el tipo del programa")
        Type type,
        @NotNull
        Long userId
) {
}
