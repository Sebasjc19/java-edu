package co.edu.uniquindio.ingesis.restful.dtos.usuarios;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SesionUserRequest(
        @NotBlank @Email String email,
        @NotBlank String password
) {
}
