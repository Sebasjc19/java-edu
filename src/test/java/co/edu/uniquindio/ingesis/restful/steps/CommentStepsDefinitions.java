package co.edu.uniquindio.ingesis.restful.steps;
import co.edu.uniquindio.ingesis.restful.domain.Role;
import co.edu.uniquindio.ingesis.restful.dtos.comments.CommentCreationRequest;
import co.edu.uniquindio.ingesis.restful.dtos.comments.UpdateCommentRequest;
import co.edu.uniquindio.ingesis.restful.dtos.programs.UpdateProgramRequest;
import co.edu.uniquindio.ingesis.restful.dtos.usuarios.LoginRequest;
import co.edu.uniquindio.ingesis.restful.dtos.usuarios.UserRegistrationRequest;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.Getter;
import java.time.LocalDate;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class CommentStepsDefinitions {

    private final UserStepDefinitions userSteps = new UserStepDefinitions();
    private CommentCreationRequest commentCreationRequest;
    private UpdateCommentRequest updateCommentRequest;
    private Response response;
    @Getter
    private Long commentId;
    private Long lastCommentId;
    String tutorToken;
    Long tutorId;
    

    @Given("Tengo los datos validos para crear un comentario")
    public void tengoLosDatosValidosParaCrearUnComentario() {
        commentCreationRequest = new CommentCreationRequest(
                "Excelente contenido",LocalDate.of(2000, 5, 20),
                1L,
                1L
        );
    }

    @When("hago una petición Post a comments")
    public void hagoUnaPeticionPOSTAComments() {
        response = given()
                .baseUri("http://localhost:8080")
                .contentType("application/json")
                .body(commentCreationRequest)
                .when()
                .post("/comments");

        commentId = response.jsonPath().getLong("id");
        System.out.println("ID del usuario creado: " + commentId);

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

    @And("creo un comentario valido")
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

    @And("el cuerpo debe contener el ID del comentario")
    public void elCuerpoDebeContenerElIDDelComentario() {
        response.then().body("id", notNullValue());
    }

    @Given("existe un usuario con comments asignados")
    public void elUsuarioConIdTieneComentariosAsignados() {
        // Se crea el tutor
        UserRegistrationRequest nuevoTutor = new UserRegistrationRequest(
                "tutor_mario_castañeda",
                "tutor@example.com",
                "Salem2004",
                "1234567890",
                LocalDate.of(2000, 5, 20),
                                Role.TUTOR
        );

        Response crearUsuarioResponse = given()
                .baseUri("http://localhost:8080")
                .contentType("application/json")
                .body(nuevoTutor)
                .when()
                .post("/users");

        crearUsuarioResponse.then().statusCode(201);

        tutorId = crearUsuarioResponse.jsonPath().getLong("id");

        // Se autentica y obtiene el token
        LoginRequest loginRequest = new LoginRequest(
                nuevoTutor.email(),
                "Salem2004" // misma que usaste en el registro
        );

        // Hacer la solicitud POST al endpoint de login
        Response loginResponse = given()
                .baseUri("http://localhost:8080")
                .contentType(ContentType.JSON)
                .body(loginRequest) // Enviar el loginRequest en el cuerpo de la solicitud
                .when()
                .post("/auth"); // Ruta del endpoint de autenticación

        // Se extrae el token de la respuesta
        tutorToken = loginResponse.then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getString("respuesta.token"); // Ajusta el path al nombre correcto del token

        // Se crea un programa asociado a dicho tutor
        CommentCreationRequest newComment = new CommentCreationRequest(
                "Buen contenido",
                LocalDate.of(2000,5,20),
                tutorId,
                1l
        );

        Response crearProgramaResponse = given()
                .baseUri("http://localhost:8080")
                .contentType("application/json")
                .auth().oauth2(tutorToken)
                .body(newComment)
                .when()
                .post("/programs");

        crearProgramaResponse.then().statusCode(201);
    }

    @When("consulto los comments del usuario autenticado")
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

    @Given("Existe un comentario y datos nuevos validos")
    public void existeUnComentarioConIdYDatosNuevosValidos(int id) {
        elUsuarioConIdTieneComentariosAsignados(); // Se reutiliza para crear el usuario y el programa asignado a este

        // Datos nuevos para actualizar
        updateCommentRequest = new UpdateCommentRequest(
                "Actualizacao de comment"
        );
    }

    @When("hago una petición PUT a {string}")
    public void hagoUnaPeticionPUTAComments(String ruta) {
        response = given()
                .baseUri("http://localhost:8080")
                .auth().oauth2(userSteps.getToken())
                .contentType("application/json")
                .body(updateCommentRequest)
                .when()
                .put(ruta);
    }

    @And("el cuerpo debe reflejar los datos actualizados")
    public void elCuerpoDebeReflejarLosDatosActualizados() {
        response.then().body("content", equalToIgnoringCase(commentCreationRequest.content()));
    }


    @Given("Existe un comentario con ID {int}")
    public void existeUnProgramaConID(int commentId) {
        existeUnUsuarioConRolAutenticado("TUTOR");

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
                .delete("/comments/" + id);
    }

}
