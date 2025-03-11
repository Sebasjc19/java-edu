package co.edu.uniquindio.ingesis.restful.resources;

import co.edu.uniquindio.ingesis.restful.dtos.comments.CommentResponse;
import co.edu.uniquindio.ingesis.restful.dtos.groups.GroupCreationRequest;
import co.edu.uniquindio.ingesis.restful.dtos.groups.GroupResponse;
import co.edu.uniquindio.ingesis.restful.dtos.groups.UpdateGroupRequest;
import co.edu.uniquindio.ingesis.restful.services.interfaces.CommentService;
import co.edu.uniquindio.ingesis.restful.services.interfaces.GroupService;
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
    public Response findGroupsByProfessorId(@PathParam("professorId") Long professorId) {
        List<GroupResponse> groupResponse = groupService.findGroupsByProfessorId(professorId);
        return Response.ok(groupResponse).build();
    }

    /**
     * Obtener un grupo por su ID.
     */
    @GET
    @Path("/{id}")
    public Response getGroupById(@PathParam("id") Long id) {
        GroupResponse groupResponse = groupService.getGroupById(id);
        return Response.ok(groupResponse).build();
    }

    /**
     * Crear un nuevo grupo.
     */
    @POST
    public Response createGroup(@Valid GroupCreationRequest request) {
        GroupResponse groupResponse = groupService.createGroup(request);
        return Response.status(Response.Status.CREATED).entity(groupResponse).build();
    }

    /**
     * Actualizar un grupo por ID.
     */
    @PUT
    @Path("/{id}")
    public Response updateGroupById(@PathParam("id") Long id, @Valid UpdateGroupRequest request) {
        GroupResponse groupResponse = groupService.updateGroupById(id, request);
        return Response.ok(groupResponse).build();
    }

    /**
     * Eliminar un grupo por ID.
     */
    @DELETE
    @Path("/{id}")
    public Response deleteGroup(@PathParam("id") Long id) {
        GroupResponse groupResponse = groupService.deleteGroup(id);
        return Response.ok(groupResponse).build();
    }
}
