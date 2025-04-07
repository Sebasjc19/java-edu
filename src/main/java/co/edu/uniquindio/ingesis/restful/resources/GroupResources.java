package co.edu.uniquindio.ingesis.restful.resources;

import co.edu.uniquindio.ingesis.restful.dtos.MessageDTO;
import co.edu.uniquindio.ingesis.restful.dtos.comments.CommentResponse;
import co.edu.uniquindio.ingesis.restful.dtos.groups.GroupCreationRequest;
import co.edu.uniquindio.ingesis.restful.dtos.groups.GroupResponse;
import co.edu.uniquindio.ingesis.restful.dtos.groups.UpdateGroupRequest;
import co.edu.uniquindio.ingesis.restful.services.interfaces.CommentService;
import co.edu.uniquindio.ingesis.restful.services.interfaces.GroupService;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Path("/groups")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class GroupResources {

    GroupService groupService;

    /**
     * Obtener todos los grupos de un profesor específico.
     */
    @GET
    @Path("/{professorId}")
    @RolesAllowed({"TUTOR"})
    public Response findGroupsByProfessorId(@PathParam("professorId") Long professorId) {
        List<GroupResponse> groupResponse = groupService.findGroupsByProfessorId(professorId);
        return Response.ok(new MessageDTO<>(false, groupResponse)).build();
    }

    /**
     * Obtener un grupo por su ID.
     */
    @GET
    @Path("/{id}")
    @PermitAll
    public Response getGroupById(@PathParam("id") Long id) {
        GroupResponse groupResponse = groupService.getGroupById(id);
        return Response.ok(new MessageDTO<>(false, groupResponse)).build();
    }

    /**
     * Crear un nuevo grupo.
     */
    @POST
    @RolesAllowed({"TUTOR"})
    public Response createGroup(@Valid GroupCreationRequest request) {
        GroupResponse groupResponse = groupService.createGroup(request);
        return Response.status(Response.Status.CREATED).entity(groupResponse).build();
    }

    /**
     * Actualizar un grupo por ID.
     */
    @PUT
    @Path("/{id}")
    @RolesAllowed({"TUTOR"})
    public Response updateGroupById(@PathParam("id") Long id, @Valid UpdateGroupRequest request) {
        GroupResponse groupResponse = groupService.updateGroupById(id, request);
        return Response.ok(new MessageDTO<>(false, groupResponse)).build();
    }

    /**
     * Eliminar un grupo por ID.
     */
    @DELETE
    @Path("/{id}")
    @RolesAllowed({"TUTOR", "ADMIN"})
    public Response deleteGroup(@PathParam("id") Long id) {
        GroupResponse groupResponse = groupService.deleteGroup(id);
        return Response.ok(new MessageDTO<>(false, groupResponse)).build();
    }
}
