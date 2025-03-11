package co.edu.uniquindio.ingesis.restful.dtos.comments;

import jakarta.validation.constraints.NotBlank;

public record UpdateCommentRequest(
        @NotBlank(message = "El comentario no puede estar vacío")
        String content
) {
}
