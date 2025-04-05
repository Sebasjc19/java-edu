package co.edu.uniquindio.ingesis.restful.exceptions.users.mappers;

import co.edu.uniquindio.ingesis.restful.dtos.MessageDTO;
import co.edu.uniquindio.ingesis.restful.dtos.usuarios.ErrorResponse;
import co.edu.uniquindio.ingesis.restful.exceptions.users.implementations.ResourceNotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ResourceNotFoundExceptionMapper implements ExceptionMapper<ResourceNotFoundException> {
    @Override
    public Response toResponse(ResourceNotFoundException e) {
        ErrorResponse errorResponse = new ErrorResponse("Resource not found", e.getMessage());
        return Response.status(Response.Status.NOT_FOUND)
                .entity(new MessageDTO<>(true, errorResponse))
                .build();
    }
}
