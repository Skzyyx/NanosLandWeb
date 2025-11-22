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

@WebServlet(name = "MarcarTerminadoServlet", urlPatterns = {"/marcarTerminado"})
public class MarcarTerminadoServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        Usuario u = (Usuario) session.getAttribute("usuario");
        if (!u.getRol().equals("admin") && !u.getRol().equals("trabajador")) {
             response.sendRedirect("login.jsp");
            return;
        }

        try {
            int idEvento = Integer.parseInt(request.getParameter("idEvento"));
            
            ModeloEvento me = new ModeloEvento();
            me.actualizarEstadoEvento(idEvento, "terminado");
            
        } catch (NumberFormatException e) {
            System.err.println("Error al parsear idEvento: " + e.getMessage());
        }
        
        
        response.sendRedirect("worker_dashboard.jsp?terminado=1");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}