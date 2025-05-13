Feature: Gestión de usuarios

  Scenario: Crear un nuevo usuario
    Given tengo los datos válidos de un nuevo usuario
    When envío una solicitud POST a "/users"
    Then recibo un código de estado 201
    And el cuerpo contiene la información del nuevo usuario

  Scenario: Obtener un usuario existente
    Given existe un usuario con ID 1
    When envío una solicitud GET a "\/users/1"
    Then recibo un código de estado 200
    And el cuerpo contiene la información del usuario con ID 1

  Scenario: Actualizar un usuario
    Given existe un usuario con ID 1 y datos actualizados
    When envío una solicitud PATCH a "\/users/1"
    Then recibo un código de estado 200
    And el cuerpo contiene la información actualizada

  Scenario: Eliminar un usuario
    Given existe un usuario con ID 1
    When envío una solicitud DELETE a "\/users/1"
    Then recibo un código de estado 200
    And el cuerpo contiene el mensaje "Usuario eliminado"

  Scenario: Obtener todos los usuarios
    Given soy un usuario con rol ADMIN
    When envío una solicitud GET a "\/users"
    Then recibo un código de estado 200
    And el cuerpo contiene una lista de usuarios

  Scenario: Obtener usuarios activos
    When envío una solicitud GET a "\/users/activos"
    Then recibo un código de estado 200
    And el cuerpo contiene una lista de usuarios activos
