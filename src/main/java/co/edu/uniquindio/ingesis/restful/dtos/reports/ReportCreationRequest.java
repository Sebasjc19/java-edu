package co.edu.uniquindio.ingesis.restful.dtos.reports;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ReportCreationRequest(
        @NotBlank
        String content,
        @NotNull
        Long groupId,
        @NotNull
        Long professorId
) {
}
