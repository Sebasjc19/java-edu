package co.edu.uniquindio.ingesis.restful.security.jwt;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import org.eclipse.microprofile.jwt.JsonWebToken;

@Path("/secured")
public class TokenSecuredResource {

    @Inject
    JsonWebToken jwt; // Objeto que contiene los datos del JWT

    // 🔹 Acceso permitido a cualquier usuario autenticado (con JWT válido)
    @GET
    @Path("/user-endpoint")
    @RolesAllowed("user")  // Solo pueden acceder los usuarios con el rol "user"
    @Produces(MediaType.TEXT_PLAIN)
    public String userEndpoint() {
        return "Bienvenido usuario: " + jwt.getName();
    }

    // 🔹 Acceso solo para administradores
    @GET
    @Path("/admin-endpoint")
    @RolesAllowed("admin")  // Solo los "admin" pueden acceder aquí
    @Produces(MediaType.TEXT_PLAIN)
    public String adminEndpoint() {
        return "Bienvenido administrador: " + jwt.getName();
    }
}
