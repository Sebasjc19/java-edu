package co.edu.uniquindio.ingesis.restful.services.interfaces;

import co.edu.uniquindio.ingesis.restful.dtos.programs.ProgramCreationRequest;
import co.edu.uniquindio.ingesis.restful.dtos.programs.ProgramResponse;
import co.edu.uniquindio.ingesis.restful.dtos.programs.UpdateProgramRequest;
import co.edu.uniquindio.ingesis.restful.exceptions.users.implementations.ResourceNotFoundException;

import java.io.IOException;
import java.util.List;

public interface ProgramService {
    List<ProgramResponse> findProgramsByUserId(Long userId);
    ProgramResponse getProgramById(Long id);
    ProgramResponse createProgram(ProgramCreationRequest request);
    ProgramResponse updateProgramById(Long id, UpdateProgramRequest request);
    ProgramResponse deleteProgram(Long id) throws ResourceNotFoundException;
    String executeProgram(Long id) throws InterruptedException, IOException;
}
