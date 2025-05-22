Feature: Gestión de Comentarios

  Scenario: Obtener un comentario por ID
    Given existe un usuario para commentario con rol "TUTOR" autenticado
    And creo un comentario valido
    When hago una petición GET al comentario recién creado
    Then la respuesta debe tener código de estado 200
    And el cuerpo debe contener el content del comentario "Excelente contenido"

  Scenario: Obtener todos los comentarios de un usuario
    Given existe un usuario para commentario con rol "TUTOR" autenticado
    When consulto los comentarios del usuario autenticado
    Then la respuesta debe tener código de estado 200
    And el cuerpo debe ser una lista de comentarios

  Scenario: Crear un nuevo comentario
    Given Tengo los datos validos para crear un comentario
    When hago una petición Post a comments
    Then la respuesta debe tener código de estado 201
    And el cuerpo debe contener el texto del comentario "Excelente contenido"

  Scenario: Actualizar un comentario existente
    Given Existe un comentario y datos nuevos validos
    When hago una peticion PUT, hacia la ruta "/comments/"
    Then la respuesta debe tener código de estado 200
    And el cuerpo debe reflejar los datos actualizados

  Scenario: Eliminar un comentario por ID
    Given Existe un comentario con ID 2
    When hago una petición DELETE a "/comments/2"
    Then la respuesta debe tener código de estado 200

