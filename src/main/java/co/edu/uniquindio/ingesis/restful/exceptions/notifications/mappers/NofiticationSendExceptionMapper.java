package co.edu.uniquindio.ingesis.restful.exceptions.notifications.mappers;

import co.edu.uniquindio.ingesis.restful.dtos.MessageDTO;
import co.edu.uniquindio.ingesis.restful.dtos.usuarios.ErrorResponse;
import co.edu.uniquindio.ingesis.restful.exceptions.notifications.implementations.NotificationSendException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class NofiticationSendExceptionMapper implements ExceptionMapper<NotificationSendException> {

    @Override
    public Response toResponse(NotificationSendException e){
        ErrorResponse errorResponse = new ErrorResponse("Notification send failed", e.getMessage());
        return Response.status(Response.Status.UNAUTHORIZED)
                .entity(new MessageDTO<>(true, errorResponse))
                .build();
    }
}
