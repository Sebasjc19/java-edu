package co.edu.uniquindio.ingesis.restful.services.implementations;


import co.edu.uniquindio.ingesis.restful.domain.Program;
import co.edu.uniquindio.ingesis.restful.domain.User;
import co.edu.uniquindio.ingesis.restful.dtos.programs.ProgramCreationRequest;
import co.edu.uniquindio.ingesis.restful.dtos.programs.ProgramResponse;
import co.edu.uniquindio.ingesis.restful.dtos.programs.UpdateProgramRequest;
import co.edu.uniquindio.ingesis.restful.exceptions.programs.ProgramNotFoundExceptionMapper;
import co.edu.uniquindio.ingesis.restful.exceptions.users.ResourceNotFoundException;
import co.edu.uniquindio.ingesis.restful.exceptions.users.UserNotFoundExceptionMapper;
import co.edu.uniquindio.ingesis.restful.mappers.ProgramMapper;
import co.edu.uniquindio.ingesis.restful.repositories.interfaces.ProgramRepository;
import co.edu.uniquindio.ingesis.restful.repositories.interfaces.UserRepository;
import co.edu.uniquindio.ingesis.restful.services.interfaces.ProgramService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.io.*;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
@RequiredArgsConstructor
public class ProgramServiceImpl implements ProgramService {

    @Inject
    ProgramMapper programMapper;
    @Inject
    ProgramRepository programRepository;
    @Inject
    UserRepository userRepository;


    @Override
    public List<ProgramResponse> findProgramsByUserId(Long userId) {
        Optional<User> userOptional = userRepository.findByIdOptional(userId);
        if (userOptional.isEmpty()) {
            new UserNotFoundExceptionMapper();
        }
        // Se buscan los programas en base al id del usuario en la base de datos
        List<Program> programs = programRepository.findByUserId(userId);

        // Convertir la lista de entidades en una lista de respuestas DTO
        return programs.stream()
                .map(programMapper::toProgramResponse)
                .collect( Collectors.toList());
    }

    @Override
    public ProgramResponse getProgramById(Long id) {
        Optional<Program> programOptional = programRepository.findByIdOptional(id);
        if (programOptional.isEmpty()) {
            new ProgramNotFoundExceptionMapper();
        }
        Program program = programOptional.get();
        return programMapper.toProgramResponse(program);
    }

    @Override
    @Transactional
    public ProgramResponse createProgram(ProgramCreationRequest request) {
        Program program = programMapper.parseOf(request);
        program.setCreationDate(LocalDate.now());
        program.persist();

        return programMapper.toProgramResponse(program);
    }

    @Override
    @Transactional
    public ProgramResponse updateProgramById(Long id, UpdateProgramRequest request) {

        // Validar si el programa se encuentra en la base de datos
        Optional<Program> optionalProgram = programRepository.findByIdOptional(id);
        if (optionalProgram.isEmpty()) {
            new ProgramNotFoundExceptionMapper();
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
    @Transactional
    public ProgramResponse deleteProgram(Long id)  {
        // Validar si el programa se encuentra en la base de datos
        Optional<Program> optionalProgram = programRepository.findByIdOptional(id);
        if (optionalProgram.isEmpty()) {
            new ProgramNotFoundExceptionMapper();
        }

        // Obtener el programa y eliminarlo
        Program program = optionalProgram.get();
        program.delete();

        return programMapper.toProgramResponse(program);
    }
    @Override
    public String executeProgram(Long programId) throws IOException, InterruptedException, ResourceNotFoundException {
        // Buscar el programa en la base de datos
        Optional<Program> optionalProgram = programRepository.findByIdOptional(programId);
        if (optionalProgram.isEmpty()) {
            throw new ResourceNotFoundException();
        }

        Program program = optionalProgram.get();
        String codigoFuente = program.getCode();

        if (codigoFuente == null || codigoFuente.isBlank()) {
            throw new IllegalArgumentException("El código fuente está vacío.");
        }

        // Crear un directorio temporal
        File directorioTemporal = Files.createTempDirectory("programaEjecutado").toFile();
        File archivoCodigo = new File(directorioTemporal, program.getTitle());

        // Guardar el código en un archivo
        try (FileWriter escritor = new FileWriter(archivoCodigo)) {
            escritor.write(codigoFuente);
        }

        // Compilar el archivo .java
        Process procesoCompilacion = new ProcessBuilder("javac", archivoCodigo.getAbsolutePath())
                .directory(directorioTemporal)
                .redirectErrorStream(true)
                .start();

        procesoCompilacion.waitFor();

        if (procesoCompilacion.exitValue() != 0) {
            return "Error en la compilación: " + obtenerSalida(procesoCompilacion);
        }

        // Ejecutar el programa compilado
        Process procesoEjecucion = new ProcessBuilder("java", "-cp", directorioTemporal.getAbsolutePath(), "Programa")
                .directory(directorioTemporal)
                .redirectErrorStream(true)
                .start();

        procesoEjecucion.waitFor();

        // Obtener la salida de la ejecución
        return obtenerSalida(procesoEjecucion);
    }

    private String obtenerSalida(Process proceso) throws IOException {
        try (BufferedReader lector = new BufferedReader(new InputStreamReader(proceso.getInputStream()))) {
            return lector.lines().collect(Collectors.joining("\n"));
        }
    }

}
