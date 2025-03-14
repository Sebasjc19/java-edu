package co.edu.uniquindio.ingesis.restful.dtos.usuarios;

import jakarta.validation.constraints.NotBlank;

public record TokenResponse(
        @NotBlank String token) {
}
