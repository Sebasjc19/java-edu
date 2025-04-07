package co.edu.uniquindio.ingesis.restful.exceptions.users.implementations;

public class ResourceNotFoundException extends RuntimeException {
   public ResourceNotFoundException(String message){
       super(message);
   }
}
