package co.edu.uniquindio.ingesis.restful.dtos.notifications;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record NotificationCreationRequest(
        @Size(min = 3, max = 20)
        String message,
        @NotNull
        Long studentId
) {
}
