package co.edu.uniquindio.ingesis.restful.steps;

import co.edu.uniquindio.ingesis.restful.domain.Type;
import co.edu.uniquindio.ingesis.restful.dtos.programs.ProgramCreationRequest;
import io.cucumber.java.en.*;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class ProgramStepDefinitions {

    private ProgramCreationRequest programCreationRequest;
    private Response response;

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

    @Given("Existe un programa con ID {int}")
    public void existeUnProgramaConID(int idEsperado) {

    }

    @When("hago una petición GET a programs{int} con un usuario con rol {string}")
    public void hagoUnaPeticiónGETAProgramsConUnUsuarioConRol(int idPrograma, String rolEstudiante) {
    }

    @And("el cuerpo debe contener el ID {int}")
    public void elCuerpoDebeContenerElID(int idPrograma) {

    }
}
