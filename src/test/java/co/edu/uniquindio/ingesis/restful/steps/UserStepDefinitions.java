package co.edu.uniquindio.ingesis.restful.steps;

import co.edu.uniquindio.ingesis.restful.domain.Role;
import co.edu.uniquindio.ingesis.restful.dtos.usuarios.LoginRequest;
import co.edu.uniquindio.ingesis.restful.dtos.usuarios.UserRegistrationRequest;
import co.edu.uniquindio.ingesis.restful.dtos.usuarios.UserUpdateRequest;
import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.http.ContentType;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Optional;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserStepDefinitions {

    private Response response;
    private UserRegistrationRequest datosUsuarioValido;
    private UserUpdateRequest datosUsuarioValidoActualizar;
    @Getter
    private Long userId;
    @Getter
    private String token;

    @Given("tengo los datos válidos de un nuevo usuario")
    public void datosUsuarioValido() {
        LocalDate localDate = LocalDate.now();
        datosUsuarioValido = new UserRegistrationRequest(
                "prueba123","prueba123@mail.com","Contrasenia123!",
                "1234567", localDate/*Esta fecha es para probar unicamente*/, Role.STUDENT
        );
        System.out.println("Datos usuario: \n"+datosUsuarioValido.toString());
    }

    @When("envío una solicitud POST a {string}")
    public void envioPostUsers(String url) {
        // Enviar la solicitud POST utilizando RestAssured
        response = RestAssured.given()
                .contentType(ContentType.JSON) // Especificamos que el cuerpo es JSON
                .body(datosUsuarioValido) // Enviamos el objeto con los datos del usuario
                .when()
                .post("/users"); // La ruta del endpoint
        userId = response.jsonPath().getLong("id");
        System.out.println("Respuesta: \n"+response.body().asString());
    }

    @Then("recibo un código de estado {int}")
    public void verificarCodigoEstado(int statusCode) {
        // Verificamos que la respuesta tenga un código de estado 201
        assertEquals(statusCode, response.getStatusCode());
    }

    @And("el cuerpo contiene la información del nuevo usuario")
    public void verificarCuerpoUsuario() {
        response.then().body("username", notNullValue());
    }

    //////

    @Given("existe un usuario con ID {int}")
    public void existeUsuarioConId(int id) {
        userId = (long) id;
        System.out.println("Id de usuario: " + userId);
    }

    @When("envío una solicitud GET del usuario")
    public void envioSolicitudGetA() {
        response = given()
                .accept(ContentType.JSON)
                .when()
                .get("/users/"+ userId);

        System.out.println("Respuesta: \n"+response.body().asString());
    }

    @And("el cuerpo contiene la información del usuario")
    public void verificarCuerpoGet() {
        response.then()
                .statusCode(200)
                .body("respuesta.username", equalTo("prueba123"));
    }

    @Given("existe un usuario con correo {string}")
    public void buscarUsuarioByEmail(String email) {
        //Hallamos el usuario con el correo
        response = given()
                .accept(ContentType.JSON)
                .when()
                .get("/users/email/" + email);

        userId = Long.valueOf(""+response.getBody().jsonPath().getInt("respuesta.id"));
        System.out.println("Id de usuario: " + userId);
    }

    @And("tiene datos a actualizar")
    public void tieneDatosAActualizar() {
        datosUsuarioValidoActualizar = new UserUpdateRequest(
                Optional.of("prueba321"),
                Optional.of("prueba321@email.com"),
                Optional.of("ClaveEjemplo123")
        );
    }

    @When("envio una solicitud PATCH a \\/users")
    public void envioUnaSolicitudPATCHAUsers() {
        //TOCA LOGUEARSE PORQUE EL PATCH NECESITA TOKEN

        response = given()
                .contentType(ContentType.JSON)
                .body(datosUsuarioValidoActualizar)
                .when()
                .patch("/users/"+userId);

        System.out.println("Respuesta PATCH:\n"+response.body().prettyPrint());
    }

    @And("el cuerpo contiene la información actualizada")
    public void verificarActualizacion() {
        response.then().body("respuesta.username", equalTo("prueba321"));
    }



    @When("envío una solicitud DELETE a \\/users")
    public void envioSolicitudDeleteA() {
        response = given()
                .when()
                .delete("/users/"+userId);

        System.out.println("Respuesta: \n"+response.body().prettyPrint());

    }

    @And("el cuerpo contiene el mensaje {string}")
    public void verificarMensaje(String mensaje) {
        response.then().body("respuesta", equalTo(mensaje));
    }

    @Given("soy un usuario con rol ADMIN")
    public void usuarioConRolAdmin() {
        loginUsuario("admin@mail.com","Contrasenia123!");
    }

    @When("envio una solicitud GET a \\/users")
    public void envioUnaSolicitudGETAUsers() {
        response = given()
                .header("Authorization", "Bearer " + token) // Aquí va tu token
                .accept(ContentType.JSON)
                .when()
                .get("/users");

        System.out.println("Respuesta: \n" + response.getBody().asPrettyString());
    }

    @When("envio una solicitud GET a \\/users\\/activos")
    public void envioUnaSolicitudGETAUsersActivos() {
        response = given()
                .header("Authorization", "Bearer " + token) // Aquí va tu token
                .accept(ContentType.JSON)
                .when()
                .get("/users/active");

        System.out.println("Respuesta: \n" + response.getBody().asPrettyString());
    }

    @And("el cuerpo contiene una lista de usuarios")
    public void verificarListaUsuarios() {
        response.then().body("", not(empty()));

    }


    public void loginUsuario(String correo, String password) {
        // Crear un objeto LoginRequest con los datos del usuario
        LoginRequest loginRequest = new LoginRequest(
                correo, password
        );

        // Hacer la solicitud POST al endpoint de login
        response = given()
                .baseUri("http://localhost:8080")
                .contentType(ContentType.JSON)
                .body(loginRequest) // Enviar el loginRequest en el cuerpo de la solicitud
                .when()
                .post("/auth"); // Ruta del endpoint de autenticación

        // Extraer el token de la respuesta (ajusta el path si es necesario)
        token = response.then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getString("respuesta.token"); // Ajusta el path al nombre correcto del token

        System.out.println("token: "+token);
    }



    public void crearYLoggearUsuarioConRol(Role rol) {
        datosUsuarioValido();
        envioPostUsers("/users");
        loginUsuario(datosUsuarioValido.email(), datosUsuarioValido.password());
    }

}