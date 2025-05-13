Feature: Gestión de Programas

  Scenario: Obtener un programa por ID
    Given existe un usuario con rol "STUDENT" autenticado
    And creo un programa válido
    When hago una petición GET al programa recién creado
    Then la respuesta debe tener codigo de estado 200
    And el cuerpo debe contener el ID del programa

  Scenario: Obtener todos los programas de un usuario
    Given existe un tutor con programas asignados
    When consulto los programas del tutor autenticado
    Then la respuesta debe tener codigo de estado 200
    And el cuerpo debe contener una lista de programas

  Scenario: Crear un nuevo programa
    Given Tengo los datos válidos para crear un programa
    When hago una petición POST a programs
    Then la respuesta debe tener codigo de estado 201
    And el cuerpo debe contener el nombre del programa "Ingeniería de Software"

  Scenario: Actualizar un programa existente
    Given Existe un programa y datos nuevos validos
    When hago una peticion PUT a "/programs/1"
    Then la respuesta debe tener codigo de estado 200
    And el cuerpo del programa debe reflejar los datos actualizados

  Scenario: Eliminar un programa por ID
    Given Existe un programa con ID 1
    When hago una petición DELETE a la ruta "/programs/1"
    Then la respuesta debe tener codigo de estado 200

  Scenario: Ejecutar un programa existente
    Given Existe un programa con ID 1
    When hago una petición GET a "/programs/execute/1"
    Then la respuesta debe tener codigo de estado 200
    And el cuerpo debe contener el texto "print"
