package co.edu.uniquindio.ingesis.restful.dtos.comments;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CommentResponse(
        @NotBlank
        String content,
        LocalDate creationDate,
        @NotNull
        @NotBlank
        Long professorId,
        @NotNull
        @NotBlank
        Long programId
) {
}
