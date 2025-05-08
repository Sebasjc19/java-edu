Feature: Gestión Notificaciones

  Scenario: Usuario consulta su primera página de notificaciones
    Given existe un estudiante con ID 1 con al menos 11 notificaciones registradas
    When envío una solicitud GET a "/notifications/student/1?page=0"
    Then recibo un código de estado 200
    And el cuerpo contiene una lista de 10 notificaciones


  Scenario: Obtener una notificación existente
    Given existe una notificación con ID 100
    When envío una solicitud GET a "/notifications/100"
    Then recibo un código de estado 200
    And el cuerpo contiene la información de la notificación con ID 100

  Scenario: Intentar obtener una notificación inexistente
    Given no existe una notificación con ID 9999
    When envío una solicitud GET a "/notifications/9999"
    Then recibo un código de estado 404
    And el cuerpo contiene el mensaje "Notificacion no encontrada"

  Scenario: Enviar una notificación a un estudiante existente
    Given existe un usuario con ID 1
    When envío una solicitud POST a "/notifications" con el cuerpo:
      """
      {
        "studentId": 1,
        "message": "Bienvenido al sistema"
      }
      """
    Then recibo un código de estado 200
    And el sistema envía la notificación al correo del estudiante

  Scenario: Enviar una notificación a un estudiante que no existe
    Given no existe un usuario con ID 999
    When envío una solicitud POST a "/notifications" con el cuerpo:
      """
      {
        "studentId": 999,
        "message": "Bienvenido al sistema"
      }
      """
    Then recibo un código de estado 404
    And el cuerpo contiene el mensaje "No se encuentra un usuario"

  Scenario: Eliminar una notificación existente
    Given existe una notificación con ID 101
    When envío una solicitud DELETE a "/notifications/101"
    Then recibo un código de estado 200
    And el cuerpo contiene la información de la notificación eliminada

  Scenario: Eliminar una notificación que no existe
    Given no existe una notificación con ID 9999
    When envío una solicitud DELETE a "/notifications/9999"
    Then recibo un código de estado 404
    And el cuerpo contiene el mensaje "Notificacion no encontrada"

