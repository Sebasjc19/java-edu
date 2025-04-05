package co.edu.uniquindio.ingesis.restful.exceptions.usuarios.implementations;

public class UsernameAlreadyExistsException extends RuntimeException{
    public UsernameAlreadyExistsException(String message){
        super(message);
    }
}
