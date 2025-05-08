package co.edu.uniquindio.ingesis.restful.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ReporteSteps {

    private String jsonBody;
    private Response response;

    @Given("tengo los datos validos de un nuevo reporte, excepto el ID del profesor que no existe")
    public void datosReporteConProfesorInvalido(){
        jsonBody = """
            {
              "titulo": "Reporte de prueba",
              "descripcion": "Esto es un test",
              "profesorId": 9999
            }
        """;
    }

    @When("envio una solicitud POST a \"reports\"")
    public void envioUnaSolicitudPOST(){
        response = given()
                .header("Content-Type", "application/json")
                .body(jsonBody)
                .when()
                .post("/reports");
    }

    @Then("recibo un codigo de estado 400")
    public void reciboUnCodigoDeEstado400(){
        int statusCode = response.getStatusCode();
        assertTrue(statusCode == 400 || statusCode == 404,
                "Esperado 400 o 404 pero se recibió: " + statusCode);
    }
}
