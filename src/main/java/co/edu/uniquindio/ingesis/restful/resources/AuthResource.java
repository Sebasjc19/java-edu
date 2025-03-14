package co.edu.uniquindio.ingesis.restful.resources;
import co.edu.uniquindio.ingesis.restful.dtos.MessageDTO;
import co.edu.uniquindio.ingesis.restful.dtos.usuarios.SesionAdminRequest;
import co.edu.uniquindio.ingesis.restful.dtos.usuarios.SesionUserRequest;
import co.edu.uniquindio.ingesis.restful.dtos.usuarios.TokenResponse;
import co.edu.uniquindio.ingesis.restful.services.interfaces.AuthService;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

    AuthService authService;
    @POST
    @Path("/login-user")
    public Response loginUser(@Valid SesionUserRequest sesionUserRequest) throws Exception {
        TokenResponse tokenResponse = authService.loginUser(sesionUserRequest);
        return Response.ok(new MessageDTO<>(false, tokenResponse)).build();
    }

    @POST
    @Path("/login-admin")
    public Response loginAdmin(@Valid SesionAdminRequest sesionAdminRequest) throws Exception {
        TokenResponse tokenResponse = authService.loginAdmin(sesionAdminRequest);
        return Response.ok(new MessageDTO<>(false, tokenResponse)).build();
    }

}