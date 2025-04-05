package co.edu.uniquindio.ingesis.restful.exceptions.usuarios.implementations;

public class InactiveUserException extends RuntimeException{
    public InactiveUserException(String message){
        super(message);
    }
}
