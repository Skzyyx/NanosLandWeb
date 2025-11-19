/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servlet;

import Controlador.ModeloEvento;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// Este Servlet maneja el botón "Eliminar" de la tabla de eventos
@WebServlet(name = "EliminarEventoServlet", urlPatterns = {"/eliminarEvento"})
public class EliminarEventoServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            int idEvento = Integer.parseInt(request.getParameter("idEvento"));
            
            ModeloEvento me = new ModeloEvento();
            me.eliminarEvento(idEvento); 
            
        } catch (NumberFormatException e) {
            System.err.println("Error al eliminar evento: " + e.getMessage());
        }
        
        response.sendRedirect("admin_dashboard.jsp?event_deleted=1");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}