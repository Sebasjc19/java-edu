package co.edu.uniquindio.ingesis.restful.repositories.implementations;

import co.edu.uniquindio.ingesis.restful.domain.Status;
import co.edu.uniquindio.ingesis.restful.domain.User;
import co.edu.uniquindio.ingesis.restful.repositories.interfaces.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepositoryImpl implements UserRepository {
    @Override
    public Optional<User> findByEmail(String email) {
        Optional<User> userOptional = find("email", email).firstResultOptional();
        return Optional.empty();
    }

    @Override
    public List<User> getActiveUsers() {
        return list("status", Status.ACTIVE);
    }
}
