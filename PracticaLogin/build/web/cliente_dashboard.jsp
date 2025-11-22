<%@page import="Controlador.ControladorEvento"%>
<%@page import="Modelo.Usuario"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    // 1. Proteger la página: Verificar sesión y rol
    HttpSession objSession = request.getSession(false);
    Usuario usuario = (objSession != null) ? (Usuario) objSession.getAttribute("usuario") : null;

    if (usuario == null || !usuario.getRol().equals("cliente")) {
        response.sendRedirect("login.jsp");
        return;
    }

    // 2. Obtener la lista de eventos del cliente
    ControladorEvento ce = new ControladorEvento();
    String htmlMisEventos = ce.getMisEventosHTML(usuario.getIdUsuario());
%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Mi Perfil - Nanos Land</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.7.0/css/all.css">
    <!-- Tu CSS personalizado -->
    <link rel="stylesheet" href="css/styles.css">
</head>
<body>

    <!-- Barra de Navegación -->
    <nav class="navbar navbar-expand-sm navbar-dark">
        <a class="navbar-brand" href="#">Nanos Land</a>
        <ul class="navbar-nav ml-auto">
            <li class="nav-item">
                <span class="navbar-text mr-3">Hola, <%= usuario.getNombre()%></span>
            </li>
            <li class="nav-item">
                <a class="nav-link text-white" href="logout.jsp">Cerrar Sesión</a>
            </li>
        </ul>
    </nav>

    <div class="container mt-4">
        
        <!-- ALERTAS DE ÉXITO -->
        <% if ("1".equals(request.getParameter("perfil_updated"))) { %>
            <div class="alert alert-success alert-dismissible fade show">
                <strong>¡Datos guardados!</strong> Tu información de contacto se ha actualizado.
                <button type="button" class="close" data-dismiss="alert">&times;</button>
            </div>
        <% }%>
        
        <% if ("1".equals(request.getParameter("pass_updated"))) { %>
            <div class="alert alert-success alert-dismissible fade show">
                <strong>¡Seguridad actualizada!</strong> Tu contraseña se cambió correctamente.
                <button type="button" class="close" data-dismiss="alert">&times;</button>
            </div>
        <% }%>

        <!-- ALERTAS DE ERROR -->
        <% if ("incorrecta".equals(request.getParameter("error_pass"))) { %>
            <div class="alert alert-danger alert-dismissible fade show">
                <strong>Error:</strong> La contraseña actual ingresada no es correcta.
                <button type="button" class="close" data-dismiss="alert">&times;</button>
            </div>
        <% }%>
        
        <% if ("nocoincide".equals(request.getParameter("error_pass"))) { %>
            <div class="alert alert-warning alert-dismissible fade show">
                <strong>Cuidado:</strong> La nueva contraseña y la confirmación no coinciden.
                <button type="button" class="close" data-dismiss="alert">&times;</button>
            </div>
        <% }%>
        
        <% if ("1".equals(request.getParameter("error"))) { %>
            <div class="alert alert-danger alert-dismissible fade show">
                Ocurrió un error al procesar la solicitud. Intenta nuevamente.
                <button type="button" class="close" data-dismiss="alert">&times;</button>
            </div>
        <% }%>

        <div class="row">

            <!-- ========================================== -->
            <!-- COLUMNA 1: DATOS PERSONALES (SOLO LECTURA) -->
            <!-- ========================================== -->
            <div class="col-md-4">
                <div class="card shadow-sm mb-4">
                    <div class="card-header bg-info text-white">
                        <h5 class="mb-0"><i class="fas fa-id-card mr-2"></i>Mi Información</h5>
                    </div>
                    <div class="card-body">
                        
                        <!-- Campos visuales (No editables aquí) -->
                        <div class="form-group">
                            <label class="text-muted small">Nombre Completo</label>
                            <input type="text" class="form-control bg-light" value="<%= usuario.getNombre()%>" readonly>
                        </div>
                        <div class="form-group">
                            <label class="text-muted small">Correo Electrónico</label>
                            <input type="text" class="form-control bg-light" value="<%= usuario.getEmail()%>" readonly>
                        </div>
                        <div class="form-group">
                            <label class="text-muted small">Teléfono</label>
                            <input type="text" class="form-control bg-light" value="<%= usuario.getTelefono()%>" readonly>
                        </div>
                        
                        <hr>

                        <!-- Botones para abrir Modales -->
                        <button type="button" class="btn btn-primary btn-block mb-2" data-toggle="modal" data-target="#modalEditarDatos">
                            <i class="fas fa-pen mr-1"></i> Modificar Datos
                        </button>
                        
                        <button type="button" class="btn btn-outline-danger btn-block btn-sm" data-toggle="modal" data-target="#modalCambiarPass">
                            <i class="fas fa-key mr-1"></i> Cambiar Contraseña
                        </button>

                    </div>
                </div>
            </div>

            <!-- ========================================== -->
            <!-- COLUMNA 2: TABLA DE EVENTOS                -->
            <!-- ========================================== -->
            <div class="col-md-8">
                <div class="card shadow-sm">
                    <div class="card-header bg-primary text-white">
                         <h5 class="mb-0"><i class="fas fa-calendar-alt mr-2"></i>Mis Próximos Eventos</h5>
                    </div>
                    <div class="card-body">
                        <p class="text-muted">Estado en tiempo real de tus fiestas reservadas.</p>

                        <div class="table-responsive">
                            <table class="table table-bordered table-hover">
                                <thead>
                                    <tr>
                                        <th>Evento</th>
                                        <th>Fecha</th>
                                        <th>Paquete</th>
                                        <th>Estado</th> 
                                    </tr>
                                </thead>
                                <tbody>
                                    <%= htmlMisEventos%>
                                </tbody>
                            </table>
                        </div>

                        <% if (htmlMisEventos.contains("No tienes eventos")) { %>
                            <div class="alert alert-light text-center mt-3 border">
                                <i class="fas fa-calendar-times fa-2x mb-2 text-muted"></i><br>
                                Aún no tienes eventos agendados.<br>
                                <small>¡Contáctanos para reservar tu fecha!</small>
                            </div>
                        <% }%>
                    </div>
                </div>
            </div>

        </div>
    </div>

    <!-- ========================================== -->
    <!-- MODAL 1: ACTUALIZAR DATOS DE CONTACTO      -->
    <!-- ========================================== -->
    <div class="modal fade" id="modalEditarDatos" tabindex="-1" role="dialog" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered" role="document">
            <div class="modal-content">
                <div class="modal-header bg-primary text-white">
                    <h5 class="modal-title"><i class="fas fa-user-edit mr-2"></i>Actualizar Datos</h5>
                    <button type="button" class="close text-white" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <form action="modificarPerfil" method="POST">
                    <!-- Campo oculto para decirle al Servlet qué hacer -->
                    <input type="hidden" name="accion" value="datos">
                    
                    <div class="modal-body">
                        <div class="form-group">
                            <label>Nombre:</label>
                            <input type="text" name="nombre" class="form-control" value="<%= usuario.getNombre()%>" required>
                        </div>
                        <div class="form-group">
                            <label>Email:</label>
                            <input type="email" name="email" class="form-control" value="<%= usuario.getEmail()%>" required>
                        </div>
                        <div class="form-group">
                            <label>Teléfono:</label>
                            <input type="tel" name="telefono" class="form-control" value="<%= usuario.getTelefono()%>">
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancelar</button>
                        <button type="submit" class="btn btn-success">Guardar Cambios</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <!-- ========================================== -->
    <!-- MODAL 2: CAMBIAR CONTRASEÑA (SEGURIDAD)    -->
    <!-- ========================================== -->
    <div class="modal fade" id="modalCambiarPass" tabindex="-1" role="dialog" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered" role="document">
            <div class="modal-content">
                <div class="modal-header bg-danger text-white">
                    <h5 class="modal-title"><i class="fas fa-lock mr-2"></i>Seguridad</h5>
                    <button type="button" class="close text-white" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <form action="modificarPerfil" method="POST">
                    <!-- Campo oculto para decirle al Servlet qué hacer -->
                    <input type="hidden" name="accion" value="pass">
                    
                    <div class="modal-body">
                        <div class="form-group">
                            <label>Contraseña Actual:</label>
                            <input type="password" name="current_pass" class="form-control" placeholder="Ingresa tu clave actual" required>
                            <small class="text-muted">Necesaria para autorizar el cambio.</small>
                        </div>
                        <hr>
                        <div class="form-group">
                            <label>Nueva Contraseña:</label>
                            <input type="password" name="new_pass" class="form-control" required>
                        </div>
                        <div class="form-group">
                            <label>Confirmar Nueva Contraseña:</label>
                            <input type="password" name="confirm_pass" class="form-control" required>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancelar</button>
                        <button type="submit" class="btn btn-danger">Actualizar Clave</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <!-- Scripts necesarios -->
    <script src="https://cdn.jsdelivr.net/npm/jquery@3.6.0/dist/jquery.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/js/bootstrap.bundle.min.js"></script>
    
    <!-- Script Opcional: Reabrir modal si hubo error de contraseña al recargar -->
    <% if (request.getParameter("error_pass") != null) { %>
    <script>
        $(document).ready(function(){
            $('#modalCambiarPass').modal('show');
        });
    </script>
    <% } %>
    
</body>
</html>