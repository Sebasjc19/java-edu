package co.edu.uniquindio.ingesis.restful.steps;
import co.edu.uniquindio.ingesis.restful.domain.Role;
import co.edu.uniquindio.ingesis.restful.dtos.comments.CommentCreationRequest;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import io.cucumber.datatable.DataTable;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals; // O AssertJ

public class CommentStepsDefinitions {

    private final UserStepDefinitions userSteps = new UserStepDefinitions();
    private CommentCreationRequest commentCreationRequest;
    private Response response;
    @Getter
    private Long commentId;
    private Long lastCommentId;

    @Given("Tengo los datos válidos para crear un comentario")
    public void tengoLosDatosValidosParaCrearUnComentario() {
        commentCreationRequest = new CommentCreationRequest(
                "Excelente contenido", LocalDate.now(),1L,
                1L
        );
    }

    @When("hago una petición POST a comments")
    public void hagoUnaPeticionPOSTAComments() {
        response = given()
                .baseUri("http://localhost:8080")
                .contentType("application/json")
                .body(commentCreationRequest)
                .when()
                .post("/comments");

        commentId = response.jsonPath().getLong("id");
    }

    @Then("la respuesta debe tener código de estado: {int}")
    public void laRespuestaDebeTenerCodigoDeEstado(int statusCode) {
        response.then().statusCode(statusCode);
    }

    @And("el cuerpo debe contener el texto del comentario {string}")
    public void elCuerpoDebeContenerElTextoDelComentario(String textoEsperado) {
        response.then().body("content", equalToIgnoringCase(textoEsperado));
    }

    // ----------------------------

    @Given("existe un usuario con rol {string} autenticado")
    public void existeUnUsuarioConRolAutenticado(String rolUsuario) {
        Role rol = Role.valueOf(rolUsuario.toUpperCase());
        userSteps.crearYLoggearUsuarioConRol(rol);
    }

    @And("creo un comentario válido")
    public void creoUnComentarioValido() {
        commentCreationRequest = new CommentCreationRequest(
                "Muy bien explicado",
                LocalDate.now(),
                userSteps.getUserId(),
                1L
        );

        response = given()
                .baseUri("http://localhost:8080")
                .contentType("application/json")
                .auth().oauth2(userSteps.getJwtToken())
                .body(commentCreationRequest)
                .when()
                .post("/comments");

        lastCommentId = response.jsonPath().getLong("id");
    }

    @When("hago una petición GET al comentario recién creado")
    public void hagoUnaPeticionGETAlComentarioRecienCreado() {
        response = given()
                .baseUri("http://localhost:8080")
                .auth().oauth2(userSteps.getJwtToken())
                .when()
                .get("/comments/" + lastCommentId);
    }

    @And("el cuerpo debe contener el ID del comentario")
    public void elCuerpoDebeContenerElIDDelComentario() {
        response.then().body("id", notNullValue());
    }

    @Given("El usuario con ID {int} tiene comentarios asignados")
    public void elUsuarioConIdTieneComentariosAsignados(int userId) {
        // Aquí podrías crear comentarios manualmente para el usuario con ID dado si es necesario
    }

    @When("hago una petición GET a comments-{int} con un usuario con rol {string}")
    public void hagoUnaPeticionGETACommentsConRol(int userId, String rolUsuario) {
        Role rol = Role.valueOf(rolUsuario.toUpperCase());
        userSteps.crearYLoggearUsuarioConRol(rol);

        response = given()
                .baseUri("http://localhost:8080")
                .auth().oauth2(userSteps.getJwtToken())
                .when()
                .get("/comments/" + userId);
    }

    @And("el cuerpo debe ser una lista")
    public void elCuerpoDebeSerUnaLista() {
        response.then().body("", is(not(empty())));
    }

    @Given("Existe un comentario con ID {int} y datos nuevos válidos")
    public void existeUnComentarioConIdYDatosNuevosValidos(int id) {
        commentCreationRequest = new CommentCreationRequest(
                "Comentario actualizado",
                LocalDate.now(),
                userSteps.getUserId(),
                1L
        );
        commentId = (long) id;
    }

    @When("hago una petición PUT a comments-{int}")
    public void hagoUnaPeticionPUTAComments(int id) {
        response = given()
                .baseUri("http://localhost:8080")
                .auth().oauth2(userSteps.getJwtToken())
                .contentType("application/json")
                .body(commentCreationRequest)
                .when()
                .put("/comments/" + id);
    }

    @And("el cuerpo debe reflejar los datos actualizados")
    public void elCuerpoDebeReflejarLosDatosActualizados() {
        response.then().body("content", equalToIgnoringCase(commentCreationRequest.content()));
    }

    @Given("Existe un comentario con ID {int}")
    public void existeUnComentarioConID(int id) {
        commentId = (long) id;
    }

    @When("hago una petición DELETE a comments-{int}")
    public void hagoUnaPeticionDELETEAComments(int id) {
        response = given()
                .baseUri("http://localhost:8080")
                .auth().oauth2(userSteps.getJwtToken())
                .when()
                .delete("/comments/" + id);
    }

    @Given("Existe un comentario ejecutable con ID {int}")
    public void existeUnComentarioEjecutableConID(int id) {
        commentId = (long) id;
    }

    @When("hago una petición GET a comments-execute-{int}")
    public void hagoUnaPeticionGETACommentsExecute(int id) {
        response = given()
                .baseUri("http://localhost:8080")
                .auth().oauth2(userSteps.getJwtToken())
                .when()
                .get("/comments/execute/" + id);
    }

    @And("el cuerpo debe contener el texto {string}")
    public void elCuerpoDebeContenerElTexto(String texto) {
        response.then().body(containsString(texto));
    }
}
