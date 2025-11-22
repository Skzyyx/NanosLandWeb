<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Nanos Land - Iniciar Sesión</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="css/styles.css">
</head>
<body class="login-body">
    
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-md-5">
                
                <div class="card login-card">
                    
                    <img src="img/logo.png" alt="Nanos Land Logo" class="login-logo">
                    
                    <div class="card-body">
                        <h2 class="mb-4">Bienvenido</h2>

                        <form action="login" method="POST">
                            <div class="form-group">
                                <label for="email">Email:</label>
                                <input type="email" class="form-control" name="email" id="email" placeholder="correo electrónico" required>
                            </div>
                            <div class="form-group">
                                <label for="pass">Contraseña:</label>
                                <input type="password" class="form-control" name="pass" id="pass" placeholder="contraseña" required>
                            </div>
                            
                            <% if (request.getParameter("error") != null) { %>
                                <div class="alert alert-danger text-center mt-3" role="alert">
                                    Datos incorrectos
                                </div>
                            <% } %>
                            <% if (request.getParameter("exito") != null) { %>
                                <div class="alert alert-success text-center mt-3" role="alert">
                                    ¡Registro exitoso!
                                </div>
                            <% } %>
                            
                            <button type="submit" class="btn btn-primary btn-block">Entrar</button>
                            
                        </form>
                    </div>
                </div>
                
            </div>
        </div>
    </div>

</body>
</html>