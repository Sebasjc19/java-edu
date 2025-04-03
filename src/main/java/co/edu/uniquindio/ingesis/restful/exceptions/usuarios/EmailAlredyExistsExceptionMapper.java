package co.edu.uniquindio.ingesis.restful.exceptions.usuarios;

import co.edu.uniquindio.ingesis.restful.dtos.MessageDTO;
import co.edu.uniquindio.ingesis.restful.dtos.usuarios.ErrorResponse;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;

/**
 * {@code EmailAlreadyExistsExceptionMapper} es un mapper de excepciones que captura
 * las excepciones {@link RuntimeException} relacionadas con intentos de registro
 * de emails duplicados y genera una respuesta HTTP adecuada.
 *
 * <p>Cuando se lanza una excepción que indica que un email ya está registrado,
 * este mapper intercepta la excepción y responde con un código HTTP 409 (Conflict)
 * junto con un cuerpo que detalla el error.</p>
 *
 * <h3>Ejemplo de respuesta:</h3>
 * <pre>
 * HTTP/1.1 409 Conflict
 * Content-Type: application/json
 *
 * {
 *   "error": "Email already exists",
 *   "message": "El email example@email.com ya está registrado."
 * }
 * </pre>
 */
public class EmailAlredyExistsExceptionMapper implements ExceptionMapper<RuntimeException> {

    @Override
    public Response toResponse(RuntimeException e) {
        ErrorResponse errorResponse = new ErrorResponse("Email already exists", e.getMessage());
        return Response.status(Response.Status.CONFLICT).entity(new MessageDTO<>(true, errorResponse)).build();
    }
}
