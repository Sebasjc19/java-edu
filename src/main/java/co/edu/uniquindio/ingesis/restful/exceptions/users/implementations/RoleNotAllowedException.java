package co.edu.uniquindio.ingesis.restful.exceptions.users.implementations;

public class RoleNotAllowedException extends RuntimeException {
    public RoleNotAllowedException(String message) {
        super(message);
    }
}
