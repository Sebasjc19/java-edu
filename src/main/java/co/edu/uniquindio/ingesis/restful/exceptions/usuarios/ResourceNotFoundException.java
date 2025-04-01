package co.edu.uniquindio.ingesis.restful.exceptions.usuarios;

import co.edu.uniquindio.ingesis.restful.dtos.usuarios.ErrorResponse;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;

public class ResourceNotFoundException extends Throwable implements ExceptionMapper<RuntimeException> {
    @Override
    public Response toResponse(RuntimeException e) {
        ErrorResponse errorResponse = new ErrorResponse("Resource not found", e.getMessage());
        return Response.status(Response.Status.CONFLICT).entity(errorResponse).build();
    }
}
