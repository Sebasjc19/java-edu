package co.edu.uniquindio.ingesis.restful.steps;

import co.edu.uniquindio.ingesis.restful.domain.Role;
import io.cucumber.java.en.Given;

public class CommonStepsDefinitions {
    private final UserStepDefinitions userSteps = new UserStepDefinitions();

    @Given("existe un usuario con rol {string} autenticado")
    public void existeUnUsuarioConRolAutenticado(String rolUsuario) {
        Role rol = Role.valueOf(rolUsuario.toUpperCase());
        userSteps.crearYLoggearUsuarioConRol(rol);
    }
}
