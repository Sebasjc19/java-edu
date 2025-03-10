package co.edu.uniquindio.ingesis.restful.resources;

import co.edu.uniquindio.ingesis.restful.dtos.notifications.NotificationCreationRequest;
import co.edu.uniquindio.ingesis.restful.dtos.notifications.NotificationResponse;
import co.edu.uniquindio.ingesis.restful.services.interfaces.NotificationService;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Path("/notifications")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class NotificationResources {

    NotificationService notificationService;

    @GET
    @Path("/student/{studentId}")
    public Response getNotificationsByStudentId(@PathParam("studentId") Long studentId) {
        List<NotificationResponse> notificationResponses =
                notificationService.findNotificationsByStudentId(studentId);
        return Response.ok(notificationResponses).build();
    }

    @GET
    @Path("/{id}")
    public Response getNotificationById(@PathParam("id") Long id) {
        NotificationResponse notificationResponse = notificationService.getNotificationById(id);
        return Response.ok(notificationResponse).build();
    }

    @POST
    public Response createNotification(@Valid NotificationCreationRequest request) {
        NotificationResponse notificationResponse = notificationService.createNotification(request);
        return Response.status(Response.Status.CREATED).entity(notificationResponse).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteNotification(@PathParam("id") Long id) {
        NotificationResponse notificationResponse = notificationService.deleteNotification(id);
        return Response.ok(notificationResponse).build();
    }
}
