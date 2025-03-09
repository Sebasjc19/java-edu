package co.edu.uniquindio.ingesis.restful.services.interfaces;

import co.edu.uniquindio.ingesis.restful.domain.Status;
import co.edu.uniquindio.ingesis.restful.dtos.usuarios.UserRegistrationRequest;
import co.edu.uniquindio.ingesis.restful.dtos.usuarios.UserResponse;
import co.edu.uniquindio.ingesis.restful.dtos.usuarios.UserUpdateRequest;

import java.util.List;

public interface UserService {
    UserResponse createUser(UserRegistrationRequest request);
    UserResponse findById(Long id);
    UserResponse deleteUser(Long id);
    List<UserResponse> getActiveUsers();
    List<UserResponse> getAllUsers();
    UserResponse updateUserById(Long id, UserUpdateRequest request);
}
