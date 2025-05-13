package co.edu.uniquindio.ingesis.restful.services.implementations;


import co.edu.uniquindio.ingesis.restful.domain.Program;
import co.edu.uniquindio.ingesis.restful.domain.User;
import co.edu.uniquindio.ingesis.restful.dtos.programs.ProgramCreationRequest;
import co.edu.uniquindio.ingesis.restful.dtos.programs.ProgramResponse;
import co.edu.uniquindio.ingesis.restful.dtos.programs.UpdateProgramRequest;
import co.edu.uniquindio.ingesis.restful.exceptions.users.implementations.ResourceNotFoundException;
import co.edu.uniquindio.ingesis.restful.mappers.ProgramMapper;
import co.edu.uniquindio.ingesis.restful.repositories.interfaces.ProgramRepository;
import co.edu.uniquindio.ingesis.restful.repositories.interfaces.UserRepository;
import co.edu.uniquindio.ingesis.restful.services.interfaces.ProgramService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
            throw new ResourceNotFoundException("Usuario no encontrado");
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
            throw  new ResourceNotFoundException("Programa no encontrado");
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
            throw  new ResourceNotFoundException("Programa no encontrado");
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
    public ProgramResponse deleteProgram(Long id) throws ResourceNotFoundException {
        // Validar si el programa se encuentra en la base de datos
        Optional<Program> optionalProgram = programRepository.findByIdOptional(id);
        if (optionalProgram.isEmpty()) {
            throw new ResourceNotFoundException("Programa no encontrado");
        }

        // Obtener el programa y eliminarlo
        Program program = optionalProgram.get();
        program.delete();

        return programMapper.toProgramResponse(program);
    }

    @Override
    public String executeProgram(Long id) throws InterruptedException, IOException {
        // Obtener el programa por su ID desde la base de datos
        Program program = programRepository.findById(id);
        if (program == null) {
            throw new NotFoundException("Programa no encontrado con ID: " + id);
        }

        // Guardar el código Java recibido en un archivo temporal
        String codigo = program.getCode();

        String className = extractClassName(codigo);
        System.out.println("Nombre de la clase:" + className);
        System.out.println("Codigo:\n"+codigo);


        // 1. Guardar el código en un archivo temporal
        Path tempDir = Files.createTempDirectory("java-code");
        Path javaFile = tempDir.resolve(className + ".java");
        Files.writeString(javaFile, codigo);

        System.out.println("Codigo en el archivo:\n"+Files.readString(javaFile));

        //Toca buscar la forma de añadirle el timeout y que funcione
        String dockerCmd = String.format(
                "docker run --rm -v %s:/app -w /app openjdk:11 sh -c \"javac %s.java && java %s\"",
                tempDir.toAbsolutePath(), className, className
        );
        return runJavaContainer(dockerCmd);

    }


    /**
     * Extrae el nombre de la clase pública del código fuente Java proporcionado.
     *
     * Este método busca una declaración de clase que siga el patrón: "public class NombreClase".
     * Si encuentra una coincidencia, devuelve el nombre de la clase. De lo contrario,
     * retorna "Program" como nombre por defecto.
     *
     * @param code El código fuente Java del cual se desea extraer el nombre de la clase.
     * @return El nombre de la clase pública encontrada o "Program" si no se encuentra ninguna.
     */
    private String extractClassName(String code) {
        // Define una expresión regular para buscar la declaración de una clase pública
        Pattern pattern = Pattern.compile("public\\s+class\\s+(\\w+)");

        // Aplica la expresión regular sobre el código
        Matcher matcher = pattern.matcher(code);

        // Si encuentra un match, devuelve el grupo 1 (nombre de la clase)
        // De lo contrario, retorna "Program" como valor por defecto
        return matcher.find() ? matcher.group(1) : "Program";
    }


    private String runJavaContainer(String cmd) throws IOException, InterruptedException {
        boolean isWindows = System.getProperty("os.name").toLowerCase().contains("win");

        ProcessBuilder pb;
        if (isWindows) {
            pb = new ProcessBuilder("cmd.exe", "/c", cmd);
        } else {
            pb = new ProcessBuilder("sh", "-c", cmd);
        }

        pb.redirectErrorStream(true);
        Process process = pb.start();

        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
        }

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            if (output.toString().contains("timeout")) {
                output.append("\nEl proceso fue detenido por exceder el tiempo límite de ejecución.");
            } else {
                output.append("\nProceso terminó con error. Código: ").append(exitCode);
            }
        }

        return output.toString();
    }

}
