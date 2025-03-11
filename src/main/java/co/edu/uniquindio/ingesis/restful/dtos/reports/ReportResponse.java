package co.edu.uniquindio.ingesis.restful.dtos.reports;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ReportResponse(
        @NotBlank
        String content,
        LocalDate creationDate,
        @NotNull
        Long groupId,
        @NotNull
        Long professorId
) {
}
