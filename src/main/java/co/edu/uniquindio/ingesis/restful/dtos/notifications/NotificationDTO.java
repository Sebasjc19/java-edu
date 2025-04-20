package co.edu.uniquindio.ingesis.restful.dtos.notifications;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record NotificationDTO(
        @NotNull @NotBlank @Email
        String destinatario,
        @NotNull @Size(min = 1, max = 20)
        String asunto,
        String mensaje,
        @NotNull @NotBlank
        long userId) {
}
