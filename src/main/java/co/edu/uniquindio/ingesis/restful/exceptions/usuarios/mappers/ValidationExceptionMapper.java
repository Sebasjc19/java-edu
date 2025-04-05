package co.edu.uniquindio.ingesis.restful.exceptions.usuarios.mappers;

import co.edu.uniquindio.ingesis.restful.dtos.MessageDTO;
import co.edu.uniquindio.ingesis.restful.dtos.usuarios.ErrorResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.List;
import java.util.stream.Collectors;

@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        List<ErrorResponse> errorResponses = exception.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .map(message -> new ErrorResponse("Validation Error", message))
                .collect(Collectors.toList());

        return Response.status(Response.Status.BAD_REQUEST).entity(new MessageDTO<>(true, errorResponses)).build();
    }
}
