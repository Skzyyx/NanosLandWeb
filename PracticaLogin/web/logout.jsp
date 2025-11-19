<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    // Cierra la sesión del usuario
    HttpSession objSession = request.getSession(false);
    if (objSession != null) {
        objSession.invalidate(); // Invalida la sesión
    }
    // Redirige a la página de login
    response.sendRedirect("login.jsp");
%>