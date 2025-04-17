package co.edu.uniquindio.ingesis.restful.services.implementations;

import jakarta.enterprise.context.ApplicationScoped;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class JavaExecutionServiceImpl  {

    private static final List<String> INSTRUCCIONES_PROHIBIDAS = List.of(
            "System.exit", "Runtime.getRuntime", "exec(", "ProcessBuilder", "Thread.sleep", "while(true", "for(;;"
    );

    public String ejecutarCodigo(String codigo) throws IOException, InterruptedException, IOException {
        validarCodigo(codigo);

        Path tempDir = Files.createTempDirectory("usercode-");
        Path javaFile = tempDir.resolve("Main.java");

        Files.writeString(javaFile, codigo);

        // Compilar
        ProcessBuilder compilePb = new ProcessBuilder("javac", "Main.java");
        compilePb.directory(tempDir.toFile());
        compilePb.redirectErrorStream(true);
        Process compileProcess = compilePb.start();

        boolean compiled = compileProcess.waitFor(5, TimeUnit.SECONDS);
        String compileOutput = new String(compileProcess.getInputStream().readAllBytes());

        if (!compiled || compileProcess.exitValue() != 0) {
            return "❌ Error de compilación:\n" + compileOutput;
        }

        // Ejecutar
        ProcessBuilder runPb = new ProcessBuilder("java", "Main");
        runPb.directory(tempDir.toFile());
        runPb.redirectErrorStream(true);
        Process runProcess = runPb.start();

        boolean finished = runProcess.waitFor(5, TimeUnit.SECONDS);
        String runOutput = new String(runProcess.getInputStream().readAllBytes());

        if (!finished) {
            runProcess.destroyForcibly();
            return "⏱ Tiempo de ejecución excedido.";
        }

        return runOutput;
    }

    private void validarCodigo(String codigo) {
        for (String prohibido : INSTRUCCIONES_PROHIBIDAS) {
            if (codigo.contains(prohibido)) {
                throw new SecurityException("Instrucción no permitida: " + prohibido);
            }
        }
    }
}