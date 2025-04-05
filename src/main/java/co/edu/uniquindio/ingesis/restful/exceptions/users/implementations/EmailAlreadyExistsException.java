package co.edu.uniquindio.ingesis.restful.exceptions.users.implementations;

public class EmailAlreadyExistsException extends RuntimeException{
    public EmailAlreadyExistsException(String message){
        super(message);
    }
}
