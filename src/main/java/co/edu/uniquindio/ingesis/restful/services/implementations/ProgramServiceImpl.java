package co.edu.uniquindio.ingesis.restful.services.implementations;


import co.edu.uniquindio.ingesis.restful.domain.Program;
import co.edu.uniquindio.ingesis.restful.dtos.programs.ProgramCreationRequest;
import co.edu.uniquindio.ingesis.restful.dtos.programs.ProgramResponse;
import co.edu.uniquindio.ingesis.restful.dtos.programs.UpdateProgramRequest;
import co.edu.uniquindio.ingesis.restful.exceptions.usuarios.ResourceNotFoundException;
import co.edu.uniquindio.ingesis.restful.mappers.ProgramMapper;
import co.edu.uniquindio.ingesis.restful.repositories.interfaces.ProgramRepository;
import co.edu.uniquindio.ingesis.restful.services.interfaces.ProgramService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
@RequiredArgsConstructor
public class ProgramServiceImpl implements ProgramService {

    @Inject
    ProgramMapper programMapper;
    final ProgramRepository programRepository;

    @Override
    public List<ProgramResponse> findProgramsByUserId(Long userId) {
        // Se buscan los programas en base al id del usuario en la base de datos
        List<Program> programs = programRepository.findByUserId(userId);

        // Convertir la lista de entidades en una lista de respuestas DTO
        return programs.stream()
                .map(programMapper::toProgramResponse)
                .collect( Collectors.toList());
    }

    @Override
    public ProgramResponse getProgramById(Long id) {
        Program program = Program.findById(id);
        if( program == null ){
            new ResourceNotFoundException();
        }
        return programMapper.toProgramResponse(program);
    }

    @Override
    public ProgramResponse createProgram(ProgramCreationRequest request) {

        Program program = programMapper.parseOf(request);
        program.setCreationDate(LocalDate.now());
        program.persist();

        return programMapper.toProgramResponse(program);
    }

    @Override
    public ProgramResponse updateProgramById(Long id, UpdateProgramRequest request) {

        // Validar si el programa se encuentra en la base de datos
        Optional<Program> optionalProgram = programRepository.findByIdOptional(id);
        if (optionalProgram.isEmpty()) {
            new ResourceNotFoundException();
        }

        Program program = optionalProgram.get();
        program.setTitle(request.title());
        program.setDescription(request.description());
        program.setCode(request.code());
        program.setModificationDate(LocalDate.now());

        /*
        TODO: Revisar si poner aqui la lista de usuarios que se les comparte el programa o en otra parte
         */
        //program.setlist...

        program.persist();

        // Convertir entidad en DTO de respuesta
        return programMapper.toProgramResponse(program);
    }

    @Override
    public ProgramResponse deleteProgram(Long id) {
        // Validar si el programa se encuentra en la base de datos
        Optional<Program> optionalProgram = programRepository.findByIdOptional(id);
        if (optionalProgram.isEmpty()) {
            new ResourceNotFoundException();
        }

        // Obtener el programa y eliminarlo
        Program program = optionalProgram.get();
        program.delete();

        return programMapper.toProgramResponse(program);
    }
}
