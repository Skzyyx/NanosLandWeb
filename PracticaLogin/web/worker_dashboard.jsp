<%@page import="Modelo.Actividad"%>
<%@page import="java.util.ArrayList"%>
<%@page import="Modelo.Evento"%>
<%@page import="Controlador.ControladorEvento"%>
<%@page import="Controlador.ModeloEvento"%> <!-- Importamos el Modelo directamente para lógica interna -->
<%@page import="Modelo.Usuario"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    HttpSession objSession = request.getSession(false);
    Usuario usuario = (objSession != null) ? (Usuario) objSession.getAttribute("usuario") : null;
    
    if (usuario == null || !usuario.getRol().equals("trabajador")) {
        response.sendRedirect("login.jsp");
        return;
    }
    
    // Obtenemos la lista de eventos
    ModeloEvento me = new ModeloEvento(); // Usamos ModeloEvento directo para métodos detallados
    ArrayList<Evento> listaEventos = me.getAllEventos("agendado");
%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Dashboard Trabajador - Nanos Land</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.7.0/css/all.css">
    <link rel="stylesheet" href="css/styles.css">
</head>
<body>
    
    <nav class="navbar navbar-expand-sm navbar-dark bg-dark">
        <a class="navbar-brand" href="#">Nanos Land (Trabajador)</a>
        <ul class="navbar-nav ml-auto">
            <li class="nav-item">
                <span class="navbar-text text-white-50 mr-3">Bienvenido, <%= usuario.getNombre() %></span>
            </li>
            <li class="nav-item">
                <a class="nav-link text-white" href="logout.jsp">Cerrar Sesión</a>
            </li>
        </ul>
    </nav>
    
    <div class="container mt-4">
        
        <% if ("1".equals(request.getParameter("terminado"))) { %>
            <div class="alert alert-success alert-dismissible fade show">
                <strong>¡Excelente!</strong> Evento finalizado y archivado.
                <button type="button" class="close" data-dismiss="alert">&times;</button>
            </div>
        <% } %>

        <div class="card shadow-sm">
            <div class="card-header bg-secondary text-white">
                <h4 class="mb-0"><i class="fas fa-clipboard-list mr-2"></i>Eventos Pendientes</h4>
            </div>
            <div class="card-body bg-white">
                <p class="text-muted mb-4">Marca todas las actividades para poder finalizar un evento.</p>

                <div class="table-responsive">
                    <table class="table table-hover table-striped table-bordered">
                        <thead class="thead-light">
                            <tr>
                                <th>Evento</th>
                                <th>Cliente</th>
                                <th>Fecha</th>
                                <th>Paquete</th>
                                <th>Estado Tareas</th>
                                <th>Acción</th>
                            </tr>
                        </thead>
                        <tbody class="bg-white">
                            <% 
                            if (listaEventos.isEmpty()) { 
                            %>
                                <tr><td colspan="6" class="text-center">No hay eventos pendientes.</td></tr>
                            <% 
                            } else {
                                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM HH:mm");
                                
                                for (Evento e : listaEventos) { 
                                    // Lógica por fila: Obtener actividades y estado
                                    ArrayList<Actividad> actividades = me.getActividadesPorEvento(e.getIdEvento());
                                    boolean todasListas = me.verificarTodasCompletas(e.getIdEvento());
                                    int totalActs = actividades.size();
                                    int completadas = 0;
                                    for(Actividad a : actividades) { if(a.isCompletado()) completadas++; }
                            %>
                                <tr>
                                    <td><%= e.getNombreEvento() %></td>
                                    <td><%= e.getNombreCliente() %></td>
                                    <td><%= sdf.format(e.getFechaEvento()) %></td>
                                    <td><%= e.getPaquete() %></td>
                                    <td>
                                        <!-- Barra de progreso simple -->
                                        <div class="progress" style="height: 20px;">
                                            <% int porcentaje = (totalActs > 0) ? (completadas * 100 / totalActs) : 0; %>
                                            <div class="progress-bar <%= todasListas ? "bg-success" : "bg-warning" %>" 
                                                 role="progressbar" 
                                                 style="width: <%= porcentaje %>%;">
                                                <%= completadas %>/<%= totalActs %>
                                            </div>
                                        </div>
                                    </td>
                                    <td>
                                        <!-- Botón que abre el Modal Único por ID -->
                                        <button type="button" class="btn btn-info btn-sm" data-toggle="modal" data-target="#modalEvento<%= e.getIdEvento() %>">
                                            <i class="fas fa-tasks"></i> Gestionar
                                        </button>
                                    </td>
                                </tr>

                                <!-- MODAL DE ACTIVIDADES (Uno por evento) -->
                                <div class="modal fade" id="modalEvento<%= e.getIdEvento() %>" tabindex="-1">
                                    <div class="modal-dialog modal-lg">
                                        <div class="modal-content">
                                            <div class="modal-header bg-info text-white">
                                                <h5 class="modal-title">Actividades: <%= e.getNombreEvento() %></h5>
                                                <button type="button" class="close text-white" data-dismiss="modal">&times;</button>
                                            </div>
                                            <div class="modal-body">
                                                <h6>Lista de Tareas:</h6>
                                                <ul class="list-group mb-3">
                                                    <% for (Actividad act : actividades) { %>
                                                        <li class="list-group-item d-flex justify-content-between align-items-center">
                                                            <span>
                                                                <i class="<%= act.isCompletado() ? "fas fa-check-circle text-success" : "far fa-circle text-muted" %> mr-2"></i>
                                                                <%= act.getDescripcion() %>
                                                            </span>
                                                            
                                                            <!-- Switch/Botón para marcar -->
                                                            <% if (!act.isCompletado()) { %>
                                                                <a href="toggleActividad?idActividad=<%= act.getIdActividad() %>&estado=true" class="btn btn-outline-success btn-sm">
                                                                    Marcar Listo
                                                                </a>
                                                            <% } else { %>
                                                                <a href="toggleActividad?idActividad=<%= act.getIdActividad() %>&estado=false" class="btn btn-outline-secondary btn-sm">
                                                                    Desmarcar
                                                                </a>
                                                            <% } %>
                                                        </li>
                                                    <% } %>
                                                    <% if (actividades.isEmpty()) { %>
                                                        <li class="list-group-item text-muted">No hay actividades específicas configuradas.</li>
                                                    <% } %>
                                                </ul>
                                                
                                                <hr>
                                                
                                                <div class="text-center">
                                                    <% if (todasListas) { %>
                                                        <div class="alert alert-success">
                                                            <i class="fas fa-check"></i> ¡Todas las tareas completadas!
                                                        </div>
                                                        <form action="marcarTerminado" method="POST">
                                                            <input type="hidden" name="idEvento" value="<%= e.getIdEvento() %>">
                                                            <button type="submit" class="btn btn-success btn-lg btn-block">
                                                                <i class="fas fa-flag-checkered"></i> FINALIZAR EVENTO
                                                            </button>
                                                        </form>
                                                    <% } else { %>
                                                        <div class="alert alert-warning">
                                                            <i class="fas fa-exclamation-triangle"></i> Debes completar todas las actividades para finalizar.
                                                        </div>
                                                        <button class="btn btn-secondary btn-lg btn-block" disabled>
                                                            Finalizar Evento (Bloqueado)
                                                        </button>
                                                    <% } %>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <!-- FIN MODAL -->

                            <% 
                                } // Fin for eventos
                            } // Fin else
                            %>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/jquery@3.6.0/dist/jquery.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>