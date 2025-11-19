<%@page import="Controlador.ControladorEvento"%>
<%@page import="Modelo.Usuario"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    // 1. Proteger la página
    HttpSession objSession = request.getSession(false);
    Usuario usuario = (objSession != null) ? (Usuario) objSession.getAttribute("usuario") : null;
    
    if (usuario == null || !usuario.getRol().equals("cliente")) {
        response.sendRedirect("login.jsp");
        return;
    }
    
    // 2. Obtener la lógica REAL de los eventos del cliente (Con colores y alertas)
    ControladorEvento ce = new ControladorEvento();
    String htmlMisEventos = ce.getMisEventosHTML(usuario.getIdUsuario());
%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Mi Perfil - Nanos Land</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/css/bootstrap.min.css">
</head>
<body>
    
    <!-- Barra de Navegación -->
    <nav class="navbar navbar-expand-sm bg-primary navbar-dark">
        <a class="navbar-brand" href="#">Nanos Land</a>
        <ul class="navbar-nav ml-auto">
            <li class="nav-item">
                <span class="navbar-text text-white-50 mr-3">Hola, <%= usuario.getNombre() %></span>
            </li>
            <li class="nav-item">
                <a class="nav-link text-white" href="logout.jsp">Cerrar Sesión</a>
            </li>
        </ul>
    </nav>
    
    <div class="container mt-4">
        <div class="row">
            
            <!-- Columna 1: Mis Datos (SOLO LECTURA) -->
            <div class="col-md-4">
                <div class="card shadow-sm mb-4">
                    <div class="card-header bg-info text-white">
                        <h5 class="mb-0">Mis Datos</h5>
                    </div>
                    <div class="card-body">
                        <form>
                            <div class="form-group">
                                <label>Nombre:</label>
                                <!-- El atributo 'readonly' impide la modificación -->
                                <input type="text" class="form-control" value="<%= usuario.getNombre() %>" readonly>
                            </div>
                            <div class="form-group">
                                <label>Email:</label>
                                <input type="text" class="form-control" value="<%= usuario.getEmail() %>" readonly>
                            </div>
                            <div class="form-group">
                                <label>Teléfono:</label>
                                <input type="text" class="form-control" value="<%= usuario.getTelefono() %>" readonly>
                            </div>
                            <small class="text-muted">
                                * Para actualizar tus datos, por favor contacta a la administración.
                            </small>
                        </form>
                    </div>
                </div>
            </div>
            
            <!-- Columna 2: Mis Eventos (Tabla con Semáforo) -->
            <div class="col-md-8">
                <div class="card shadow-sm">
                    <div class="card-body">
                        <h3>Mis Próximos Eventos</h3>
                        <p class="text-muted">Consulta el estado y tiempo restante de tus reservaciones.</p>
                        
                        <table class="table table-bordered table-hover">
                            <thead class="thead-dark">
                                <tr>
                                    <th>Evento</th>
                                    <th>Fecha</th>
                                    <th>Paquete</th>
                                    <!-- Esta columna coincide con la lógica del controlador -->
                                    <th>Tiempo Restante</th> 
                                </tr>
                            </thead>
                            <tbody>
                                <%= htmlMisEventos %>
                            </tbody>
                        </table>
                        
                        <% if (htmlMisEventos.contains("No tienes eventos")) { %>
                            <div class="alert alert-light text-center">
                                Aún no tienes eventos agendados.
                            </div>
                        <% } %>
                    </div>
                </div>
            </div>
            
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/jquery@3.6.0/dist/jquery.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
