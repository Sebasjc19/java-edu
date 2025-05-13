package co.edu.uniquindio.ingesis.restful.steps;

import co.edu.uniquindio.ingesis.restful.domain.Role;
import co.edu.uniquindio.ingesis.restful.dtos.usuarios.LoginRequest;
import co.edu.uniquindio.ingesis.restful.dtos.usuarios.UserRegistrationRequest;
import io.cucumber.java.en.*;
import io.restassured.response.Response;
import io.restassured.http.ContentType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class UserStepDefinitions {

    private Response response;
    @Getter
    private Long userId;
    private UserRegistrationRequest userRegistrationRequest;
    @Getter
    @Setter
    private String jwtToken; //  Aquí se almacena el token

    @Given("tengo los datos válidos de un nuevo usuario")
    public void tengoDatosUsuarioValido() {
        // Paso informativo
        userRegistrationRequest = new UserRegistrationRequest(
                "juan_perez",                         // username
                "juan.perez@example.com",             // email
                "Password123",                         // password
                "1234567890",                          // identificationNumber
                LocalDate.of(2000, 5, 20),             // birthDate
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

        response.prettyPrint();

        userId = response.jsonPath().getLong("id");
        System.out.println("ID del usuario creado: " + userId);
    }

    @Then("recibo un código de estado {int}")
    public void verificarCodigoEstado(int statusCode) {
        response.then().statusCode(statusCode);
    }

    @And("el cuerpo contiene la información del nuevo usuario")
    public void verificarCuerpoUsuario() {
        response.then().body("email", notNullValue());
    }

    //-------------------------
    public void crearYLoggearUsuarioConRol(Role rol) {
        userRegistrationRequest = new UserRegistrationRequest(
                "user_" + UUID.randomUUID(), // username aleatorio
                "user" + UUID.randomUUID() + "@example.com",
                "Password123",
                "1234567890",
                LocalDate.of(2000, 1, 1),
                rol
        );
        envioPostUsers("/users");
        loginUsuario();
    }

    private String obtenerAccessToken() {
        Map<String, String> loginData = new HashMap<>();
        loginData.put("email", userRegistrationRequest.email());
        loginData.put("password", userRegistrationRequest.password());

        return given()
                .baseUri("http://localhost:8080")
                .contentType("application/json")
                .body(loginData)
                .when()
                .post("/auth")
                .then()
                .statusCode(200)
                .extract()
                .path("respuesta.token"); // <- Extrae el token correctamente
    }

    public void loginUsuario() {
        // Crear un objeto LoginRequest con los datos del usuario
        LoginRequest loginRequest = new LoginRequest(
                userRegistrationRequest.email(),  // Usar el email de userRegistrationRequest
                userRegistrationRequest.password() // Usar la contraseña de userRegistrationRequest
        );

        // Hacer la solicitud POST al endpoint de login
        response = given()
                .baseUri("http://localhost:8080")
                .contentType(ContentType.JSON)
                .body(loginRequest) // Enviar el loginRequest en el cuerpo de la solicitud
                .when()
                .post("/auth"); // Ruta del endpoint de autenticación

        // Extraer el token de la respuesta (ajusta el path si es necesario)
        jwtToken = response.then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getString("respuesta.token"); // Ajusta el path al nombre correcto del token
    }
}
