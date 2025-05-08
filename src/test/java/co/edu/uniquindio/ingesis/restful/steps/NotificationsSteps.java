package co.edu.uniquindio.ingesis.restful.steps;

import io.restassured.http.ContentType;
import io.cucumber.java.en.*;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class NotificationsSteps {
    private Response response;
    private Long notificationId;
    private Long studentId;

    // ------------------- Consultar notificaciones por estudiante -------------------

    @Given("existe un estudiante con ID {int} con al menos {int} notificaciones registradas")
    public void existeEstudianteConNotificaciones(int id, int cantidad) {
        studentId = (long) id;
        // Paso informativo: se asume que el estudiante tiene la cantidad de notificaciones requeridas en la BD
    }

    @When("envío una solicitud GET a {string}")
    public void envioGetNotificacionesPorEstudiante(String url) {
        response = given()
                .accept(ContentType.JSON)
                .when()
                .get(url);
    }

    @Then("recibo un código de estado {int}")
    public void reciboCodigoEstado(int statusCode) {
        response.then().statusCode(statusCode);
    }

    @And("el cuerpo contiene una lista de {int} notificaciones")
    public void cuerpoContieneListaDeNotificaciones(int cantidad) {
        response.then().body("size()", equalTo(cantidad));
    }

    @And("el cuerpo contiene una lista vacía")
    public void cuerpoContieneListaVacia() {
        response.then().body("$", empty());
    }

    // ------------------- Obtener notificación por ID -------------------

    @Given("existe una notificación con ID {int}")
    public void existeNotificacionConId(int id) {
        notificationId = (long) id;
    }

    @When("envío una solicitud GET a {string}")
    public void envioGetNotificacionPorId(String url) {
        response = given()
                .accept(ContentType.JSON)
                .when()
                .get(url);
    }

    @And("el cuerpo contiene la información de la notificación con ID {int}")
    public void cuerpoContieneNotificacionConId(int id) {
        response.then().body("id", equalTo(id));
    }

    @Given("no existe una notificación con ID {int}")
    public void noExisteNotificacionConId(int id) {
        notificationId = (long) id;
    }

    @And("el cuerpo contiene el mensaje {string}")
    public void cuerpoContieneMensajeDeError(String mensaje) {
        response.then().body("message", containsString(mensaje));
    }

    // ------------------- Enviar notificación -------------------

    @Given("existe un usuario con ID {int}")
    public void existeUsuarioConId(int id) {
        studentId = (long) id;
    }

    @When("envío una solicitud POST a {string} con el cuerpo:")
    public void envioPostNotificacion(String url, String body) {
        response = given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post(url);
    }

    @And("el sistema envía la notificación al correo del estudiante")
    public void sistemaEnviaNotificacion() {
        // Verificación simple de respuesta exitosa
        response.then().statusCode(200);
    }

    @Given("no existe un usuario con ID {int}")
    public void noExisteUsuarioConId(int id) {
        studentId = (long) id;
    }

    // ------------------- Eliminar notificación -------------------

    @When("envío una solicitud DELETE a {string}")
    public void envioDeleteNotificacion(String url) {
        response = given()
                .when()
                .delete(url);
    }

    @And("el cuerpo contiene la información de la notificación eliminada")
    public void cuerpoContieneNotificacionEliminada() {
        response.then().body("id", notNullValue());
    }
}
