package co.edu.uniquindio.ingesis.restful.services.interfaces;

import co.edu.uniquindio.ingesis.restful.domain.Status;
import co.edu.uniquindio.ingesis.restful.dtos.usuarios.ShowUserRequest;
import co.edu.uniquindio.ingesis.restful.dtos.usuarios.UserRegistrationRequest;
import co.edu.uniquindio.ingesis.restful.dtos.usuarios.UserResponse;
import co.edu.uniquindio.ingesis.restful.dtos.usuarios.UserUpdateRequest;
import co.edu.uniquindio.ingesis.restful.exceptions.usuarios.ResourceNotFoundException;

import java.util.List;
import java.util.Set;

public interface UserService {
    UserResponse createUser(UserRegistrationRequest request);
    UserResponse findById(Long id) throws ResourceNotFoundException;
    UserResponse deleteUser(Long id);
    UserResponse showUser(Long id, ShowUserRequest showUserRequest);
    List<UserResponse> getActiveUsers();
    List<UserResponse> getAllUsers();
    UserResponse updateUserById(Long id, UserUpdateRequest request);
}
