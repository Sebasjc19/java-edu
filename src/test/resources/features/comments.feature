Feature: Gestión de comentarios
  Scenario: Obtener todos los comentarios en una página específica
    Given tengo los datos válidos de un nuevo comentario
    When envío una solicitud GET a "/comments?page=0"
    Then recibo un código de estado 200
    And el cuerpo contiene una lista de comentarios con un máximo de 10 elementos

  Scenario: Obtener un comentario existente por su ID
    Given existe un comentario con ID 1
    When envío una solicitud GET a "/comments/1"
    Then recibo un código de estado 200
    And el cuerpo contiene los detalles del comentario con ID 1

  Scenario: Obtener un comentario inexistente
    Given no existe un comentario con ID 9999
    When envío una solicitud GET a "/comments/9999"
    Then recibo un código de estado 404
    And el cuerpo contiene un mensaje de error indicando "Comentario no encontrado"

  Scenario: Crear un nuevo comentario válido
    Given tengo un profesor y un programa válidos registrados
    And tengo los datos válidos para crear un comentario
    When envío una solicitud POST a "/comments" con el cuerpo del comentario
    Then recibo un código de estado 201
    And el cuerpo contiene la información del nuevo comentario creado

  Scenario: Crear un comentario con un profesor no registrado
    Given no existe un profesor con ID 99
    When envío una solicitud POST a "/comments" con el ID del profesor inexistente
    Then recibo un código de estado 404
    And el cuerpo contiene un mensaje de error indicando "Profesor no encontrado"

  Scenario: Actualizar el contenido de un comentario
    Given existe un comentario con ID 2
    When envío una solicitud PUT a "/comments/2" con el nuevo contenido
    Then recibo un código de estado 200
    And el cuerpo contiene el comentario actualizado

  Scenario: Eliminar un comentario por su ID
    Given existe un comentario con ID 3
    When envío una solicitud DELETE a "/comments/3"
    Then recibo un código de estado 200
    And el cuerpo contiene los datos del comentario eliminado

  Scenario: Intentar eliminar un comentario que no existe
    Given no existe un comentario con ID 99
    When envío una solicitud DELETE a "/comments/99"
    Then recibo un código de estado 404
    And el cuerpo contiene un mensaje de error indicando "Comentario no encontrado"

  Scenario: Buscar comentarios asociados a un profesor específico
    Given existe un profesor con ID 5 y comentarios asociados
    When envío una solicitud GET a "/comments/professor/5?page=0"
    Then recibo un código de estado 200
    And el cuerpo contiene una lista de comentarios pertenecientes al profesor
