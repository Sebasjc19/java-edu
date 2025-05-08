package co.edu.uniquindio.ingesis.restful.resources;
import co.edu.uniquindio.ingesis.restful.dtos.MessageDTO;
import co.edu.uniquindio.ingesis.restful.dtos.TokenDTO;
import co.edu.uniquindio.ingesis.restful.dtos.usuarios.LoginRequest;
import co.edu.uniquindio.ingesis.restful.dtos.usuarios.SesionAdminRequest;
import co.edu.uniquindio.ingesis.restful.dtos.usuarios.SesionUserRequest;
import co.edu.uniquindio.ingesis.restful.dtos.usuarios.TokenResponse;
import co.edu.uniquindio.ingesis.restful.services.interfaces.AuthService;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

    @Inject AuthService authService;

    @POST
    @PermitAll
    public Response login(@Valid LoginRequest credentials) {
        try {
            TokenDTO tokenDTO = authService.userLogin(credentials);
            return Response.ok(new MessageDTO<>(false, tokenDTO)).build();
        }catch (Exception e){
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(new MessageDTO<>(true, "Error al iniciar sesion")).build();
        }
    }


}