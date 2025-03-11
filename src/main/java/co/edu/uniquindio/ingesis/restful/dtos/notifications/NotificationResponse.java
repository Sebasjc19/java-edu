package co.edu.uniquindio.ingesis.restful.dtos.notifications;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record NotificationResponse(
        @Size(min = 3, max = 20)
        String message,
        LocalDate sentDate,
        boolean read,
        @NotNull
        Long studentId
) {
}
