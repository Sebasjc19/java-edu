package co.edu.uniquindio.ingesis.restful.exceptions.users.mappers;

import co.edu.uniquindio.ingesis.restful.dtos.MessageDTO;
import co.edu.uniquindio.ingesis.restful.dtos.usuarios.ErrorResponse;
import co.edu.uniquindio.ingesis.restful.exceptions.users.implementations.RoleNotAllowedException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;

public class RoleNotAllowedExceptionMapper implements ExceptionMapper<RoleNotAllowedException> {
    @Override
    public Response toResponse(RoleNotAllowedException e) {
        ErrorResponse errorResponse = new ErrorResponse("Role Not allowed", e.getMessage());
        return Response.status(Response.Status.FORBIDDEN).entity(new MessageDTO<>(true, errorResponse)).build();
    }
}
