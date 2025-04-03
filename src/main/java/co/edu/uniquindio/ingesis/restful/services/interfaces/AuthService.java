package co.edu.uniquindio.ingesis.restful.services.interfaces;

import co.edu.uniquindio.ingesis.restful.dtos.TokenDTO;
import co.edu.uniquindio.ingesis.restful.dtos.usuarios.LoginRequest;

import co.edu.uniquindio.ingesis.restful.dtos.usuarios.TokenResponse;

import java.util.Optional;

public interface AuthService {
    public TokenDTO userLogin(LoginRequest loginRequest) throws Exception;
}
