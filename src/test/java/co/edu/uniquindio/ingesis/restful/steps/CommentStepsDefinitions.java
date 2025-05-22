package co.edu.uniquindio.ingesis.restful.steps;
import co.edu.uniquindio.ingesis.restful.domain.Role;
import co.edu.uniquindio.ingesis.restful.dtos.comments.CommentCreationRequest;
import co.edu.uniquindio.ingesis.restful.dtos.comments.UpdateCommentRequest;
import co.edu.uniquindio.ingesis.restful.dtos.usuarios.LoginRequest;
import co.edu.uniquindio.ingesis.restful.dtos.usuarios.UserRegistrationRequest;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.Getter;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class CommentStepsDefinitions {

    private final UserStepDefinitions userSteps = new UserStepDefinitions();
    private final CommonStepsDefinitions commonSteps = new CommonStepsDefinitions();
    private CommentCreationRequest commentCreationRequest;
    private UpdateCommentRequest updateCommentRequest;
    private Response response;
    @Getter
    private Long commentId;
    private Long lastCommentId;
    String tutorToken;
    Long tutorId;


    @Given("existe un usuario para commentario con rol {string} autenticado")
    public void elUsuarioConIdTieneComentariosAsignados(String rolUsuario) {
        Role rol = Role.valueOf(rolUsuario.toUpperCase());
        userSteps.crearYLoggearUsuarioConRol(rol);
    }
    @When("consulto los comentarios del usuario autenticado")
    public void hagoUnaPeticionGETACommentsConRol() {
        response = given()
                .baseUri("http://localhost:8080")
                .contentType("application/json")
                .auth().oauth2(tutorToken)
                .when()
                .get("/comments/" + tutorId);
    }

    @And("el cuerpo debe ser una lista de comentarios")
    public void elCuerpoDebeSerUnaLista() {
        response.then().body("$", not(empty()));
    }



    @And("creo un comentario valido")
    public void creoUnComentarioValido() {
        Long userID = userSteps.getUserId();
        Response checkResponse = given()
                .baseUri("http://localhost:8080")
                .contentType("application/json")
                .auth().oauth2(userSteps.getToken())
                .when()
                .get("/comments/" + userID);
        if (checkResponse.statusCode() == 200) {
            // Se asume que la respuesta es una lista de objetos con campos "id"
            List<Map<String, Object>> comentarios = checkResponse.jsonPath().getList("");
            if (!comentarios.isEmpty()) {
                Integer id = (Integer) comentarios.get(0).get("id");
                lastCommentId = id.longValue();
                System.out.println("Ya existe un comentario con ID: " + lastCommentId);
                return;
            }
        } else if (checkResponse.statusCode() == 404) {
            System.out.println("No hay commentarios existentes para el usuario " + userID + ". Se procederá a crear uno.");
        } else {
            throw new RuntimeException("Error inesperado al consultar comentarios: " + checkResponse.statusCode());
        }

        commentCreationRequest = new CommentCreationRequest(
                "Muy bien explicado",
                LocalDate.now(),
                userSteps.getUserId(),
                1L
        );

        response = given()
                .baseUri("http://localhost:8080")
                .contentType("application/json")
                .auth().oauth2(userSteps.getToken())
                .body(commentCreationRequest)
                .when()
                .post("/comments");

        lastCommentId = response.jsonPath().getLong("id");
    }


    @When("hago una petición GET al comentario recién creado")
    public void hagoUnaPeticionGETAlComentarioRecienCreado() {
        response = given()
                .baseUri("http://localhost:8080")
                .auth().oauth2(userSteps.getToken())
                .when()
                .get("/comments/" + lastCommentId);
    }

    @And("el cuerpo debe contener el texto del comentario {string}")
    public void elCuerpoDebeContenerElTextoDelComentario(String textoEsperado) {
        response.then().body("content", equalToIgnoringCase(textoEsperado));
    }
    

    @Given("Tengo los datos validos para crear un comentario")
    public void tengoLosDatosValidosParaCrearUnComentario() {
        commentCreationRequest = new CommentCreationRequest(
                "Excelente contenido",LocalDate.of(2000, 5, 20),
                4L,
                1L
        );
    }

    @When("hago una petición Post a comments")
    public void hagoUnaPeticionPOSTAComments() {
        response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(commentCreationRequest)
                .when()
                .post("/comments");

        commentId = response.jsonPath().getLong("id");
        System.out.println("ID del comentario creado: " + commentId);

    }

    @Then("la respuesta debe tener código de estado {int}")
    public void laRespuestaDebeTenerCodigoDeEstado(int statusCode) {
        response.then().statusCode(statusCode);
    }

    @And("el cuerpo debe contener el content del comentario {string}")
    public void elCuerpoDebeContenerElIDDelComentario() {
        response.then().body("content", notNullValue());
    }


    // ----------------------------
    @Given("Existe un comentario y datos nuevos validos")
    public void existeUnComentarioConIdYDatosNuevosValidos() {
        elUsuarioConIdTieneComentariosAsignados("TUTOR");
        creoUnComentarioValido();// Se reutiliza para crear el usuario y el programa asignado a este
        // Datos nuevos para actualizar
        updateCommentRequest = new UpdateCommentRequest(
                "Actualizao de comment"
        );
    }

    @When("hago una peticion PUT, hacia la ruta {string}")
    public void hagoUnaPeticionPUTAComments(String ruta) {
        response = given()
                .baseUri("http://localhost:8080")
                .auth().oauth2(userSteps.getToken())
                .contentType("application/json")
                .body(updateCommentRequest)
                .when()
                .put(ruta + lastCommentId);
    }

    @And("el cuerpo debe reflejar los datos actualizados")
    public void elCuerpoDebeReflejarLosDatosActualizados() {
        response.then().body("content", equalToIgnoringCase(commentCreationRequest.content()));
    }

    @Given("Existe un comentario con ID {int}")
    public void existeUnProgramaConID(int commentId) {
        elUsuarioConIdTieneComentariosAsignados("TUTOR");

        Response getResponse = given()
                .baseUri("http://localhost:8080")
                .auth().oauth2(userSteps.getToken())  // Aquí añades el token
                .when()
                .get("/comments/" + commentId);

        if (getResponse.statusCode() == 404) {
            creoUnComentarioValido();
        }
    }
    @When("hago una petición DELETE a {string}")
    public void hagoUnaPeticionDELETEAComments(int id) {
        response = given()
                .baseUri("http://localhost:8080")
                .auth().oauth2(userSteps.getToken())
                .when()
                .delete("/comments/" + lastCommentId);
    }

}
