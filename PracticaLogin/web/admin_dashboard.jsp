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
    
    // 2. Obtener el HTML para las tablas y dropdowns
    ControladorUsuario cu = new ControladorUsuario();
    String htmlTablaUsuarios = cu.getUsuariosHTML();
    String htmlDropdownClientes = cu.getClientesDropdownHTML();
    
    ControladorEvento ce = new ControladorEvento();
    String htmlTablaEventos = ce.getTodosEventosHTML();
%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Dashboard Admin - Nanos Land</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.7.0/css/all.css">
    <link rel="stylesheet" href="css/styles.css">
    <style>
        /* Ajuste específico para que el menú lateral no choque */
        .sidebar .nav-link { font-size: 0.9rem; }
    </style>
</head>
<body>
    
    <!-- Navbar Superior -->
    <nav class="navbar navbar-dark sticky-top bg-danger flex-md-nowrap p-0 shadow">
        <a class="navbar-brand col-md-3 col-lg-2 mr-0 px-3" href="#">Nanos Land Admin</a>
        <ul class="navbar-nav px-3">
            <li class="nav-item text-nowrap">
                <a class="nav-link text-white" href="logout.jsp">Cerrar Sesión (<%= usuario.getNombre() %>)</a>
            </li>
        </ul>
    </nav>
    
    <div class="container-fluid">
        <div class="row">
            
            <!-- 1. BARRA LATERAL -->
            <nav class="col-md-3 col-lg-2 d-none d-md-block bg-light sidebar py-4">
                <div class="sidebar-sticky">
                    <h6 class="sidebar-heading d-flex justify-content-between align-items-center px-3 mt-4 mb-1 text-muted">
                        <span>Menú Principal</span>
                    </h6>
                    <div class="nav flex-column nav-pills" id="v-pills-tab" role="tablist" aria-orientation="vertical">
                        <a class="nav-link active mb-2" id="v-pills-usuarios-tab" data-toggle="pill" href="#v-pills-usuarios" role="tab" aria-controls="v-pills-usuarios" aria-selected="true">
                            <i class="fas fa-users mr-2"></i> Usuarios
                        </a>
                        <a class="nav-link" id="v-pills-eventos-tab" data-toggle="pill" href="#v-pills-eventos" role="tab" aria-controls="v-pills-eventos" aria-selected="false">
                            <i class="fas fa-calendar-alt mr-2"></i> Eventos
                        </a>
                    </div>
                </div>
            </nav>

            <!-- 2. CONTENIDO PRINCIPAL -->
            <main role="main" class="col-md-9 ml-sm-auto col-lg-10 px-4 py-4">
                
                <% if ("1".equals(request.getParameter("error"))) { %>
                    <div class="alert alert-danger">Ocurrió un error inesperado.</div>
                <% } %>

                <div class="tab-content" id="v-pills-tabContent">
                    
                    <!-- ================= SECCIÓN USUARIOS ================= -->
                    <div class="tab-pane fade show active" id="v-pills-usuarios" role="tabpanel">
                        <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pb-2 mb-3 border-bottom">
                            <h1 class="h2">Gestión de Usuarios</h1>
                        </div>

                        <!-- Alertas Usuarios -->
                        <% if ("1".equals(request.getParameter("user_created"))) { %> <div class="alert alert-success">¡Usuario creado con éxito!</div> <% } %>
                        <% if ("1".equals(request.getParameter("user_updated"))) { %> <div class="alert alert-success">¡Usuario modificado con éxito!</div> <% } %>
                        <% if ("1".equals(request.getParameter("user_deleted"))) { %> <div class="alert alert-success">¡Usuario eliminado con éxito!</div> <% } %>

                        <div class="row">
                            <!-- Formulario Usuarios -->
                            <div class="col-md-4">
                                <div class="card shadow-sm">
                                    <div class="card-header bg-dark text-white"><i class="fas fa-user-plus"></i> Nuevo Usuario</div>
                                    <div class="card-body">
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
                            
                            <!-- Tabla Usuarios -->
                            <div class="col-md-8">
                                <div class="card shadow-sm">
                                    <div class="card-header bg-secondary text-white">Listado de Usuarios</div>
                                    <div class="card-body p-0">
                                        <div class="table-responsive">
                                            <table class="table table-striped table-hover mb-0">
                                                <thead class="thead-light">
                                                    <tr>
                                                        <th>ID</th>
                                                        <th>Nombre</th>
                                                        <th>Email</th>
                                                        <th>Rol</th>
                                                        <th>Acción</th>
                                                    </tr>
                                                </thead>
                                                <tbody><%= htmlTablaUsuarios %></tbody>
                                            </table>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- ================= SECCIÓN EVENTOS (MODIFICADA) ================= -->
                    <div class="tab-pane fade" id="v-pills-eventos" role="tabpanel">
                        <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pb-2 mb-3 border-bottom">
                            <h1 class="h2">Gestión de Eventos</h1>
                        </div>

                        <!-- Alertas Eventos -->
                        <% if ("1".equals(request.getParameter("exito_agenda"))) { %> <div class="alert alert-success">¡Evento agendado con éxito!</div> <% } %>
                        <% if ("1".equals(request.getParameter("event_updated"))) { %> <div class="alert alert-success">¡Evento modificado con éxito!</div> <% } %>
                        <% if ("1".equals(request.getParameter("event_deleted"))) { %> <div class="alert alert-success">¡Evento eliminado con éxito!</div> <% } %>
                        <% if ("1".equals(request.getParameter("error_agenda"))) { %> <div class="alert alert-danger">Error al agendar: verifica los datos.</div> <% } %>

                        <div class="row">
                            <!-- Formulario Eventos -->
                            <div class="col-md-5"> <!-- Aumenté un poco el ancho para las actividades -->
                                <div class="card shadow-sm">
                                    <div class="card-header bg-primary text-white"><i class="fas fa-calendar-plus"></i> Agendar Evento</div>
                                    <div class="card-body">
                                        <form action="agendarEvento" method="POST">
                                            <div class="form-group">
                                                <label>Cliente:</label>
                                                <select name="id_cliente" class="form-control" required>
                                                    <option value="">-- Seleccionar --</option>
                                                    <%= htmlDropdownClientes %>
                                                </select>
                                            </div>
                                            <div class="form-group">
                                                <label>Nombre del Evento:</label>
                                                <input type="text" name="nombre_evento" class="form-control" placeholder="Ej: Boda de Ana" required>
                                            </div>
                                            <div class="form-row">
                                                <div class="form-group col-md-6">
                                                    <label>Fecha:</label>
                                                    <input type="datetime-local" name="fecha_evento" class="form-control" required>
                                                </div>
                                                <div class="form-group col-md-6">
                                                    <label>Paquete:</label>
                                                    <select name="paquete" class="form-control" onchange="actualizarCosto(this)">
                                                        <option value="Basico" data-costo="1500.00">Básico ($1500)</option>
                                                        <option value="Completo" data-costo="3000.00">Completo ($3000)</option>
                                                        <option value="Premium" data-costo="5000.00">Premium ($5000)</option>
                                                    </select>
                                                </div>
                                            </div>
                                            <input type="hidden" name="costo" id="costo" value="1500.00">
                                            
                                            <hr>
                                            
                                            <!-- SECCIÓN DE ACTIVIDADES DINÁMICAS -->
                                            <div class="form-group">
                                                <label class="d-flex justify-content-between align-items-center">
                                                    Actividades Adicionales
                                                    <button type="button" class="btn btn-sm btn-outline-primary" onclick="agregarActividad()">
                                                        <i class="fas fa-plus"></i> Agregar
                                                    </button>
                                                </label>
                                                <small class="text-muted d-block mb-2">Las actividades del paquete se agregan automáticamente.</small>
                                                
                                                <div id="contenedorActividades">
                                                    <!-- Aquí se insertarán los inputs con JS -->
                                                </div>
                                            </div>

                                            <button type="submit" class="btn btn-success btn-block">Agendar</button>
                                        </form>
                                    </div>
                                </div>
                            </div>
                            
                            <!-- Tabla Eventos -->
                            <div class="col-md-7">
                                <div class="card shadow-sm">
                                    <div class="card-header bg-secondary text-white">Calendario de Reservas</div>
                                    <div class="card-body p-0">
                                        <div class="table-responsive">
                                            <table class="table table-striped table-hover mb-0">
                                                <thead class="thead-light">
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
                                                <tbody><%= htmlTablaEventos %></tbody>
                                            </table>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div> 

                </div>
            </main>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/jquery@3.6.0/dist/jquery.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/js/bootstrap.bundle.min.js"></script>
    
    <script>
        // Mantener pestaña activa tras recargar
        $(document).ready(function(){
            const urlParams = new URLSearchParams(window.location.search);
            if(urlParams.has('exito_agenda') || urlParams.has('error_agenda') || urlParams.has('event_updated') || urlParams.has('event_deleted')) {
                $('#v-pills-eventos-tab').tab('show');
            } else if(urlParams.has('user_created') || urlParams.has('user_updated') || urlParams.has('user_deleted')) {
                $('#v-pills-usuarios-tab').tab('show');
            }
        });

        // Función para actualizar costo del input hidden
        function actualizarCosto(select) {
            document.getElementById('costo').value = select.options[select.selectedIndex].dataset.costo;
        }

        // FUNCIÓN PARA AGREGAR CAMPOS DE ACTIVIDAD
        function agregarActividad() {
            const div = document.createElement('div');
            div.className = 'input-group mb-2';
            div.innerHTML = `
                <input type="text" name="actividades" class="form-control" placeholder="Ej: Instalar luces..." required>
                <div class="input-group-append">
                    <button class="btn btn-outline-danger" type="button" onclick="this.parentElement.parentElement.remove()">
                        <i class="fas fa-times"></i>
                    </button>
                </div>
            `;
            document.getElementById('contenedorActividades').appendChild(div);
        }
    </script>
</body>
</html>