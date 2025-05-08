package co.edu.uniquindio.ingesis.restful.steps;

import co.edu.uniquindio.ingesis.restful.domain.Role;
import co.edu.uniquindio.ingesis.restful.dtos.usuarios.UserRegistrationRequest;
import io.cucumber.java.en.*;
import io.restassured.response.Response;
import io.restassured.http.ContentType;

import java.time.LocalDate;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class UserStepDefinitions {

    private Response response;
    private Long userId;
    private UserRegistrationRequest userRegistrationRequest;

    @Given("tengo los datos válidos de un nuevo usuario")
    public void tengoDatosUsuarioValido() {
        // Paso informativo
        userRegistrationRequest = new UserRegistrationRequest(
                "juan_perez",                         // username
                "juan.perez@example.com",             // email
                "Password123",                         // password
                "1234567890",                          // identificationNumber
                LocalDate.of(2000, 5, 20),             // birthDate
                9007199254740991L,                                    // idGroup
                Role.STUDENT                           // role (puede ser null, default es STUDENT)
        );
    }

    @When("envío una solicitud POST a {string}")
    public void envioPostUsers(String url) {
        response = given()
                .baseUri("http://localhost:8080")
                .contentType("application/json")
                .body(userRegistrationRequest)
                .when()
                .post(url);
    }

    @Then("recibo un código de estado {int}")
    public void verificarCodigoEstado(int statusCode) {
        response.then().statusCode(statusCode);
    }

    @And("el cuerpo contiene la información del nuevo usuario")
    public void verificarCuerpoUsuario() {
        response.then().body("email", notNullValue());
    }
}
