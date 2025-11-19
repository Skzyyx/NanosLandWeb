<%@page import="Controlador.ControladorEvento"%>
<%@page import="Modelo.Usuario"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    // 1. Proteger la página
    HttpSession objSession = request.getSession(false);
    Usuario usuario = (objSession != null) ? (Usuario) objSession.getAttribute("usuario") : null;
    
    if (usuario == null || !usuario.getRol().equals("trabajador")) {
        response.sendRedirect("login.jsp");
        return;
    }
    
    // 2. Obtener el HTML de los eventos pendientes
    ControladorEvento ce = new ControladorEvento();
    String htmlEventosPendientes = ce.getEventosPendientesHTML();
    
    
%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Dashboard Trabajador - Nanos Land</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.7.0/css/all.css">
</head>
<body>
    
    <nav class="navbar navbar-expand-sm bg-dark navbar-dark">
        <a class="navbar-brand" href="#">Nanos Land (Trabajador)</a>
        <ul class="navbar-nav ml-auto">
            <li class="nav-item">
                <span class="navbar-text text-white-50">Bienvenido, <%= usuario.getNombre() %></span>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="logout.jsp">Cerrar Sesión</a>
            </li>
        </ul>
    </nav>
    
    <div class="container mt-4">
        
       

        <!-- Esta es la única función del trabajador -->
        <h2>Eventos Pendientes</h2>
        <p>Esta es la lista de eventos agendados que deben ser atendidos.</p>

        <table class="table table-hover table-striped">
            <thead class="thead-light">
                <tr>
                    <th>Nombre del Evento</th>
                    <th>Cliente</th>
                    <th>Fecha y Hora</th>
                    <th>Paquete</th>
                    <th>Acción</th>
                </tr>
            </thead>
            <tbody>
                <%= htmlEventosPendientes %>
            </tbody>
        </table>
            
    </div>

    <script src="https://cdn.jsdelivr.net/npm/jquery@3.6.0/dist/jquery.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>