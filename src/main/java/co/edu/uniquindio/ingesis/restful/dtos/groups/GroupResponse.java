package co.edu.uniquindio.ingesis.restful.dtos.groups;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record GroupResponse(
        @Size(min = 3, max = 20)
        String name,
        @NotNull
        String idProfessor
) {
}
