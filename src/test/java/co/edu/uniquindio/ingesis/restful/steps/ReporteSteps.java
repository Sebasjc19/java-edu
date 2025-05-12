package co.edu.uniquindio.ingesis.restful.steps;

import co.edu.uniquindio.ingesis.restful.dtos.reports.ReportCreationRequest;
import co.edu.uniquindio.ingesis.restful.dtos.usuarios.LoginRequest;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.hamcrest.Matchers.*;

public class ReporteSteps {

    private Response response;
    private ReportCreationRequest reportCreationRequest;
    private LoginRequest loginRequest;
    private String token;

    @Given("tengo los datos validos de un nuevo reporte, excepto el ID del profesor que no existe")
    public void datosReporteConProfesorInvalido(){
        reportCreationRequest= new ReportCreationRequest("Content", 1L, 0L);
    }

    @When("envio una solicitud POST a \"reports\"")
    public void envioUnaSolicitudPOST(){
        response = given()
                .baseUri("http://localhost:8080")
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(reportCreationRequest)
                .when()
                .post("/reports");
    }

    @Then("recibo un codigo de estado {int}")
    public void reciboUnCodigoDeEstado(int statusCode){
        response.then().statusCode(statusCode);
    }

    @And("el cuerpo contiene un mensaje explicando el error")
    public void elCuerpoContieneUnMensajeExplicandoElError(){
        response.then().body("respuesta", notNullValue());
    }

    @Given("tengo los datos validos de un nuevo reporte")
    public void tengoLosDatosValidosDeUnNuevoReporte(){
        reportCreationRequest= new ReportCreationRequest("Content", 1L, 1L);
    }

    @And("el cuerpo debe contener el contenido del reporte")
    public void elCuerpoDebeContenerElContenidoDelReporte(){
        response.then().body("content", notNullValue());
    }

    @Given("estoy autenticado como TUTOR")
    public void estoyAutenticadoComoTutor(){
        loginRequest = new LoginRequest("tutor@mail.com", "TutorPass12");
        response = given()
                .baseUri("http://localhost:8080")
                .contentType("application/json")
                .body(loginRequest)
                .when()
                .post("/auth");
        token = response.jsonPath().getString("respuesta.token");
        assertNotNull(token, "El token no debería ser nulo");
    }

    @When("envío una solicitud GET a \"reports professor 2\"")
    public void envioUnaSolicitudGet(){
        response = given()
                .baseUri("http://localhost:8080")
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(loginRequest)
                .when()
                .get("/reports/professor/1");
    }

    @And("el cuerpo contiene una lista de reportes")
    public void elCuerpoContieneUnaListaDeReportes(){
        response.then().body("content", notNullValue());
    }
}
