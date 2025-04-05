package co.edu.uniquindio.ingesis.restful.exceptions.users.mappers;

import co.edu.uniquindio.ingesis.restful.dtos.MessageDTO;
import co.edu.uniquindio.ingesis.restful.dtos.usuarios.ErrorResponse;
import co.edu.uniquindio.ingesis.restful.exceptions.users.implementations.UsernameAlreadyExistsException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class UsernameAlredyExistsExceptionMapper implements ExceptionMapper<UsernameAlreadyExistsException> {
    @Override
    public Response toResponse(UsernameAlreadyExistsException e) {
        ErrorResponse errorResponse = new ErrorResponse("Username already exists", e.getMessage());
        return Response.status(Response.Status.CONFLICT)
                .entity(new MessageDTO<>(true, errorResponse))
                .build();
    }
}
