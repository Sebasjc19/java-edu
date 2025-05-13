Feature: Gestión de Usuarios

Scenario: Crear un nuevo usuario
Given tengo los datos válidos de un nuevo usuario
When envío una solicitud POST a "/users"
Then recibo un código de estado 201
And el cuerpo contiene la información del nuevo usuario

Scenario: Obtener un usuario existente
Given existe un usuario con correo "prueba123@mail.com"
When envío una solicitud GET del usuario
Then recibo un código de estado 200
And el cuerpo contiene la información del usuario

Scenario: Actualizar un usuario
Given existe un usuario con correo "prueba123@mail.com"
And tiene datos a actualizar
When envio una solicitud PATCH a /users
Then recibo un código de estado 200
And el cuerpo contiene la información actualizada

Scenario: Eliminar un usuario
Given existe un usuario con correo "prueba321@email.com"
When envío una solicitud DELETE a /users
Then recibo un código de estado 200
And el cuerpo contiene el mensaje "Usuario eliminado correctamente"

Scenario: Obtener todos los usuarios
Given soy un usuario con rol ADMIN
When envio una solicitud GET a /users
Then recibo un código de estado 200
And el cuerpo contiene una lista de usuarios

Scenario: Obtener usuarios activos
Given soy un usuario con rol ADMIN
When envio una solicitud GET a /users/activos
Then recibo un código de estado 200
And el cuerpo contiene una lista de usuarios
