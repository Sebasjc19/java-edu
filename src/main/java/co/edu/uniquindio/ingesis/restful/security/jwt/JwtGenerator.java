package co.edu.uniquindio.ingesis.restful.security.jwt;
import co.edu.uniquindio.ingesis.restful.domain.Role;
import io.smallrye.jwt.build.Jwt;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class JwtGenerator {

    public static String generateToken(String email, String birthdate, String role, String identificatiopnNumber) {
        // Validar que el usuario tiene un rol asignado
        if (role == null || role.isEmpty()) {
            throw new IllegalArgumentException("No se puede generar un token sin un rol asignado.");
        }

        // Validar que un usuario "CLIENTE" no tenga permisos de "ADMIN"
        if (Role.USER.toString().equalsIgnoreCase(role) && Role.ADMIN.toString().equalsIgnoreCase(role)) {
            throw new IllegalArgumentException("Un usuario CLIENTE no puede tener permisos de ADMIN.");
        }

        return Jwt.issuer("https://example.com/issuer")
                .upn(email)
                .groups(Set.of(role)) // Asignar el único rol permitido
                .claim("birthdate", birthdate)
                .claim("identification",identificatiopnNumber) // Agregar otras claims personalizadas
                .expiresAt(Instant.now().getEpochSecond() + 3600) // Expira en 1 hora
                .sign();
    }
}




