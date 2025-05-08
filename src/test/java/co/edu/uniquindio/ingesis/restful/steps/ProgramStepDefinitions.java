package co.edu.uniquindio.ingesis.restful.steps;

import co.edu.uniquindio.ingesis.restful.domain.Role;
import co.edu.uniquindio.ingesis.restful.domain.Type;
import co.edu.uniquindio.ingesis.restful.dtos.programs.ProgramCreationRequest;
import co.edu.uniquindio.ingesis.restful.dtos.usuarios.LoginRequest;
import co.edu.uniquindio.ingesis.restful.dtos.usuarios.UserRegistrationRequest;
import io.cucumber.java.en.*;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.Getter;

import java.time.LocalDate;
import java.util.UUID;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class ProgramStepDefinitions {

    private final UserStepDefinitions userSteps = new UserStepDefinitions();
    private ProgramCreationRequest programCreationRequest;
    private Response response;
    @Getter
    private Long programId;
    private Long lastProgramId;
    Long tutorId;
    String tutorToken;

    @Given("Tengo los datos válidos para crear un programa")
    public void tengoLosDatosVálidosParaCrearUnPrograma() {
        programCreationRequest = new ProgramCreationRequest(
                "Ingeniería de Software",
                "este es un programa de java",
                "System.out.println(holaaa)",
                Type.NORMAL,
                1L
        );
    }

    @When("hago una petición POST a programs")
    public void hagoUnaPeticiónPOSTAPrograms() {
        response = given()
                .baseUri("http://localhost:8080")
                .contentType("application/json")
                .body(programCreationRequest)
                .when()
                .post("/programs");

        programId = response.jsonPath().getLong("id");  // <-- ID del programa
    }

    @Then("la respuesta debe tener código de estado {int}")
    public void laRespuestaDebeTenerCódigoDeEstado(int statusCode) {
        response.then().statusCode(statusCode);
    }

    @And("el cuerpo debe contener el nombre del programa {string}")
    public void elCuerpoDebeContenerElNombreDelPrograma(String nombreEsperado) {
        // El nombre esperado es "Ingeniería de Software" tal y como está en program.feature
        response.then().body("title", equalToIgnoringCase(nombreEsperado));
    }

    // ----------------------------------------------------
    @Given("existe un usuario con rol {string} autenticado")
    public void existeUnUsuarioConRolAutenticado(String rolUsuario) {
        Role rol = Role.valueOf(rolUsuario.toUpperCase());
        userSteps.crearYLoggearUsuarioConRol(rol);
    }

    @And("creo un programa válido")
    public void creoUnProgramaVálido() {

        programCreationRequest = new ProgramCreationRequest(
                "Ingeniería de Prueba",
                "Un programa para pruebas",
                "System.out.println('Prueba');",
                Type.NORMAL,
                userSteps.getUserId() // Usa el ID del usuario
        );

        response = given()
                .baseUri("http://localhost:8080")
                .contentType("application/json")
                .auth().oauth2(userSteps.getJwtToken())
                .body(programCreationRequest)
                .when()
                .post("/programs");

        lastProgramId = response.jsonPath().getLong("id");
    }

    @When("hago una petición GET al programa recién creado")
    public void hagoUnaPeticiónGETAlProgramaReciénCreado() {
        response = given()
                .baseUri("http://localhost:8080")
                .auth().oauth2(userSteps.getJwtToken())
                .when()
                .get("/programs/" + lastProgramId);
    }

    @And("el cuerpo debe contener el ID del programa")
    public void elCuerpoDebeContenerElIDDelPrograma() {
        response.then().body("id", notNullValue());
    }

    // ----------------------------------------------------

    @Given("existe un tutor con programas asignados")
    public void existeUnTutorConProgramasAsignados() {
        // Se crea el tutor
        UserRegistrationRequest nuevoTutor = new UserRegistrationRequest(
                "tutor_mario_castañeda",
                "tutor@example.com",
                "Salem2004",
                "1234567890",
                LocalDate.of(2000, 5, 20),
                1L,
                Role.TUTOR
        );

        Response crearUsuarioResponse = given()
                .baseUri("http://localhost:8080")
                .contentType("application/json")
                .body(nuevoTutor)
                .when()
                .post("/users");

        crearUsuarioResponse.then().statusCode(201);

        tutorId = crearUsuarioResponse.jsonPath().getLong("id");

        // Se autentica y obtiene el token
        LoginRequest loginRequest = new LoginRequest(
                nuevoTutor.email(),
                "Salem2004" // misma que usaste en el registro
        );

        // Hacer la solicitud POST al endpoint de login
        Response loginResponse = given()
                .baseUri("http://localhost:8080")
                .contentType(ContentType.JSON)
                .body(loginRequest) // Enviar el loginRequest en el cuerpo de la solicitud
                .when()
                .post("/auth"); // Ruta del endpoint de autenticación

        // Se extrae el token de la respuesta
        tutorToken = loginResponse.then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getString("respuesta.token"); // Ajusta el path al nombre correcto del token

        // Se crea un programa asociado a dicho tutor
        ProgramCreationRequest nuevoPrograma = new ProgramCreationRequest(
                "Programa de prueba",
                "Descripción de prueba",
                "System.out.println('Hola mundo');",
                Type.NORMAL,
                tutorId
        );

        Response crearProgramaResponse = given()
                .baseUri("http://localhost:8080")
                .contentType("application/json")
                .auth().oauth2(tutorToken)
                .body(nuevoPrograma)
                .when()
                .post("/programs");

        crearProgramaResponse.then().statusCode(201);
    }

    @When("consulto los programas del tutor autenticado")
    public void consultoLosProgramasDelTutorAutenticado() {
        response = given()
                .baseUri("http://localhost:8080")
                .contentType("application/json")
                .auth().oauth2(tutorToken)
                .when()
                .get("/programs/" + tutorId);
    }

    @And("el cuerpo debe contener una lista de programas")
    public void elCuerpoDebeContenerUnaListaDeProgramas() {
        response.then()
                .body("$", not(empty())); // Verifica que la respuesta sea una lista no vacía
    }
}
