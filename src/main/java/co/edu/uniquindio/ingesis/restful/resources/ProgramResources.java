package co.edu.uniquindio.ingesis.restful.resources;

import co.edu.uniquindio.ingesis.restful.dtos.programs.ProgramCreationRequest;
import co.edu.uniquindio.ingesis.restful.dtos.programs.ProgramResponse;
import co.edu.uniquindio.ingesis.restful.dtos.programs.UpdateProgramRequest;
import co.edu.uniquindio.ingesis.restful.exceptions.users.implementations.ResourceNotFoundException;
import co.edu.uniquindio.ingesis.restful.services.implementations.JavaExecutionServiceImpl;
import co.edu.uniquindio.ingesis.restful.services.interfaces.ProgramService;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

import java.io.IOException;
import java.util.List;

@Path("/programs")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class ProgramResources {

    @Inject
    ProgramService programService;
    JavaExecutionServiceImpl executeProgramService;

    /**
     * Obtener todos los programas asociados a un usuario.
     */
    @GET
    @RolesAllowed({"STUDENT", "TUTOR"})
    @Path("/{userId}")
    public Response findProgramsByUserId(@PathParam("userId") Long userId) {
        List<ProgramResponse> programResponses = programService.findProgramsByUserId(userId);
        return Response.ok(programResponses).build();
    }

    /**
     * Obtener un programa por su ID.
     */
    @GET
    @RolesAllowed({"STUDENT", "TUTOR"})
    @Path("/{id}")
    public Response getProgramById(@PathParam("id") Long id) {
        ProgramResponse programResponse = programService.getProgramById(id);
        return Response.ok(programResponse).build();
    }

    /**
     * Crear un programa por primera vez
     */
    @POST
    @PermitAll
    public Response createProgram(@Valid ProgramCreationRequest request){
        ProgramResponse programResponse = programService.createProgram(request);
        return Response.status(Response.Status.CREATED).entity(programResponse).build();
    }

    /**
     * Actualizar un programa
     */
    @PUT
    @RolesAllowed({"STUDENT", "TUTOR"})
    @Path("/{id}")
    public Response updateProgramById(@PathParam("id") Long id, @Valid UpdateProgramRequest request){
        ProgramResponse programResponse = programService.updateProgramById(id, request);
        return Response.ok(programResponse).build();
    }

    /**
     * Eliminar un programa por ID.
     */
    @DELETE
    @RolesAllowed({"STUDENT", "TUTOR"})
    @Path("/{id}")
    public Response deleteProgram(@PathParam("id") Long id) throws ResourceNotFoundException {
        ProgramResponse programResponse = programService.deleteProgram(id);
        return Response.ok(programResponse).build();
    }

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public Response ejecutarPrograma(String codigo) {
        try {
            String resultado = executeProgramService.ejecutarCodigo(codigo);
            return Response.ok(resultado).build();
        } catch (SecurityException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Código no permitido: " + e.getMessage()).build();
        } catch (IOException | InterruptedException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error al ejecutar: " + e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error inesperado: " + e.getMessage()).build();
        }
    }
}
