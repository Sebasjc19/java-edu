package co.edu.uniquindio.ingesis.restful.services.implementations;

import co.edu.uniquindio.ingesis.restful.domain.Role;
import co.edu.uniquindio.ingesis.restful.domain.User;
import co.edu.uniquindio.ingesis.restful.dtos.usuarios.SesionAdminRequest;
import co.edu.uniquindio.ingesis.restful.dtos.usuarios.SesionUserRequest;
import co.edu.uniquindio.ingesis.restful.dtos.usuarios.TokenResponse;
import co.edu.uniquindio.ingesis.restful.mappers.UserMapper;
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
    UserMapper userMapper;
    UserRepository userRepository;
    @Override
    public TokenResponse loginUser(SesionUserRequest sesionUserRequest) {
        return userRepository.findByEmail(sesionUserRequest.email())
                .filter(user -> BcryptUtil.matches(sesionUserRequest.password(), user.getPassword()))
                .filter(user -> user.getRole() != null && !user.getRole().equals(Role.ADMIN))
                .map(user -> {
                    String token = JwtGenerator.generateToken(
                            user.getEmail(),
                            user.getBirthDate().toString(),
                            user.getRole().toString(),
                            user.getIdentificationNumber()
                    );
                    return new TokenResponse(token);
                })
                .orElseThrow(() -> new IllegalArgumentException("Credenciales inválidas o usuario sin rol válido."));
    }

    @Override
    public TokenResponse loginAdmin(SesionAdminRequest sesionAdminRequest) throws Exception {
        return userRepository.findByEmail(sesionAdminRequest.email())
                .filter(user -> BcryptUtil.matches(sesionAdminRequest.password(), user.getPassword()))
                .filter(user -> user.getRole() != null && !user.getRole().equals(Role.USER))
                .map(user -> {
                    String token = JwtGenerator.generateToken(
                            user.getEmail(),
                            user.getBirthDate().toString(),
                            user.getRole().toString(),
                            user.getIdentificationNumber()
                    );
                    return new TokenResponse(token);
                })
                .orElseThrow(() -> new IllegalArgumentException("Credenciales inválidas o usuario sin rol válido."));
    }

}
