<%@page import="Modelo.Usuario"%>
<%@page import="Controlador.ModeloUsuario"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    // 1. Proteger
    HttpSession objSession = request.getSession(false);
    Usuario admin = (objSession != null) ? (Usuario) objSession.getAttribute("usuario") : null;
    
    if (admin == null || !admin.getRol().equals("admin")) {
        response.sendRedirect("login.jsp");
        return;
    }
    
    // 2. Obtener ID
    int idUsuario = 0;
    try {
        idUsuario = Integer.parseInt(request.getParameter("id"));
    } catch (Exception e) {
        response.sendRedirect("admin_dashboard.jsp");
        return;
    }
    
    // 3. Buscar usuario
    ModeloUsuario mu = new ModeloUsuario();
    Usuario u = mu.getUsuarioPorId(idUsuario);
    
    if (u == null) {
        response.sendRedirect("admin_dashboard.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Modificar Usuario - Nanos Land</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/css/bootstrap.min.css">
</head>
<body>
    <nav class="navbar navbar-expand-sm bg-danger navbar-dark">
        <a class="navbar-brand" href="#">Nanos Land (Admin)</a>
    </nav>
    
    <div class="container mt-4">
        <div class="row justify-content-center">
            <div class="col-md-6">
                <div class="card">
                    <div class="card-header bg-dark text-white">Modificar Usuario</div>
                    <div class="card-body">
                        <form action="modificarUsuarioAdmin" method="POST">
                            <input type="hidden" name="idUsuario" value="<%= u.getIdUsuario() %>">
                            
                            <div class="form-group">
                                <label>Nombre:</label>
                                <input type="text" name="nombre" class="form-control" value="<%= u.getNombre() %>" required>
                            </div>
                            <div class="form-group">
                                <label>Email:</label>
                                <input type="email" name="email" class="form-control" value="<%= u.getEmail() %>" required>
                            </div>
                            <div class="form-group">
                                <label>Teléfono:</label>
                                <input type="tel" name="telefono" class="form-control" value="<%= u.getTelefono() %>">
                            </div>
                            <div class="form-group">
                                <label>Rol:</label>
                                <select name="rol" class="form-control">
                                    <option value="cliente" <%= u.getRol().equals("cliente") ? "selected" : "" %>>Cliente</option>
                                    <option value="trabajador" <%= u.getRol().equals("trabajador") ? "selected" : "" %>>Trabajador</option>
                                    <option value="admin" <%= u.getRol().equals("admin") ? "selected" : "" %>>Administrador</option>
                                </select>
                            </div>
                            
                            <button type="submit" class="btn btn-warning btn-block">Guardar Cambios</button>
                            <a href="admin_dashboard.jsp" class="btn btn-secondary btn-block">Cancelar</a>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>