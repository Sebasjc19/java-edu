package co.edu.uniquindio.ingesis.restful.resources;

import co.edu.uniquindio.ingesis.restful.dtos.usuarios.ObtainUsersResponse;
import co.edu.uniquindio.ingesis.restful.dtos.usuarios.UserRegistrationRequest;
import co.edu.uniquindio.ingesis.restful.dtos.usuarios.UserResponse;
import co.edu.uniquindio.ingesis.restful.dtos.usuarios.UserUpdateRequest;
import co.edu.uniquindio.ingesis.restful.exceptions.usuarios.EmailAlredyExistsExceptionMapper;
import co.edu.uniquindio.ingesis.restful.exceptions.usuarios.ResourceNotFoundException;
import co.edu.uniquindio.ingesis.restful.services.interfaces.UserService;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResources {
    UserService userService;

    @DELETE
    @Path("/{id}")
    public Response deleteUser(@PathParam("id") Long id) {
        UserResponse deleteUserResponse = userService.deleteUser(id);
        return Response.ok(deleteUserResponse).build();
    }

    @GET
    public Response getAllUsers() {
        //Consultar usuarios en la base de datos
        List<UserResponse> users = userService.getAllUsers();
        return Response.ok(users).build();
    }

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") Long id) throws ResourceNotFoundException {
        UserResponse userResponse = userService.findById(id);
        return Response.ok(userResponse).build();
    }

    /**
     * Obtener usuarios activos
     */
    @GET
    @Path("/active")
    public Response getActiveUsers() {
        List<UserResponse> userResponses = userService.getActiveUsers();
        return Response.ok(userResponses).build();
    }

    @POST
    public Response createUser(@Valid UserRegistrationRequest request) {
        UserResponse newUser = userService.createUser(request);
        return Response.status(Response.Status.CREATED).entity(newUser).build();
    }

    @PATCH
    @Path("/{id}")
    public Response updateUserById(@PathParam("id") Long id, @Valid UserUpdateRequest request) {
        UserResponse userResponse = userService.updateUserById(id, request);
        return Response.ok(userResponse).build();
    }
}