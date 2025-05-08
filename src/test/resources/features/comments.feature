Feature: Gestión de Comentarios

  Scenario: Obtener un comentario por ID
    Given existe un usuario con rol "STUDENT" autenticado
    And creo un comentario válido
    When hago una petición GET al comentario recién creado
    Then la respuesta debe tener código de estado 200
    And el cuerpo debe contener el ID del comentario

  Scenario: Obtener todos los comentarios de un usuario
    Given El usuario con ID 2 tiene comentarios asignados
    When hago una petición GET a comments-2 con un usuario con rol "TUTOR"
    Then la respuesta debe tener código de estado 200
    And el cuerpo debe ser una lista

  Scenario: Crear un nuevo comentario
    Given Tengo los datos válidos para crear un comentario
    When hago una petición POST a comments
    Then la respuesta debe tener código de estado 201
    And el cuerpo debe contener el texto del comentario "Excelente contenido"

  Scenario: Actualizar un comentario existente
    Given Existe un comentario con ID 3 y datos nuevos válidos
    When hago una petición PUT a comments-3
    Then la respuesta debe tener código de estado 200
    And el cuerpo debe reflejar los datos actualizados

  Scenario: Eliminar un comentario por ID
    Given Existe un comentario con ID 4
    When hago una petición DELETE a comments-4
    Then la respuesta debe tener código de estado 200

  Scenario: Ejecutar un comentario existente (simulado)
    Given Existe un comentario ejecutable con ID 5
    When hago una petición GET a comments-execute-5
    Then la respuesta debe tener código de estado 200
    And el cuerpo debe contener el texto "Resultado"
