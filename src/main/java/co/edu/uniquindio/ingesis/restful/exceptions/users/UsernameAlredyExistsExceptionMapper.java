package co.edu.uniquindio.ingesis.restful.exceptions.users;

import co.edu.uniquindio.ingesis.restful.dtos.MessageDTO;
import co.edu.uniquindio.ingesis.restful.dtos.usuarios.ErrorResponse;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;

public class UsernameAlredyExistsExceptionMapper implements ExceptionMapper<RuntimeException> {
    @Override
    public Response toResponse(RuntimeException e) {
        ErrorResponse errorResponse = new ErrorResponse("username alredy exists", e.getMessage());
        return Response.status(Response.Status.CONFLICT).entity(new MessageDTO<>(true, errorResponse)).build();
    }
}
