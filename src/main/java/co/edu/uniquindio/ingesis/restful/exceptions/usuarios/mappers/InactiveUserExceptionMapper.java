package co.edu.uniquindio.ingesis.restful.exceptions.usuarios.mappers;

import co.edu.uniquindio.ingesis.restful.dtos.MessageDTO;
import co.edu.uniquindio.ingesis.restful.dtos.usuarios.ErrorResponse;
import co.edu.uniquindio.ingesis.restful.exceptions.usuarios.implementations.InactiveUserException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class InactiveUserExceptionMapper implements ExceptionMapper<InactiveUserException> {

    @Override
    public Response toResponse(InactiveUserException e) {
        ErrorResponse errorResponse = new ErrorResponse("Inactive user", e.getMessage());
        return Response.status(Response.Status.UNAUTHORIZED)
                .entity(new MessageDTO<>(true, errorResponse))
                .build();
    }
}
