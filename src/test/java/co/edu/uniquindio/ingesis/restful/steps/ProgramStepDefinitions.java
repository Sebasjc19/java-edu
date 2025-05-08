package co.edu.uniquindio.ingesis.restful.steps;

import co.edu.uniquindio.ingesis.restful.domain.Role;
import co.edu.uniquindio.ingesis.restful.domain.Type;
import co.edu.uniquindio.ingesis.restful.dtos.programs.ProgramCreationRequest;
import io.cucumber.java.en.*;
import io.restassured.response.Response;
import lombok.Getter;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class ProgramStepDefinitions {

    private final UserStepDefinitions userSteps = new UserStepDefinitions();
    private ProgramCreationRequest programCreationRequest;
    private Response response;
    @Getter
    private Long programId;
    private Long lastProgramId;

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

    // ----------------------------
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
}
