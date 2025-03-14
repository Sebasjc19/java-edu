package co.edu.uniquindio.ingesis.restful.services.interfaces;

import co.edu.uniquindio.ingesis.restful.dtos.usuarios.SesionAdminRequest;
import co.edu.uniquindio.ingesis.restful.dtos.usuarios.SesionUserRequest;
import co.edu.uniquindio.ingesis.restful.dtos.usuarios.TokenResponse;

public interface AuthService {
    public TokenResponse loginUser(SesionUserRequest sesionUserRequest)throws Exception;
    public TokenResponse loginAdmin(SesionAdminRequest sesionAdminRequest)throws Exception;
}
