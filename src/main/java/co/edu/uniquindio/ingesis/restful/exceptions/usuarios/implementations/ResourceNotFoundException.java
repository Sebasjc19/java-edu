package co.edu.uniquindio.ingesis.restful.exceptions.usuarios.implementations;

import co.edu.uniquindio.ingesis.restful.dtos.MessageDTO;
import co.edu.uniquindio.ingesis.restful.dtos.usuarios.ErrorResponse;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;

public class ResourceNotFoundException extends RuntimeException {
   public ResourceNotFoundException(String message){
       super(message);
   }
}
