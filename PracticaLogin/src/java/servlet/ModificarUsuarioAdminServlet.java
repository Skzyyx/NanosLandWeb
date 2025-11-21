/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servlet;

import Controlador.ModeloUsuario;
import Modelo.Usuario;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "ModificarUsuarioAdminServlet", urlPatterns = {"/modificarUsuarioAdmin"})
public class ModificarUsuarioAdminServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        Usuario admin = (session != null) ? (Usuario) session.getAttribute("usuario") : null;
        if (admin == null || !admin.getRol().equals("admin")) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            int idUsuario = Integer.parseInt(request.getParameter("idUsuario"));
            String nombre = request.getParameter("nombre");
            String email = request.getParameter("email");
            String telefono = request.getParameter("telefono");
            String rol = request.getParameter("rol");

            Usuario u = new Usuario();
            u.setIdUsuario(idUsuario);
            u.setNombre(nombre);
            u.setEmail(email);
            u.setTelefono(telefono);
            u.setRol(rol);

            ModeloUsuario mu = new ModeloUsuario();
            mu.modificarUsuario(u); 

            response.sendRedirect("admin_dashboard.jsp?user_updated=1");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("admin_dashboard.jsp?error=1");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}