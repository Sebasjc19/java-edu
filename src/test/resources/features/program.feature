Feature: Gestión de Programas

  Scenario: Obtener un programa por ID
    Given Existe un programa con ID 1
    When hago una petición GET a programs-1 con un usuario con rol "STUDENT"
    Then la respuesta debe tener código de estado 200
    And el cuerpo debe contener el ID 1

  Scenario: Obtener todos los programas de un usuario
    Given El usuario con ID 2 tiene programas asignados
    When hago una petición GET a programs-2 con un usuario con rol "TUTOR"
    Then la respuesta debe tener código de estado 200
    And el cuerpo debe ser una lista

  Scenario: Crear un nuevo programa
    Given Tengo los datos válidos para crear un programa
    When hago una petición POST a programs
    Then la respuesta debe tener código de estado 201
    And el cuerpo debe contener el nombre del programa "Ingeniería de Software"

  Scenario: Actualizar un programa existente
    Given Existe un programa con ID 3 y datos nuevos válidos
    When hago una petición PUT a programs-3
    Then la respuesta debe tener código de estado 200
    And el cuerpo debe reflejar los datos actualizados

  Scenario: Eliminar un programa por ID
    Given Existe un programa con ID 4
    When hago una petición DELETE a programs-4
    Then la respuesta debe tener código de estado 200

  Scenario: Ejecutar un programa existente
    Given Existe un programa ejecutable con ID 5
    When hago una petición GET a programs-execute-5
    Then la respuesta debe tener código de estado 200
    And el cuerpo debe contener el texto "Resultado"
