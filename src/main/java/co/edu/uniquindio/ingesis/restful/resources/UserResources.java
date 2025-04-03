package co.edu.uniquindio.ingesis.restful.resources;

import co.edu.uniquindio.ingesis.restful.dtos.MessageDTO;
import co.edu.uniquindio.ingesis.restful.dtos.usuarios.ObtainUsersResponse;
import co.edu.uniquindio.ingesis.restful.dtos.usuarios.UserRegistrationRequest;
import co.edu.uniquindio.ingesis.restful.dtos.usuarios.UserResponse;
import co.edu.uniquindio.ingesis.restful.dtos.usuarios.UserUpdateRequest;
import co.edu.uniquindio.ingesis.restful.exceptions.usuarios.EmailAlredyExistsExceptionMapper;
import co.edu.uniquindio.ingesis.restful.exceptions.usuarios.ResourceNotFoundException;
import co.edu.uniquindio.ingesis.restful.services.interfaces.UserService;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResources {
    @Inject
    UserService userService;

    @DELETE
    @Path("/{id}")
    @RolesAllowed({"STUDENT", "TUTOR"})
    public Response deleteUser(@PathParam("id") Long id) {
        UserResponse deleteUserResponse = userService.deleteUser(id);
        return Response.ok(new MessageDTO<>(false, deleteUserResponse)).build();
    }

    @GET
    public Response getAllUsers() {
        //Consultar usuarios en la base de datos
        List<UserResponse> users = userService.getAllUsers();
        return Response.ok(new MessageDTO<>(false, users)).build();
    }

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") Long id) throws ResourceNotFoundException {
        UserResponse userResponse = userService.findById(id);
        return Response.ok(new MessageDTO<>(false, userResponse)).build();
    }

    /**
     * Obtener usuarios activos
     */
    @GET
    @Path("/active")
    @RolesAllowed({"ADMIN"})
    public Response getActiveUsers() {
        List<UserResponse> userResponses = userService.getActiveUsers();
        return Response.ok(new MessageDTO<>(false, userResponses)).build();
    }

    @POST
    @PermitAll
    public Response createUser(@Valid UserRegistrationRequest request) {
        UserResponse newUser = userService.createUser(request);
        return Response.status(Response.Status.CREATED).entity(newUser).build();
    }

    @PATCH
    @Path("/{id}")
    @RolesAllowed({"STUDENT", "TUTOR"})
    public Response updateUserById(@PathParam("id") Long id, @Valid UserUpdateRequest request) {
        UserResponse userResponse = userService.updateUserById(id, request);
        return Response.ok(new MessageDTO<>(false, userResponse)).build();
    }
}