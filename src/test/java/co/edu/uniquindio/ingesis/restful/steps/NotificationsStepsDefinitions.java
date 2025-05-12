package co.edu.uniquindio.ingesis.restful.steps;

import co.edu.uniquindio.ingesis.restful.domain.Role;
import co.edu.uniquindio.ingesis.restful.dtos.notifications.NotificationCreationRequest;
import co.edu.uniquindio.ingesis.restful.dtos.usuarios.LoginRequest;
import co.edu.uniquindio.ingesis.restful.dtos.usuarios.UserRegistrationRequest;
import io.cucumber.java.en.*;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
public class NotificationsStepsDefinitions {

    private final UserStepDefinitions userSteps = new UserStepDefinitions();
    private NotificationCreationRequest notificationCreationRequest;
    private Response response;
    @Getter
    private Long notificationId;
    private Long lastNotificationId;
    Long studentId;
    String studentToken;
//  ----------------------------------------------

    @Given("Tengo los datos validos para crear una notificacion")
    public void tengoLosDatosValidosParaCrearUnaNotificacion() {
        notificationCreationRequest = new NotificationCreationRequest(
                "Revisión de código",
                1L
        );
    }

    @When("hago una peticion POST a notifications")
    public void hagoUnaPeticionPOSTANotifications() {
        response = given()
                .baseUri("http://localhost:8080")
                .contentType("application/json")
                .body(notificationCreationRequest)
                .when()
                .post("/notifications");

        notificationId = response.jsonPath().getLong("id");
    }

    @Then("la respuesta debe tener código de estado {int}")
    public void laRespuestaDebeTenerCodigoDeEstado(int statusCode) {
        response.then().statusCode(statusCode);
    }

    @And("el cuerpo debe contener el message de la notificacion {string}")
    public void elCuerpoDebeContenerElMessageDeLaNotificacion(String nombreEsperado) {
        response.then().body("message", equalToIgnoringCase(nombreEsperado));
    }

// -------------------------------------

    @Given("Existe una notificacion con ID {int} de un estudiante")
    public void existeUnaNotificacionDeEstudianteConID(int id) {
        // Autenticarse como STUDENT
        userSteps.crearYLoggearUsuarioConRol(Role.STUDENT);
        Long studentUserId = userSteps.getUserId();

        // Consultar si ya existe la notificación
        Response getResponse = given()
                .baseUri("http://localhost:8080")
                .auth().oauth2(userSteps.getJwtToken())
                .when()
                .get("/notifications/" + id);

        if (getResponse.statusCode() == 404) {
            // Crear la notificación para el estudiante autenticado
            NotificationCreationRequest nuevaNoti = new NotificationCreationRequest(
                    "Notificación del estudiante",
                    studentUserId
            );

            Response postResponse = given()
                    .baseUri("http://localhost:8080")
                    .auth().oauth2(userSteps.getJwtToken())
                    .contentType("application/json")
                    .body(nuevaNoti)
                    .when()
                    .post("/notifications");

            postResponse.then().statusCode(201);
            lastNotificationId = postResponse.jsonPath().getLong("id");

        } else {
            lastNotificationId = (long) id;
        }
    }


    @When("hago una petición GET a notifications-{int} con un usuario con rol {string}")
    public void hagoUnaPeticionGETPorId(int id, String rol) {
        Role rolUsuario = Role.valueOf(rol.toUpperCase());
        userSteps.crearYLoggearUsuarioConRol(rolUsuario);

        response = given()
                .baseUri("http://localhost:8080")
                .auth().oauth2(userSteps.getJwtToken())
                .when()
                .get("/notifications/" + id);
    }

    @And("el cuerpo debe contener el ID {int}")
    public void elCuerpoDebeContenerElID(int idEsperado) {
        response.then().body("id", equalTo(idEsperado));
    }

// --------------------

    @Given("existe un {string} con notificaciones asignadas")
    public void existeUnUsuarioConNotificacionesAsignadas(String rolStr) {
        Role rol = Role.valueOf(rolStr.toUpperCase());

        UserRegistrationRequest nuevoUsuario = new UserRegistrationRequest(
                "usuario_notif_" + rolStr.toLowerCase(),
                "user@example.com",
                "Password123",
                "123456789",
                LocalDate.of(1995, 1, 1),
                1L,
                rol
        );

        Response crearUsuarioResponse = given()
                .baseUri("http://localhost:8080")
                .contentType("application/json")
                .body(nuevoUsuario)
                .when()
                .post("/users");

        crearUsuarioResponse.then().statusCode(201);

        studentId = crearUsuarioResponse.jsonPath().getLong("id");

        LoginRequest loginRequest = new LoginRequest(nuevoUsuario.email(), "Password123");

        Response loginResponse = given()
                .baseUri("http://localhost:8080")
                .contentType(ContentType.JSON)
                .body(loginRequest)
                .when()
                .post("/auth");

        studentToken = loginResponse.then().statusCode(200)
                .extract().jsonPath().getString("respuesta.token");

        NotificationCreationRequest noti = new NotificationCreationRequest("Test notificación", studentId);

        given()
                .baseUri("http://localhost:8080")
                .contentType("application/json")
                .auth().oauth2(studentToken)
                .body(noti)
                .when()
                .post("/notifications")
                .then()
                .statusCode(201);
    }

    @When("hago una petición GET a notifications-2 con un usuario con rol {string}")
    public void hagoUnaPeticionGETTodasLasNotificaciones(String rol) {
        response = given()
                .baseUri("http://localhost:8080")
                .auth().oauth2(studentToken)
                .when()
                .get("/notifications/" + studentId);
    }

    @And("el cuerpo debe ser una lista")
    public void elCuerpoDebeSerUnaLista() {
        response.then().body("$", not(empty()));
    }

}

