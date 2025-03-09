package co.edu.uniquindio.ingesis.restful.dtos.usuarios;

import co.edu.uniquindio.ingesis.restful.domain.Role;
import co.edu.uniquindio.ingesis.restful.domain.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.Collection;

public record UserResponse(
        @NotBlank
        String username,

        @NotBlank
        @Email
        String email,

        @NotBlank
        @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).*$",
                message = "La contraseña debe tener al menos una mayúscula, una minúscula y un número.")
        String password,

        @NotBlank(message = "El número de identificación no puede ser nulo")
        String identificationNumber,

        Status status,

        @NotNull(message = "La fecha no puede ser nula")
        @PastOrPresent(message = "La fecha no puede ser futura")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate birthDate,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate registrationDate,

        @NotNull
        Role role,

        @NotNull
        Long idGroup) {
}