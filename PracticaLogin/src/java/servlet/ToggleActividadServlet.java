package servlet;

import Controlador.ModeloEvento;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "ToggleActividadServlet", urlPatterns = {"/toggleActividad"})
public class ToggleActividadServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String idActividadStr = request.getParameter("idActividad");
        // Si viene marcado, el checkbox envía "on", si no, no envía nada (null)
        // Pero aquí lo manejaremos con un parámetro explícito si es posible, o asumiendo toggle.
        // Para simplificar JSP, usaremos un parametro 'estado' que sea "true" o "false"
        String estadoStr = request.getParameter("estado"); 
        
        if (idActividadStr != null && estadoStr != null) {
            int idActividad = Integer.parseInt(idActividadStr);
            boolean estado = Boolean.parseBoolean(estadoStr);
            
            ModeloEvento me = new ModeloEvento();
            me.toggleActividad(idActividad, estado);
        }
        
        // Redirigir siempre al dashboard
        response.sendRedirect("worker_dashboard.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}