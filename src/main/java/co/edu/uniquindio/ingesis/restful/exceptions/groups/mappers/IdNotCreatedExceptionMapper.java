package co.edu.uniquindio.ingesis.restful.exceptions.groups.mappers;

import co.edu.uniquindio.ingesis.restful.dtos.MessageDTO;
import co.edu.uniquindio.ingesis.restful.dtos.usuarios.ErrorResponse;
import co.edu.uniquindio.ingesis.restful.exceptions.groups.implementations.IdNotCreatedException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;

public class IdNotCreatedExceptionMapper implements ExceptionMapper<IdNotCreatedException> {
    @Override
    public Response toResponse(IdNotCreatedException e) {
        ErrorResponse error = new ErrorResponse("Group Id not created", e.getMessage());
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new MessageDTO<>(true, error)).build();
    }
}
