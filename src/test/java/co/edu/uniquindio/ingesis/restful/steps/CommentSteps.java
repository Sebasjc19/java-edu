package co.edu.uniquindio.ingesis.restful.steps;
import io.cucumber.java.en.*;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class CommentSteps {

    private Response response;
    private Long commentId;

    @Given("tengo los datos válidos para un nuevo comentario")
    public void tengoDatosValidosParaComentario() {
        // Paso informativo. Se usará en el paso de envío.
    }

    @When("envío una solicitud POST a {string} con el comentario")
    public void envioPostComentario(String url) {
        String comentarioJson = """
            {
                "professorId": 1,
                "programId": 1,
                "content": "Comentario de prueba desde test"
            }
        """;

        response = given()
                .contentType(ContentType.JSON)
                .body(comentarioJson)
                .when()
                .post(url);

        commentId = response.jsonPath().getLong("id");
    }

    @Then("el código de estado debe ser {int}")
    public void codigoDeEstadoDebeSer(int status) {
        response.then().statusCode(status);
    }

    @And("el cuerpo contiene la información del comentario creado")
    public void cuerpoContieneComentarioCreado() {
        response.then().body("id", notNullValue());
    }

    @Given("existe un comentario con ID {int}")
    public void existeComentarioConId(int id) {
        commentId = (long) id;
    }

    @When("envío una solicitud GET a {string}")
    public void envioGetComentario(String url) {
        response = given()
                .accept(ContentType.JSON)
                .when()
                .get(url);
    }

    @And("el cuerpo contiene los datos del comentario con ID {int}")
    public void cuerpoContieneComentarioConId(int id) {
        response.then().body("id", equalTo(id));
    }

    @When("envío una solicitud DELETE a {string}")
    public void envioDeleteComentario(String url) {
        response = given()
                .when()
                .delete(url);
    }

    @And("el cuerpo indica que el comentario fue eliminado")
    public void cuerpoIndicaComentarioEliminado() {
        response.then().body("deleted", equalTo(true));
    }

    @When("envío una solicitud PUT a {string} con el nuevo contenido")
    public void envioPutComentario(String url) {
        String jsonActualizado = """
            {
                "content": "Contenido actualizado desde prueba"
            }
        """;

        response = given()
                .contentType(ContentType.JSON)
                .body(jsonActualizado)
                .when()
                .put(url);
    }

    @And("el cuerpo contiene el comentario actualizado")
    public void cuerpoContieneComentarioActualizado() {
        response.then().body("content", equalTo("Contenido actualizado desde prueba"));
    }

    @When("envío una solicitud GET a {string} para listar comentarios")
    public void envioGetListaComentarios(String url) {
        response = given()
                .accept(ContentType.JSON)
                .when()
                .get(url);
    }

    @And("el cuerpo contiene una lista de comentarios")
    public void cuerpoContieneListaComentarios() {
        response.then().body("content", not(empty()));
    }

    @When("envío una solicitud POST a {string} con un ID de profesor inexistente")
    public void envioPostConProfesorInexistente(String url) {
        String comentarioInvalido = """
            {
                "professorId": 9999,
                "programId": 1,
                "content": "Comentario con profesor inexistente"
            }
        """;

        response = given()
                .contentType(ContentType.JSON)
                .body(comentarioInvalido)
                .when()
                .post(url);
    }

    @And("el cuerpo contiene un mensaje de error que incluye {string}")
    public void cuerpoContieneMensajeDeError(String mensaje) {
        response.then().body("message", containsString(mensaje));
    }
}
