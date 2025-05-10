Feature: Gestión de Reportes

  Scenario: Crear un nuevo reporte
    Given tengo los datos validos de un nuevo reporte
    And estoy autenticado como TUTOR
    When envio una solicitud POST a "reports"
    Then recibo un codigo de estado 201
    And el cuerpo debe contener el contenido del reporte

  Scenario: Crear un nuevo reporte con un profesor inexistente
    Given tengo los datos validos de un nuevo reporte, excepto el ID del profesor que no existe
    When envio una solicitud POST a "reports"
    Then recibo un codigo de estado 400
    And el cuerpo contiene un mensaje explicando el error

  Scenario: Obtener reportes de un profesor existente
    Given estoy autenticado como TUTOR
    When envío una solicitud GET a "reports professor 2"
    Then recibo un código de estado 200
    And el cuerpo contiene una lista de reportes