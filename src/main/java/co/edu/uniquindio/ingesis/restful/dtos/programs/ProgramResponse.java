package co.edu.uniquindio.ingesis.restful.dtos.programs;

import co.edu.uniquindio.ingesis.restful.domain.Type;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ProgramResponse(
        Long id,
        @NotNull
        @NotBlank
        String title,
        @NotBlank
        String description,
        @NotBlank
        String code,
        LocalDate creationDate,
        LocalDate modificationDate,
        Type type,
        @NotNull Long userId
) {
}
