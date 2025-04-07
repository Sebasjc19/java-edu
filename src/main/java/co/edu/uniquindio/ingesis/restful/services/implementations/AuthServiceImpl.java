package co.edu.uniquindio.ingesis.restful.services.implementations;

import co.edu.uniquindio.ingesis.restful.domain.User;
import co.edu.uniquindio.ingesis.restful.dtos.TokenDTO;
import co.edu.uniquindio.ingesis.restful.dtos.usuarios.LoginRequest;
import co.edu.uniquindio.ingesis.restful.dtos.usuarios.TokenResponse;
import co.edu.uniquindio.ingesis.restful.jwt.utils.TokenUtils;
import co.edu.uniquindio.ingesis.restful.mappers.UserMapper;
import co.edu.uniquindio.ingesis.restful.services.interfaces.AuthService;
import co.edu.uniquindio.ingesis.restful.repositories.interfaces.UserRepository;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import org.jose4j.jwk.Use;
import org.jose4j.jwt.JwtClaims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static io.quarkus.elytron.security.common.BcryptUtil.matches;

@RequestScoped
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    @Inject
    UserRepository userRepository;

    private static final Logger auditLogger = LoggerFactory.getLogger("audit");

    public TokenDTO userLogin(LoginRequest loginRequest) throws Exception {
        Optional<User> optionalUser = userRepository.findByEmail(loginRequest.email());
        if (optionalUser.isEmpty()){
            throw new Exception(" El correo no se encuentra registrado");
        }
        User user = optionalUser.get();
        //cambiar cuando se encripte la contraseña
        if(!BcryptUtil.matches(loginRequest.password(), user.getPassword())){
            throw new Exception("La contraseña es incorrecta");
        }
        try {

            JwtClaims jwtClaims = new JwtClaims();
            jwtClaims.setIssuer("Java-edu");
            jwtClaims.setSubject(user.getEmail());
            jwtClaims.setAudience("users");
            jwtClaims.setStringClaim("rol", user.getRole().toString());
            jwtClaims.setStringClaim("nombre", user.getUsername());
            jwtClaims.setExpirationTimeMinutesInTheFuture(60);
            String token = TokenUtils.generateTokenString(jwtClaims, user.getRole().toString());

            auditLogger.info("Login exitoso para usuario '{}'", user.getEmail());
            // Registrar en el Log
            return new TokenDTO(token);
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException();
            //Registrar en el log
        }
    }
}
