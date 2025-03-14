package co.edu.uniquindio.ingesis.restful.dtos;

public record MessageDTO<T>(
        boolean error,
        T respuesta
) {
}
