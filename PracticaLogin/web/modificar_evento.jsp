<%@page import="java.text.SimpleDateFormat"%>
<%@page import="Modelo.Evento"%>
<%@page import="Controlador.ModeloEvento"%>
<%@page import="Modelo.Usuario"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    // 1. Proteger la página
    HttpSession objSession = request.getSession(false);
    Usuario usuario = (objSession != null) ? (Usuario) objSession.getAttribute("usuario") : null;
    
    if (usuario == null || !usuario.getRol().equals("admin")) {
        response.sendRedirect("login.jsp");
        return;
    }
    
    // 2. Obtener el ID del evento de la URL (del ?id=...)
    int idEvento = 0;
    try {
        idEvento = Integer.parseInt(request.getParameter("id"));
    } catch (Exception e) {
        // Si no hay ID, no podemos hacer nada.
        response.sendRedirect("admin_dashboard.jsp?error=1");
        return;
    }
    
    // 3. Buscar el evento en la BD para rellenar el formulario
    ModeloEvento me = new ModeloEvento();
    Evento evento = me.getEventoPorId(idEvento);
    
    if (evento == null) {
        // Si el ID no existe
        response.sendRedirect("admin_dashboard.jsp?error=2");
        return;
    }
    
    // 4. Formatear la fecha para el input (datetime-local)
    // El formato debe ser: yyyy-MM-ddTHH:mm
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
    String fechaParaFormulario = sdf.format(evento.getFechaEvento());
    
%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Modificar Evento - Nanos Land</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/css/bootstrap.min.css">
</head>
<body>
    <nav class="navbar navbar-expand-sm bg-danger navbar-dark">
        <a class="navbar-brand" href="#">Nanos Land (Admin)</a>
        <ul class="navbar-nav ml-auto">
            <li class="nav-item">
                <a class="nav-link" href="admin_dashboard.jsp">Volver al Panel</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="logout.jsp">Cerrar Sesión</a>
            </li>
        </ul>
    </nav>
    
    <div class="container mt-4">
        <div class="row justify-content-center">
            <div class="col-md-8">
                <div class="card">
                    <div class="card-header bg-dark text-white">
                        <h3>Modificar Evento (ID: <%= evento.getIdEvento() %>)</h3>
                    </div>
                    <div class="card-body">
                        <form action="modificarEvento" method="POST">
                            
                            <input type="hidden" name="id_evento" value="<%= evento.getIdEvento() %>">
                            
                            <div class="form-group">
                                <label>Nombre del Evento:</label>
                                <input type="text" name="nombre_evento" class="form-control" 
                                       value="<%= evento.getNombreEvento() %>" required>
                            </div>
                            
                            <div class="form-group">
                                <label>Fecha y Hora:</label>
                                <input type="datetime-local" name="fecha_evento" class="form-control" 
                                       value="<%= fechaParaFormulario %>" required>
                            </div>
                            
                            <div class="form-group">
                                <label>Paquete:</label>
                                <input type="text" name="paquete" class="form-control" 
                                       value="<%= evento.getPaquete() %>" required>
                            </div>
                            
                            <div class="form-group">
                                <label>Costo:</label>
                                <input type="number" step="0.01" name="costo" class="form-control" 
                                       value="<%= evento.getCosto() %>" required>
                            </div>
                            
                            <div class="form-group">
                                <label>Estado:</label>
                                <select name="estado" class="form-control">
                                    <option value="agendado" <%= "agendado".equals(evento.getEstado()) ? "selected" : "" %>>
                                        Agendado
                                    </option>
                                    <option value="terminado" <%= "terminado".equals(evento.getEstado()) ? "selected" : "" %>>
                                        Terminado
                                    </option>
                                </select>
                            </div>
                            
                            <button type="submit" class="btn btn-success btn-block">Guardar Cambios</button>
                            <a href="admin_dashboard.jsp" class="btn btn-secondary btn-block">Cancelar</a>
                            
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
