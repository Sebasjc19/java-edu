package co.edu.uniquindio.ingesis.restful.dtos.usuarios;

import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.Optional;

public record UserUpdateRequest(
        /* no se toman en cuenta variables no actualizables tales como
        la fecha de nacimiento o el rol
         */
        @NotBlank(message = "El campo es requerido")
        @Size(max = 100,message = "No debe exceder los 100 caracteres")
        Optional<String> username,
        @NotBlank(message = "El campo es requerido")
        @Email(message = "Debe ser un email válido")
        Optional<String> email,
        @NotBlank(message = "El campo es requerido")
        @Pattern(regexp = "^(?=.*\\\\d)(?=.*[a-z])(?=.*[A-Z]).*$",message = "Debe contener a")
        @Size(min = 8,message = "La longitud mínima es 8")
        Optional<String> password
) {
}
