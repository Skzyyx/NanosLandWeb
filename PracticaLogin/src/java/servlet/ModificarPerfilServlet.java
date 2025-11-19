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

// Este Servlet hace que funcione el formulario de perfil
@WebServlet(name = "ModificarPerfilServlet", urlPatterns = {"/modificarPerfil"})
public class ModificarPerfilServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect("index.jsp");
            return;
        }
        
        try {
            // 1. Obtener datos del formulario
            int idUsuario = Integer.parseInt(request.getParameter("idUsuario"));
            String nombre = request.getParameter("nombre");
            String email = request.getParameter("email");
            String telefono = request.getParameter("telefono");

            // 2. Obtener usuario de la sesión para mantener ROL y PASS
            Usuario usuarioActual = (Usuario) session.getAttribute("usuario");
            
            // 3. Crear el objeto actualizado
            Usuario usuarioModificado = new Usuario();
            usuarioModificado.setIdUsuario(idUsuario);
            usuarioModificado.setNombre(nombre);
            usuarioModificado.setEmail(email);
            usuarioModificado.setTelefono(telefono);
            // Mantenemos los datos que no se modifican desde la sesión
            usuarioModificado.setRol(usuarioActual.getRol()); 

            // 4. Llamar al modelo
            ModeloUsuario mu = new ModeloUsuario();
            boolean exito = mu.modificarUsuario(usuarioModificado);

            if (exito) {
                // 5. Actualizar el objeto en la sesión
                session.setAttribute("usuario", usuarioModificado);
                response.sendRedirect("cliente_dashboard.jsp?exito_perfil=1");
            } else {
                 response.sendRedirect("cliente_dashboard.jsp?error_perfil=1");
            }
            
        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.sendRedirect("cliente_dashboard.jsp?error_perfil=1");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}