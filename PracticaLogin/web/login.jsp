<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Nanos Land - Iniciar Sesión</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/css/bootstrap.min.css">
    <style>
        body { background-color: #F3FC08; }
        .card {
            border: 2px solid #fbc02d; 
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="row justify-content-center mt-5">
            <div class="col-md-5">
                <div class="card shadow-sm">
                   <div class="card-body p-4">

                    <div class="text-center mb-3">
                        <img src="img/logo.png" alt="Nanos Land Logo" style="width: 150px;">
                    </div>

                    <h2 class="text-center mb-4">Bienvenido a Nanos Land</h2>

                    <form action="login" method="POST">

                            <div class="form-group">
                                <label for="email">Email:</label>
                                <input type="email" class="form-control" name="email" id="email" placeholder="admin@nanosland.com" required>
                            </div>
                            <div class="form-group">
                                <label for="pass">Contraseña:</label>
                                <input type="password" class="form-control" name="pass" id="pass" placeholder="123" required>
                            </div>
                            
                            <!-- Manejo de Errores -->
                            <% if (request.getParameter("error") != null) { %>
                                <div class="alert alert-danger" role="alert">
                                    Email o contraseña incorrectos.
                                </div>
                            <% } %>
                            
                            <!-- Mensaje de Éxito de Registro (Lo dejamos por si el admin crea una cuenta y le dice al usuario que ya puede entrar) -->
                            <% if (request.getParameter("exito") != null) { %>
                                <div class="alert alert-success" role="alert">
                                    ¡Registro exitoso! Por favor, inicia sesión.
                                </div>
                            <% } %>
                            
                            <button type="submit" class="btn btn-primary btn-block">Iniciar Sesión</button>
                        </form>
                        
                        
                        
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>