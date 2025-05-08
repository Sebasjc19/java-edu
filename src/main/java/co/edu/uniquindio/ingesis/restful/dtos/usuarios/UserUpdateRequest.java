package co.edu.uniquindio.ingesis.restful.dtos.usuarios;

import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.Optional;

public record UserUpdateRequest(
        /* no se toman en cuenta variables no actualizables tales como
        la fecha de nacimiento o el rol
         */
        Optional<String> username,
        Optional<String> email,
        Optional<String> password
) {
}
