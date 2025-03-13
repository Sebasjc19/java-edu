package co.edu.uniquindio.ingesis.restful.security.jwt;
import io.smallrye.jwt.build.Jwt;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.PrivateKey;
import io.smallrye.jwt.util.KeyUtils;
import java.util.Set;

import io.smallrye.jwt.build.Jwt;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.PrivateKey;
import java.util.Set;

public class JwtGenerator {
    public static void main(String[] args) throws Exception {
        // Cargar clave privada desde un archivo
        String privateKeyContent = new String(Files.readAllBytes(Paths.get("src/main/resources/privateKey.pem")))
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", ""); // Eliminar saltos de línea y espacios

        PrivateKey privateKey = KeyUtils.decodePrivateKey(privateKeyContent);

        // 🔹 Generar token para usuario normal
        String userToken = generateToken("usuario_demo@example.com", Set.of("user"), privateKey);

        // 🔹 Generar token para administrador
        String adminToken = generateToken("admin@example.com", Set.of("admin", "user"), privateKey);

        System.out.println("🔹 Token Usuario: " + userToken);
        System.out.println("🔹 Token Admin: " + adminToken);
    }

    private static String generateToken(String upn, Set<String> roles, PrivateKey privateKey) {
        return Jwt.issuer("https://example.com/issuer")
                .upn(upn)
                .groups(roles) // Definir los roles
                .expiresAt(System.currentTimeMillis() / 1000 + 3600) // Expira en 1 hora
                .sign(privateKey);
    }
}


