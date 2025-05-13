package co.edu.uniquindio.ingesis.restful.exceptions.groups.mappers;

import co.edu.uniquindio.ingesis.restful.dtos.MessageDTO;
import co.edu.uniquindio.ingesis.restful.dtos.usuarios.ErrorResponse;
import co.edu.uniquindio.ingesis.restful.exceptions.groups.implementations.StudentNotInGroupException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;

public class StudentNotInGroupExceptionMapper implements ExceptionMapper<StudentNotInGroupException> {
    @Override
    public Response toResponse(StudentNotInGroupException e) {
        ErrorResponse errorResponse = new ErrorResponse("Student not in group", e.getMessage());
        return Response.status(Response.Status.BAD_REQUEST).entity(new MessageDTO<>(true, errorResponse)).build();
    }
}
