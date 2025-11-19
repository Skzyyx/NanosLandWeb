/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servlet;

import Controlador.ModeloUsuario;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// Este Servlet maneja el botón "Eliminar" de la tabla de usuarios
@WebServlet(name = "EliminarUsuarioServlet", urlPatterns = {"/eliminarUsuario"})
public class EliminarUsuarioServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            int idUsuario = Integer.parseInt(request.getParameter("idUsuario"));
            
            ModeloUsuario mu = new ModeloUsuario();
            mu.eliminarUsuario(idUsuario);
            
        } catch (NumberFormatException e) {
            System.err.println("Error al eliminar usuario: " + e.getMessage());
        }
        
        response.sendRedirect("admin_dashboard.jsp?user_deleted=1");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}