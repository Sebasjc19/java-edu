package co.edu.uniquindio.ingesis.restful.steps;

import io.cucumber.java.en.*;
import io.restassured.response.Response;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class UserStepDefinitions {

    private Response response;
    private Long userId;

    @Given("tengo los datos válidos de un nuevo usuario")
    public void tengoDatosUsuarioValido() {
        // Paso informativo
    }

    @When("envío una solicitud POST a {string}")
    public void envioPostUsers(String url) {
        String nuevoUsuarioJson = """
            {
                "name": "Juan",
                "email": "juan@correo.com",
                "password": "seguro123"
            }
        """;

        response = given()
                .contentType(ContentType.JSON)
                .body(nuevoUsuarioJson)
                .when()
                .post(url);

        userId = response.jsonPath().getLong("id");
    }

    @Then("recibo un código de estado {int}")
    public void verificarCodigoEstado(int statusCode) {
        response.then().statusCode(statusCode);
    }

    @And("el cuerpo contiene la información del nuevo usuario")
    public void verificarCuerpoUsuario() {
        response.then().body("id", notNullValue());
    }

    @Given("existe un usuario con ID {int}")
    public void existeUsuarioConId(int id) {
        userId = (long) id;
    }

    @When("envío una solicitud GET a {string}")
    public void envioSolicitudGetA(String url) {
        response = given()
                .accept(ContentType.JSON)
                .when()
                .get(url);
    }

    @And("el cuerpo contiene la información del usuario con ID {int}")
    public void verificarCuerpoGet(int id) {
        response.then().body("id", equalTo(id));
    }

    @Given("existe un usuario con ID {int} y datos actualizados")
    public void existeUsuarioParaActualizar(int id) {
        userId = (long) id;
    }

    @When("envío una solicitud PATCH a {string}")
    public void envioSolicitudPatchA(String url) {
        String jsonActualizacion = """
            {
                "name": "Juan Actualizado",
                "email": "juan.nuevo@correo.com"
            }
        """;

        response = given()
                .contentType(ContentType.JSON)
                .body(jsonActualizacion)
                .when()
                .patch(url);
    }

    @And("el cuerpo contiene la información actualizada")
    public void verificarActualizacion() {
        response.then().body("name", equalTo("Juan Actualizado"));
    }

    @When("envío una solicitud DELETE a {string}")
    public void envioSolicitudDeleteA(String url) {
        response = given()
                .when()
                .delete(url);
    }

    @And("el cuerpo contiene el mensaje {string}")
    public void verificarMensaje(String mensaje) {
        response.then().body("message", equalTo(mensaje));
    }

    @Given("soy un usuario con rol ADMIN")
    public void usuarioConRolAdmin() {
        // Aquí puedes generar un token JWT si estás usando autenticación
    }

    @And("el cuerpo contiene una lista de usuarios")
    public void verificarListaUsuarios() {
        response.then().body("data", not(empty()));
    }

    @And("el cuerpo contiene una lista de usuarios activos")
    public void verificarUsuariosActivos() {
        response.then().body("data", not(empty()));
    }
}
