package co.edu.uniquindio.ingesis.restful.dtos.comments;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

public record ObtainCommentsResponse(
        @NotBlank(message = "No se encuentra el contenido del comentario")
        String content,
        @NotBlank(message = "El comentario no posee una fecha de creación")
        @PastOrPresent(message = "La fecha no puede ser futura")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate creationDate,
        @NotNull(message = "El comentario no registra un id de profesor")
        @NotBlank
        Long professorId,
        @NotNull(message = "El comentario no registra un id de programa")
        @NotBlank
        Long programId
) {
}
