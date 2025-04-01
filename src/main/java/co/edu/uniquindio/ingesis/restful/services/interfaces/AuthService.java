package co.edu.uniquindio.ingesis.restful.services.interfaces;

import co.edu.uniquindio.ingesis.restful.dtos.usuarios.LoginRequest;

import co.edu.uniquindio.ingesis.restful.dtos.usuarios.TokenResponse;

import java.util.Optional;

public interface AuthService {
    Optional<TokenResponse> login(LoginRequest request);

}
