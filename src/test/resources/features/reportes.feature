Feature: Gestión de Reportes

  Scenario: Crear un nuevo reporte
    Given tengo los datos validos de un nuevo reporte
    When envio una solicitud POST a "/reports
    Then recibo un codigo de estado 201
    And el cuerpo contiene la informacion del nuevo reporte

  Scenario: Crear un nuevo reporte con un profesor inexistente
    Given tengo los datos validos de un nuevo reporte, excepto el ID del profesor que no existe
    When envio una solicitud POST a "reports"
    Then recibo un codigo de estado 400
    And el cuerpo contiene un mensaje explicando el error