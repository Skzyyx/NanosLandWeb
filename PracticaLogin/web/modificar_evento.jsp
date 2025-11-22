<%@page import="Modelo.Actividad"%>
<%@page import="java.util.ArrayList"%>
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
    
    // 2. Obtener el ID
    int idEvento = 0;
    try {
        idEvento = Integer.parseInt(request.getParameter("id"));
    } catch (Exception e) {
        response.sendRedirect("admin_dashboard.jsp?error=1");
        return;
    }
    
    // 3. Buscar evento y sus actividades
    ModeloEvento me = new ModeloEvento();
    Evento evento = me.getEventoPorId(idEvento);
    
    if (evento == null) {
        response.sendRedirect("admin_dashboard.jsp?error=2");
        return;
    }
    
    // Obtenemos la lista de actividades de este evento
    ArrayList<Actividad> listaActividades = me.getActividadesPorEvento(idEvento);
    
    // 4. Formatear fecha
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
    String fechaParaFormulario = sdf.format(evento.getFechaEvento());
%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Modificar Evento - Nanos Land</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.7.0/css/all.css">
    <link rel="stylesheet" href="css/styles.css">
</head>
<body>
    <nav class="navbar navbar-expand-sm bg-danger navbar-dark">
        <a class="navbar-brand" href="#">Nanos Land (Admin)</a>
        <ul class="navbar-nav ml-auto">
            <li class="nav-item">
                <a class="nav-link text-white" href="admin_dashboard.jsp">Volver al Panel</a>
            </li>
            <li class="nav-item">
                <a class="nav-link text-white" href="logout.jsp">Cerrar Sesión</a>
            </li>
        </ul>
    </nav>
    
    <div class="container mt-4">
        <div class="row justify-content-center">
            <div class="col-md-8">
                <div class="card shadow-sm">
                    <div class="card-header bg-dark text-white">
                        <h3><i class="fas fa-edit mr-2"></i>Modificar Evento (ID: <%= evento.getIdEvento() %>)</h3>
                    </div>
                    <div class="card-body">
                        <form action="modificarEvento" method="POST">
                            
                            <input type="hidden" name="id_evento" value="<%= evento.getIdEvento() %>">
                            
                            <div class="form-group">
                                <label>Nombre del Evento:</label>
                                <input type="text" name="nombre_evento" class="form-control" 
                                       value="<%= evento.getNombreEvento() %>" required>
                            </div>
                            
                            <div class="form-row">
                                <div class="form-group col-md-6">
                                    <label>Fecha y Hora:</label>
                                    <input type="datetime-local" name="fecha_evento" class="form-control" 
                                           value="<%= fechaParaFormulario %>" required>
                                </div>
                                <div class="form-group col-md-6">
                                    <label>Paquete:</label>
                                    <input type="text" name="paquete" class="form-control" 
                                           value="<%= evento.getPaquete() %>" required>
                                </div>
                            </div>
                            
                            <div class="form-row">
                                <div class="form-group col-md-6">
                                    <label>Costo:</label>
                                    <input type="number" step="0.01" name="costo" class="form-control" 
                                           value="<%= evento.getCosto() %>" required>
                                </div>
                                <div class="form-group col-md-6">
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
                            </div>
                            
                            <hr>
                            
                            <!-- SECCIÓN DE ACTIVIDADES -->
                            <div class="form-group">
                                <label class="d-flex justify-content-between align-items-center">
                                    <strong>Actividades del Evento</strong>
                                    <button type="button" class="btn btn-sm btn-outline-primary" onclick="agregarActividad()">
                                        <i class="fas fa-plus"></i> Agregar Nueva
                                    </button>
                                </label>
                                <small class="text-muted mb-3 d-block">Puedes editar o eliminar las tareas existentes.</small>
                                
                                <div id="contenedorActividades">
                                    <% for (Actividad act : listaActividades) { %>
                                        <div class="input-group mb-2">
                                            <input type="text" name="actividades" class="form-control" value="<%= act.getDescripcion() %>" required>
                                            <div class="input-group-append">
                                                <button class="btn btn-outline-danger" type="button" onclick="this.parentElement.parentElement.remove()">
                                                    <i class="fas fa-trash"></i>
                                                </button>
                                            </div>
                                        </div>
                                    <% } %>
                                    <% if (listaActividades.isEmpty()) { %>
                                        <p class="text-muted text-center small" id="msgVacio">No hay actividades registradas.</p>
                                    <% } %>
                                </div>
                            </div>
                            
                            <hr>
                            
                            <div class="d-flex justify-content-between">
                                <a href="admin_dashboard.jsp" class="btn btn-secondary">Cancelar</a>
                                <button type="submit" class="btn btn-success px-5">Guardar Cambios</button>
                            </div>
                            
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <script>
        function agregarActividad() {
            // Ocultar mensaje de vacío si existe
            const msg = document.getElementById('msgVacio');
            if(msg) msg.style.display = 'none';

            const div = document.createElement('div');
            div.className = 'input-group mb-2';
            div.innerHTML = `
                <input type="text" name="actividades" class="form-control" placeholder="Nueva actividad..." required>
                <div class="input-group-append">
                    <button class="btn btn-outline-danger" type="button" onclick="this.parentElement.parentElement.remove()">
                        <i class="fas fa-trash"></i>
                    </button>
                </div>
            `;
            document.getElementById('contenedorActividades').appendChild(div);
        }
    </script>
    
    <script src="https://cdn.jsdelivr.net/npm/jquery@3.6.0/dist/jquery.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>