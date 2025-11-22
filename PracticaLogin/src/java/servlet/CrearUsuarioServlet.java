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

@WebServlet(name = "CrearUsuarioServlet", urlPatterns = {"/crearUsuario"})
public class CrearUsuarioServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // 1. Obtener datos
        String nombre = request.getParameter("nombre");
        String email = request.getParameter("email");
        String pass = request.getParameter("pass");
        String telefono = request.getParameter("telefono");
        String rol = request.getParameter("rol");

        ModeloUsuario mu = new ModeloUsuario();

        // --- VALIDACIÓN DE CORREO DUPLICADO ---
        if (mu.existeEmail(email)) {
            // Si el correo ya existe, redirigir con un error específico
            response.sendRedirect("admin_dashboard.jsp?error_email_duplicado=1");
            return; // Detenemos la ejecución aquí
        }
        // --------------------------------------

        // 2. Crear el objeto Usuario
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(nombre);
        nuevoUsuario.setEmail(email);
        nuevoUsuario.setPass(pass);
        nuevoUsuario.setTelefono(telefono);
        nuevoUsuario.setRol(rol);

        // 3. Guardar en la base de datos
        mu.registrarUsuarioAdmin(nuevoUsuario);
        
        // 4. Éxito
        response.sendRedirect("admin_dashboard.jsp?user_created=1");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}