package co.edu.uniquindio.ingesis.restful.services.implementations;

import co.edu.uniquindio.ingesis.restful.domain.User;
import co.edu.uniquindio.ingesis.restful.dtos.usuarios.LoginRequest;
import co.edu.uniquindio.ingesis.restful.dtos.usuarios.TokenResponse;
import co.edu.uniquindio.ingesis.restful.mappers.UserMapper;
import co.edu.uniquindio.ingesis.restful.services.interfaces.AuthService;
import co.edu.uniquindio.ingesis.restful.repositories.interfaces.UserRepository;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import java.util.Optional;

import static io.quarkus.elytron.security.common.BcryptUtil.matches;

@ApplicationScoped
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    @Inject
    UserMapper userMapper;
    UserRepository userRepository;

    private TokenResponse createToken( User user ){
        return new TokenResponse(
                        Jwt
                        .subject(user.getUsername())
                        .groups(user.getRole().toString())
                        .sign()
        );
    }


    @Override
    public Optional<TokenResponse> login(LoginRequest request)  {
        return userRepository.findByEmail(request.email())
                .filter( user-> matches(request.password(), user.getPassword()) )
                .map( this::createToken );
    }
}
