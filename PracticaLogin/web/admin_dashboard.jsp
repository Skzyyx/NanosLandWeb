<%@page import="Controlador.ControladorEvento"%>
<%@page import="Controlador.ControladorUsuario"%>
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
    
    // 2. Obtener el HTML para las tablas de gestión (Lógica de LECTURA del CRUD)
    ControladorUsuario cu = new ControladorUsuario();
    String htmlTablaUsuarios = cu.getUsuariosHTML();
    
    ControladorEvento ce = new ControladorEvento();
    String htmlTablaEventos = ce.getTodosEventosHTML();
    
    // 3.  Obtener el HTML para el dropdown de clientes
    String htmlDropdownClientes = cu.getClientesDropdownHTML();
    
%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Dashboard Administrador - Nanos Land</title>
    <!-- CSS de Bootstrap y FontAwesome para los iconos -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.7.0/css/all.css">
</head>
<body>
    
    <nav class="navbar navbar-expand-sm bg-danger navbar-dark">
        <a class="navbar-brand" href="#">Nanos Land (Admin)</a>
        <ul class="navbar-nav ml-auto">
            <li class="nav-item">
                <span class="navbar-text text-white-50">Bienvenido, <%= usuario.getNombre() %></span>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="logout.jsp">Cerrar Sesión</a>
            </li>
        </ul>
    </nav>
    
    <div class="container-fluid mt-4">
        <h2>Panel de Administración</h2>
        <p>Desde aquí podrás gestionar usuarios y eventos.</p>
        
        <!-- Alertas de éxito -->
        <% if ("1".equals(request.getParameter("user_created"))) { %>
            <div class="alert alert-success">¡Usuario creado con éxito!</div>
        <% } %>
        <% if ("1".equals(request.getParameter("user_deleted"))) { %>
            <div class="alert alert-success">¡Usuario eliminado con éxito!</div>
        <% } %>
        <% if ("1".equals(request.getParameter("event_deleted"))) { %>
            <div class="alert alert-success">¡Evento eliminado con éxito!</div>
        <% } %>
        <% if ("1".equals(request.getParameter("event_updated"))) { %>
            <div class="alert alert-success">¡Evento modificado con éxito!</div>
        <% } %>
        <% if ("1".equals(request.getParameter("exito_agenda"))) { %>
            <div class="alert alert-success">¡Evento agendado con éxito!</div>
        <% } %>
        <% if ("1".equals(request.getParameter("error_agenda"))) { %>
            <div class="alert alert-danger">Error al agendar el evento.</div>
        <% } %>

        
        <div class="row">
            <!-- Columna 1: CREAR Usuario (CRUD - Create) -->
            <div class="col-md-6">
                <div class="card">
                    <div class="card-header bg-dark text-white">
                        Crear Nuevo Usuario
                    </div>
                    <div class="card-body">
                        <!-- Este formulario llama al servlet "crearUsuario" -->
                        <form action="crearUsuario" method="POST">
                            <div class="form-group">
                                <label>Nombre:</label>
                                <input type="text" name="nombre" class="form-control" required>
                            </div>
                            <div class="form-group">
                                <label>Email:</label>
                                <input type="email" name="email" class="form-control" required>
                            </div>
                            <div class="form-group">
                                <label>Contraseña:</label>
                                <input type="text" name="pass" class="form-control" value="123" required>
                                <small>Por defecto '123'.</small>
                            </div>
                            <div class="form-group">
                                <label>Teléfono:</label>
                                <input type="tel" name="telefono" class="form-control">
                            </div>
                            <div class="form-group">
                                <label>Rol:</label>
                                <select name="rol" class="form-control">
                                    <option value="cliente">Cliente</option>
                                    <option value="trabajador">Trabajador</option>
                                    <option value="admin">Administrador</option>
                                </select>
                            </div>
                            <button type="submit" class="btn btn-primary btn-block">Crear Usuario</button>
                        </form>
                    </div>
                </div>
            </div>
            
            <!-- Columna 2:Formulario para Agendar Evento -->
            <div class="col-md-6">
                <div class="card">
                    <div class="card-header bg-primary text-white">
                        Agendar Nuevo Evento
                    </div>
                    <div class="card-body">
                        <!-- Este formulario llama al servlet "agendarEvento" -->
                        <form action="agendarEvento" method="POST">
                            
                            
                            <div class="form-group">
                                <label>Seleccionar Cliente:</label>
                                <select name="id_cliente" class="form-control" required>
                                    <option value="">-- Elija un cliente --</option>
                                    <%= htmlDropdownClientes %>
                                </select>
                            </div>
                            
                            <div class="form-group">
                                <label>Nombre del Evento:</label>
                                <input type="text" name="nombre_evento" class="form-control" placeholder="Ej: Cumpleaños de Sofía" required>
                            </div>
                            <div class="form-group">
                                <label>Fecha y Hora:</label>
                                <input type="datetime-local" name="fecha_evento" class="form-control" required>
                            </div>
                            <div class="form-group">
                                <label>Paquete:</label>
                                <select name="paquete" class="form-control" onchange="document.getElementById('costo').value = this.options[this.selectedIndex].dataset.costo;">
                                    <option value="Basico" data-costo="1500.00">Básico ($1500)</option>
                                    <option value="Completo" data-costo="3000.00">Completo ($3000)</option>
                                    <option value="Premium" data-costo="5000.00">Premium ($5000)</option>
                                </select>
                            </div>
                            <input type="hidden" name="costo" id="costo" value="1500.00">
                            
                            <button type="submit" class="btn btn-success btn-block">Agendar Evento</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
        
        <hr class="my-4">
        
        <!-- Fila 2: LEER y ELIMINAR Usuarios -->
        <div class="row mt-4">
            <div class="col-12">
                <h3>Gestionar Usuarios</h3>
                <table class="table table-striped table-bordered">
                    <thead class="thead-dark">
                        <tr>
                            <th>ID</th>
                            <th>Nombre</th>
                            <th>Email</th>
                            <th>Rol</th>
                            <th>Acción</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%= htmlTablaUsuarios %>
                    </tbody>
                </table>
            </div>
        </div>
        
        <hr class="my-4">
        
        <!-- Fila 3: LEER, MODIFICAR y ELIMINAR Eventos -->
        <div class="row mt-4">
            <div class="col-12">
                <h3>Gestionar Todos los Eventos</h3>
                <table class="table table-striped table-bordered">
                     <thead class="thead-dark">
                        <tr>
                            <th>ID</th>
                            <th>Evento</th>
                            <th>Cliente</th>
                            <th>Fecha</th>
                            <th>Paquete</th>
                            <th>Estado</th>
                            <th>Acción</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%= htmlTablaEventos %>
                    </tbody>
                </table>
            </div>
        </div>
        
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/jquery@3.6.0/dist/jquery.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>