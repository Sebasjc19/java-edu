package co.edu.uniquindio.ingesis.restful.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TokenDTO(
        @NotBlank
        @NotNull
        String token
) {
}
