package co.edu.uniquindio.ingesis.restful.resources;

import co.edu.uniquindio.ingesis.restful.dtos.comments.CommentCreationRequest;
import co.edu.uniquindio.ingesis.restful.dtos.comments.CommentResponse;
import co.edu.uniquindio.ingesis.restful.dtos.comments.UpdateCommentRequest;
import co.edu.uniquindio.ingesis.restful.services.interfaces.CommentService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Path("/comments")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class CommentResources {

    CommentService commentService;

    //CRUD
    // 1. Obtener todos los comentarios
    @GET
    @RolesAllowed({"ADMIN"})
    public Response getAllComments(@QueryParam("page")@DefaultValue("0") int page) {
        List<CommentResponse> commentResponse = commentService.getAllComments(page);
        return Response.ok(commentResponse).build();
    }

    // 2. Obtener un comentario por ID
    @GET
    @Path("/{id}")
    @RolesAllowed({"STUDENT", "TUTOR"})

    public Response getCommentById(@PathParam("id") Long id) {
        CommentResponse commentResponse = commentService.getCommentById(id);
        return Response.ok(commentResponse).build();
    }

    // 3. Crear un nuevo comentario
    @POST
    @RolesAllowed({"TUTOR"})
    public Response createComment(@Valid CommentCreationRequest request) {
        CommentResponse commentResponse = commentService.createComment(request);
        return Response.ok(commentResponse).build();
    }

    // 4. Actualizar completamente un comentario
    @PUT
    @RolesAllowed({"TUTOR"})
    @Path("/{id}")
    public Response updateCommentById(@PathParam("id") Long id, @Valid UpdateCommentRequest request) {
        CommentResponse commentResponse = commentService.updateCommentById(id, request);
        return Response.ok(commentResponse).build();
    }

    // 6. Eliminar un comentario
    @DELETE
    @RolesAllowed({"TUTOR"})
    @Path("/{id}")
    public Response deleteComment(@PathParam("id") Long id) {
        CommentResponse deleteCommentResponse = commentService.deleteComment(id);
        return Response.ok(deleteCommentResponse).build();
    }

    // 7. Método personalizado: Obtener comentarios realizados segun el profesor
    @GET
    @RolesAllowed({"STUDENT"})
    @Path("/{professorId}")
    public Response findCommentsByProfessorId(@PathParam("professorId") Long professorId,@QueryParam("page")@DefaultValue("0") int page) {
        //TODO: pendiente de organizar la ruta
        List<CommentResponse> commentResponseList = commentService.findCommentsByProfessorId(professorId, page);
        return Response.ok(commentResponseList).build();
    }
}
