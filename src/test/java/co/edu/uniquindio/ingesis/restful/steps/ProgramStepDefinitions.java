package co.edu.uniquindio.ingesis.restful.steps;

import co.edu.uniquindio.ingesis.restful.domain.Role;
import co.edu.uniquindio.ingesis.restful.domain.Type;
import co.edu.uniquindio.ingesis.restful.dtos.programs.ProgramCreationRequest;
import co.edu.uniquindio.ingesis.restful.dtos.programs.UpdateProgramRequest;
import io.cucumber.java.en.*;
import io.restassured.response.Response;
import lombok.Getter;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class ProgramStepDefinitions {

    private final UserStepDefinitions userSteps = new UserStepDefinitions();
    private ProgramCreationRequest programCreationRequest;
    private Response response;
    @Getter
    private Long programId;
    private Long lastProgramId;
    Long tutorId;
    String tutorToken;
    private UpdateProgramRequest updateRequest;

    @Given("Tengo los datos válidos para crear un programa")
    public void tengoLosDatosVálidosParaCrearUnPrograma() {
        programCreationRequest = new ProgramCreationRequest(
                "Ingeniería de Software",
                "este es un programa de java",
                "System.out.println(holaaa)",
                Type.NORMAL,
                1L
        );
    }

    @When("hago una petición POST a programs")
    public void hagoUnaPeticiónPOSTAPrograms() {
        response = given()
                .baseUri("http://localhost:8080")
                .contentType("application/json")
                .body(programCreationRequest)
                .when()
                .post("/programs");

        programId = response.jsonPath().getLong("id");  // <-- ID del programa
    }

    @Then("la respuesta debe tener codigo de estado {int}")
    public void laRespuestaDebeTenerCodigoDeEstado(int statusCode) {
        response.then().statusCode(statusCode);
    }

    @And("el cuerpo debe contener el nombre del programa {string}")
    public void elCuerpoDebeContenerElNombreDelPrograma(String nombreEsperado) {
        // El nombre esperado es "Ingeniería de Software" tal y como está en program.feature
        response.then().body("title", equalToIgnoringCase(nombreEsperado));
    }

    // ----------------------------------------------------
    @Given("existe un usuario para programa con rol {string} autenticado")
    public void existeUnUsuarioParaProgramaConRolAutenticado(String rolUsuario) {
        Role rol = Role.valueOf(rolUsuario.toUpperCase());
        userSteps.crearYLoggearUsuarioConRol(rol);
    }

    @And("creo un programa valido")
    public void creoUnProgramaValido() {

        // Definir el ID del programa a verificar si ya existe
        Long userID = userSteps.getUserId();

        // 1. Verificar si hay programas para el usuario
        Response checkResponse = given()
                .baseUri("http://localhost:8080")
                .contentType("application/json")
                .auth().oauth2(userSteps.getToken())
                .when()
                .get("/programs/user/" + userID);  // Usamos el ID en la ruta

        // 2. Comprobar el código de respuesta
        if (checkResponse.statusCode() == 200) {
            // Se asume que la respuesta es una lista de objetos con campos "id"
            List<Map<String, Object>> programas = checkResponse.jsonPath().getList("");
            if (!programas.isEmpty()) {
                Integer id = (Integer) programas.get(0).get("id");
                lastProgramId = id.longValue();
                System.out.println("Ya existe un programa con ID: " + lastProgramId);
                return;
            }
        } else if (checkResponse.statusCode() == 404) {
            System.out.println("No hay programas existentes para el usuario " + userID + ". Se procederá a crear uno.");
        } else {
            throw new RuntimeException("Error inesperado al consultar programas: " + checkResponse.statusCode());
        }

        programCreationRequest = new ProgramCreationRequest(
                "Ingenieria de Prueba",
                "Un programa para pruebas",
                "public class Main {\n" +
                        "    public static void main(String[] args) {\n" +
                        " \n" +
                        "        System.out.println(\"¡Hola, Mundo!\");\n" +
                        "    }\n" +
                        "}",
                Type.NORMAL,
                userSteps.getUserId() // Usa el ID del usuario
        );

        response = given()
                .baseUri("http://localhost:8080")
                .contentType("application/json")
                .auth().oauth2(userSteps.getToken())
                .body(programCreationRequest)
                .when()
                .post("/programs");

        lastProgramId = response.jsonPath().getLong("id");
    }

    @When("hago una petición GET al programa recién creado")
    public void hagoUnaPeticiónGETAlProgramaReciénCreado() {
        response = given()
                .baseUri("http://localhost:8080")
                .auth().oauth2(userSteps.getToken())
                .when()
                .get("/programs/" + lastProgramId);
    }

    @And("el cuerpo debe contener el ID del programa")
    public void elCuerpoDebeContenerElIDDelPrograma() {
        response.then().body("id", notNullValue());
    }

    // ----------------------------------------------------

    @When("consulto los programas del tutor autenticado")
    public void consultoLosProgramasDelTutorAutenticado() {
        response = given()
                .baseUri("http://localhost:8080")
                .contentType("application/json")
                .auth().oauth2(tutorToken)
                .when()
                .get("/programs/" + tutorId);
    }

    @And("el cuerpo debe contener una lista de programas")
    public void elCuerpoDebeContenerUnaListaDeProgramas() {
        response.then()
                .body("$", not(empty())); // Verifica que la respuesta sea una lista no vacía
    }

    // ----------------------------------------------------

    @Given("Existe un programa y datos nuevos validos")
    public void existeUnProgramaYDatosNuevosValidos() {
        existeUnUsuarioParaProgramaConRolAutenticado(String.valueOf(Role.TUTOR));
        creoUnProgramaValido();
        // Datos nuevos para actualizar
        updateRequest = new UpdateProgramRequest(
                "Nombre actualizado",
                "Descripción actualizada",
                "System.out.println('Actualizado');",
                userSteps.getUserId()
        );
    }

    @When("hago una peticion PUT hacia la ruta {string}")
    public void hagoUnaPeticionPUTHaciaLaRuta(String ruta) {
        response = given()
                .baseUri("http://localhost:8080")
                .contentType("application/json")
                .auth().oauth2(userSteps.getToken())
                .body(updateRequest)
                .when()
                .put(ruta + lastProgramId);

    }

    @And("el cuerpo del programa debe reflejar los datos actualizados")
    public void elCuerpoDelProgramaDebeReflejarLosDatosActualizados() {

        System.out.println(updateRequest.toString());
        response.then()
                .body("title", equalTo(updateRequest.title()))
                .body("description", equalTo(updateRequest.description()))
                .body("code", equalTo(updateRequest.code()))
                .body("userId", equalTo(updateRequest.userId().intValue()));
    }

    // ----------------------------------------------------

    @Given("Existe un programa con ID {int}")
    public void existeUnProgramaConID(int programID) {
        existeUnUsuarioParaProgramaConRolAutenticado("Tutor");
        creoUnProgramaValido();

        Response getResponse = given()
                .baseUri("http://localhost:8080")
                .auth().oauth2(userSteps.getToken())  // Aquí añades el token
                .when()
                .get("/programs/" + programID);

        if (getResponse.statusCode() == 404) {
            //creoUnProgramaVálido();
        }
    }


    @When("hago una petición DELETE a la ruta {string}")
    public void hagoUnaPeticiónDELETEALaRuta(String url) {
        response = given()
                .baseUri("http://localhost:8080")
                .auth().oauth2(userSteps.getToken())
                .when()
                .delete(url + lastProgramId);
    }

    // ----------------------------------------------------

    @When("hago una petición GET a {string}")
    public void hagoUnaPeticiónGETA(String url) {
        // Realizamos la petición GET para ejecutar el programa
        response = given()
                .baseUri("http://localhost:8080")
                .auth().oauth2(userSteps.getToken())
                .when()
                .get(url + lastProgramId);
    }

    @And("el cuerpo debe contener el texto {string}")
    public void elCuerpoDebeContenerElTexto(String textCode) {
        response.then().body(containsString(textCode));
    }

    @When("consulto los programas del usuario autenticado")
    public void consultoLosProgramasDelUsuarioAutenticado() {
        response = given()
                .baseUri("http://localhost:8080")
                .contentType("application/json")
                .auth().oauth2(userSteps.getToken())
                .when()
                .get("/programs/user/" + userSteps.getUserId());  // Usamos el ID en la ruta
    }
}