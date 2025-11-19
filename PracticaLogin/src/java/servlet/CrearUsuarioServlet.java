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

// Este Servlet maneja el formulario "Crear Nuevo Usuario" del Admin
@WebServlet(name = "CrearUsuarioServlet", urlPatterns = {"/crearUsuario"})
public class CrearUsuarioServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // 1. Obtener todos los datos del formulario del admin
        String nombre = request.getParameter("nombre");
        String email = request.getParameter("email");
        String pass = request.getParameter("pass");
        String telefono = request.getParameter("telefono");
        String rol = request.getParameter("rol"); // El rol que el admin seleccionó

        // 2. Crear el objeto Usuario
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(nombre);
        nuevoUsuario.setEmail(email);
        nuevoUsuario.setPass(pass); // (pass en texto plano '123')
        nuevoUsuario.setTelefono(telefono);
        nuevoUsuario.setRol(rol); // Asignamos el rol del admin

        // 3. Llamar al modelo
        ModeloUsuario mu = new ModeloUsuario();
        mu.registrarUsuarioAdmin(nuevoUsuario); // Usamos el método especial del admin
        
        // 4. Redirigir al dashboard con un mensaje de éxito
        response.sendRedirect("admin_dashboard.jsp?user_created=1");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}