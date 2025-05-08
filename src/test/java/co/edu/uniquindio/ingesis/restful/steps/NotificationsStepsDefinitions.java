package co.edu.uniquindio.ingesis.restful.steps;

import io.cucumber.java.en.*;
import io.restassured.response.Response;

import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
public class NotificationsStepsDefinitions {

    private Response response;
    private Long notificacionIdExistente = 100L;
    private Long notificacionIdEliminable = 101L;

    @Given("existe un estudiante con ID {long} con al menos {int} notificaciones registradas")
    public void existeUnEstudianteConNotificaciones(Long studentId, int cantidad) {
        for (int i = 0; i < cantidad; i++) {
            given()
                    .baseUri("http://localhost:8080")
                    .contentType("application/json")
                    .body(Map.of("studentId", studentId, "message", "Notificación #" + i))
                    .when()
                    .post("/notifications")
                    .then()
                    .statusCode(200);
        }
    }

    @When("envío una solicitud GET a {string}")
    public void envioSolicitudGET(String endpoint) {
        response = given()
                .baseUri("http://localhost:8080")
                .when()
                .get(endpoint);
    }

    @Then("recibo un código de estado: {int}")
    public void reciboCodigoDeEstado(int statusCode) {
        response.then().statusCode(statusCode);
    }

    @And("el cuerpo contiene una lista de {int} notificaciones")
    public void cuerpoContieneListaDeNotificaciones(int cantidadEsperada) {
        response.then().body("size()", equalTo(cantidadEsperada));
    }

    @Given("existe una notificación con ID {long}")
    public void existeUnaNotificacionConID(Long id) {
        Map<String, Object> notificacion = Map.of(
                "studentId", 1L,
                "message", "Notificación existente"
        );
        response = given()
                .baseUri("http://localhost:8080")
                .contentType("application/json")
                .body(notificacion)
                .when()
                .post("/notifications");

        // Sobrescribimos el ID existente para garantizar la prueba
        notificacionIdExistente = response.jsonPath().getLong("id");
    }

    @And("el cuerpo contiene la información de la notificación con ID {long}")
    public void cuerpoContieneInformacionDeNotificacion(Long idEsperado) {
        response.then().body("id", equalTo(idEsperado.intValue()));
    }

    @Given("no existe una notificación con ID {long}")
    public void noExisteNotificacionConID(Long id) {
        // No se necesita hacer nada, se asume que el ID no existe
    }

    @And("el cuerpo contiene el mensaje {string}")
    public void elCuerpoContieneMensaje(String mensajeEsperado) {
        response.then().body("message", containsStringIgnoringCase(mensajeEsperado));
    }

    @Given("existe un usuario con ID {long}")
    public void existeUnUsuarioConID(Long idUsuario) {
        // Aquí deberías tener un endpoint para crear o asegurar un usuario
        given()
                .baseUri("http://localhost:8080")
                .contentType("application/json")
                .body(Map.of("id", idUsuario, "name", "Estudiante Ejemplo", "email", "test@example.com"))
                .when()
                .post("/students")
                .then()
                .statusCode(anyOf(is(200), is(201))); // Permitir creación o que ya exista
    }

    @When("envío una solicitud POST a {string} con el cuerpo:")
    public void envioPostConCuerpo(String endpoint, String cuerpoJson) {
        response = given()
                .baseUri("http://localhost:8080")
                .contentType("application/json")
                .body(cuerpoJson)
                .when()
                .post(endpoint);
    }

    @And("el sistema envía la notificación al correo del estudiante")
    public void sistemaEnvíaCorreo() {
        // Aquí asumes que hay un campo que indica envío o logs
        response.then().body("message", containsStringIgnoringCase("Bienvenido"));
    }

    @Given("existe una notificación con ID {long} que puede eliminarse")
    public void existeUnaNotificacionEliminable(Long id) {
        Map<String, Object> notificacion = Map.of(
                "studentId", 1L,
                "message", "Notificación eliminable"
        );
        response = given()
                .baseUri("http://localhost:8080")
                .contentType("application/json")
                .body(notificacion)
                .when()
                .post("/notifications");

        notificacionIdEliminable = response.jsonPath().getLong("id");
    }

    @When("envío una solicitud DELETE a {string}")
    public void envioSolicitudDELETE(String endpoint) {
        response = given()
                .baseUri("http://localhost:8080")
                .when()
                .delete(endpoint);
    }

    @And("el cuerpo contiene la información de la notificación eliminada")
    public void cuerpoContieneInformacionEliminada() {
        response.then().body("id", notNullValue());
    }
}

