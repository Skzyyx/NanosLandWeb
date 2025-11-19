/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servlet;

import Controlador.ModeloEvento;
import Modelo.Usuario;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

// Este Servlet  hace que funcione el botón del trabajador
@WebServlet(name = "MarcarTerminadoServlet", urlPatterns = {"/marcarTerminado"})
public class MarcarTerminadoServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // 1. Proteger el servlet
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect("index.jsp");
            return;
        }
        
        Usuario u = (Usuario) session.getAttribute("usuario");
        // Solo admin o trabajador pueden marcar
        if (!u.getRol().equals("admin") && !u.getRol().equals("trabajador")) {
             response.sendRedirect("index.jsp");
            return;
        }

        // 2. Obtener el ID del evento del formulario
        try {
            int idEvento = Integer.parseInt(request.getParameter("idEvento"));
            
            // 3. Llamar al modelo
            ModeloEvento me = new ModeloEvento();
            me.actualizarEstadoEvento(idEvento, "terminado");
            
        } catch (NumberFormatException e) {
            System.err.println("Error al parsear idEvento: " + e.getMessage());
        }
        
        // 4. Redirigir de vuelta al dashboard del trabajador
        response.sendRedirect("worker_dashboard.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}