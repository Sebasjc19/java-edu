package co.edu.uniquindio.ingesis.restful.services.implementations;

import co.edu.uniquindio.ingesis.restful.domain.Role;
import co.edu.uniquindio.ingesis.restful.domain.User;
import co.edu.uniquindio.ingesis.restful.dtos.usuarios.SesionAdminRequest;
import co.edu.uniquindio.ingesis.restful.dtos.usuarios.SesionUserRequest;
import co.edu.uniquindio.ingesis.restful.dtos.usuarios.TokenResponse;
import co.edu.uniquindio.ingesis.restful.services.interfaces.AuthService;
import co.edu.uniquindio.ingesis.restful.repositories.interfaces.UserRepository;
import co.edu.uniquindio.ingesis.restful.security.jwt.JwtGenerator;
import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import java.util.Optional;

@ApplicationScoped
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    @Inject
    UserRepository userRepository;
    @Override
    public TokenResponse loginUser(SesionUserRequest sesionUserRequest) {
        if (sesionUserRequest.email() == null || sesionUserRequest.email().isEmpty() ||
                sesionUserRequest.password() == null || sesionUserRequest.password().isEmpty()) {
            throw new IllegalArgumentException("El email y la contraseña no pueden estar vacíos.");
        }

        Optional<User> usuarioOptional = userRepository.findByEmail(sesionUserRequest.email());

        if (usuarioOptional.isEmpty()) {
            throw new IllegalArgumentException("El correo no se encuentra registrado.");
        }

        User user = usuarioOptional.get();
        if (!BcryptUtil.matches(sesionUserRequest.password(), user.getPassword())) {
            throw new IllegalArgumentException("La contraseña es incorrecta.");
        }

        // Usar el rol real
        if (user.getRole()== null || user.getRole().equals(Role.ADMIN)) {
            throw new IllegalArgumentException("El usuario no tiene un rol asignado.");
        }
        String token = JwtGenerator.generateToken(user.getEmail(), user.getBirthDate().toString(), user.getRole().toString(), user.getIdentificationNumber());

        return new TokenResponse(token);
    }


    @Override
    public TokenResponse loginAdmin(SesionAdminRequest sesionAdminRequest) throws Exception {
        if (sesionAdminRequest.email() == null || sesionAdminRequest.email().isEmpty() ||
                sesionAdminRequest.password() == null || sesionAdminRequest.password().isEmpty()) {
            throw new IllegalArgumentException("El email y la contraseña no pueden estar vacíos.");
        }

        Optional<User> usuarioOptional = userRepository.findByEmail(sesionAdminRequest.email());

        if (usuarioOptional.isEmpty()) {
            throw new IllegalArgumentException("El correo no se encuentra registrado.");
        }

        User user = usuarioOptional.get();
        if (!BcryptUtil.matches(sesionAdminRequest.password(), user.getPassword())) {
            throw new IllegalArgumentException("La contraseña es incorrecta.");
        }

        // Usar el rol real
        if (user.getRole()== null || user.getRole().equals(Role.USER)) {
            throw new IllegalArgumentException("El usuario no tiene un rol asignado.");
        }
        String token = JwtGenerator.generateToken(user.getEmail(), user.getBirthDate().toString(), user.getRole().toString(), user.getIdentificationNumber());

        return new TokenResponse(token);
    }
}
