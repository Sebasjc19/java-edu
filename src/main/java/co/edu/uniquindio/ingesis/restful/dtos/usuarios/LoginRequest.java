package co.edu.uniquindio.ingesis.restful.dtos.usuarios;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoginRequest(
        @NotBlank String email,
        @NotNull String password
) {
}
