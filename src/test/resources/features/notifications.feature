Feature: Gestión Notificaciones

  Scenario: Obtener una notificacion por ID
    Given Existe una notificacion con ID 1 de un estudiante
    When hago una petición GET a notifications-1 con un usuario con rol "STUDENT"
    Then la respuesta debe tener código de estado 200
    And el cuerpo debe contener el ID 1

  Scenario: Obtener todas las notificaciones de un usuario
    Given existe un "STUDENT" con notificaciones asignadas
    When hago una petición GET a notifications-2 con un usuario con rol "STUDENT"
    Then la respuesta debe tener código de estado 200
    And el cuerpo debe ser una lista

  Scenario: Crear una nueva notificacion
    Given Tengo los datos validos para crear una notificacion
    When hago una peticion POST a notifications
    Then la respuesta debe tener código de estado 201
    And el cuerpo debe contener el message de la notificacion "Revisión de código"




