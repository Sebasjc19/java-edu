package co.edu.uniquindio.ingesis.restful.resources;

import co.edu.uniquindio.ingesis.restful.dtos.notifications.NotificationCreationRequest;
import co.edu.uniquindio.ingesis.restful.dtos.notifications.NotificationResponse;
import co.edu.uniquindio.ingesis.restful.services.interfaces.NotificationService;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
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
    @Inject
    NotificationService notificationService;

    @GET
    @RolesAllowed({"STUDENT","TUTOR"})
    @Path("/student/{studentId}")
    public Response getNotificationsByStudentId(@PathParam("studentId") Long studentId,@QueryParam("page")@DefaultValue("0") int page) {
        List<NotificationResponse> notificationResponses =
                notificationService.findNotificationsByStudentId(studentId,page);

        return Response.ok(notificationResponses).build();
    }

    @GET
    @RolesAllowed({"STUDENT", "TUTOR"})
    @Path("/{id}")
    public Response getNotificationById(@PathParam("id") Long id) {
        NotificationResponse notificationResponse = notificationService.getNotificationById(id);
        return Response.ok(notificationResponse).build();
    }
    @PermitAll
    @POST
    public Response createNotification(@Valid NotificationCreationRequest request) {
        notificationService.sendNotification(request);
        return Response.status(Response.Status.CREATED).build();
    }

    @DELETE
    @RolesAllowed({"STUDENT","TUTOR"})
    @Path("/{id}")
    public Response deleteNotification(@PathParam("id") Long id) {
        NotificationResponse notificationResponse = notificationService.deleteNotification(id);
        return Response.ok(notificationResponse).build();
    }
}
