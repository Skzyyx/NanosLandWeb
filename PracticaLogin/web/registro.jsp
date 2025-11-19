<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Nanos Land - Registro de Cliente</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/css/bootstrap.min.css">
    <style>
        body { background-color: #f8f9fa; }
    </style>
</head>
<body>
    <div class="container">
        <div class="row justify-content-center mt-5">
            <div class="col-md-6">
                <div class="card shadow-sm">
                    <div class="card-body p-4">
                        <h2 class="text-center mb-4">Registro de Nuevo Cliente</h2>
                        
                        <form action="registrar" method="POST" onsubmit="return validarPassword();">
                            <div class="form-group">
                                <label>Nombre Completo:</label>
                                <input type="text" class="form-control" name="nombre" placeholder="Tu nombre" required>
                            </div>
                            <div class="form-group">
                                <label>Email:</label>
                                <input type="email" class="form-control" name="email" placeholder="tu@email.com" required>
                            </div>
                            <div class="form-group">
                                <label>Teléfono:</label>
                                <input type="tel" class="form-control" name="telefono" placeholder="Ej: 5551234567" required minlength="8" pattern="[0-9]+">
                            </div>
                            <div class="form-group">
                                <label>Contraseña:</label>
                                <input type="password" class="form-control" name="pass" id="pass1" required minlength="3">
                            </div>
                            <div class="form-group">
                                <label>Confirmar Contraseña:</label>
                                <!-- 
                                  --- ¡AQUÍ ESTÁ LA CORRECCIÓN! ---
                                  Añadimos name="pass2" para que el Servlet pueda leerlo.
                                -->
                                <input type="password" class="form-control" name="pass2" id="pass2" required minlength="3">
                            </div>

                            <!-- Manejo de Errores -->
                            <% if (request.getParameter("error") != null) { %>
                                <div class="alert alert-danger" role="alert">
                                    <% if ("1".equals(request.getParameter("error"))) { %>
                                        Las contraseñas no coinciden.
                                    <% } else { %>
                                        Ocurrió un error en el registro (Email duplicado).
                                    <% } %>
                                </div>
                            <% } %>
                            
                            <button type="submit" class="btn btn-success btn-block">Crear Cuenta</button>
                        </form>
                        <hr>
                        <div class="text-center">
                            <a href="login.jsp">¿Ya tienes una cuenta? Inicia Sesión</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Script de JavaScript para validar contraseñas -->
    <script>
        function validarPassword() {
            var pass1 = document.getElementById('pass1').value;
            var pass2 = document.getElementById('pass2').value;
            
            if (pass1 !== pass2) {
                alert("Las contraseñas no coinciden. Por favor, inténtelo de nuevo.");
                return false; // Evita que el formulario se envíe
            }
            return true; // Permite el envío
        }
    </script>
</body>
</html>