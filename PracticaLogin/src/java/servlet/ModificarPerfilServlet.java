/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */package servlet;

import Controlador.ModeloUsuario;
import Modelo.Usuario;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "ModificarPerfilServlet", urlPatterns = {"/modificarPerfil"})
public class ModificarPerfilServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8"); 
        
        HttpSession session = request.getSession(false);
        Usuario usuarioSesion = (session != null) ? (Usuario) session.getAttribute("usuario") : null;
        
        if (usuarioSesion == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String accion = request.getParameter("accion"); 
        ModeloUsuario mu = new ModeloUsuario();
        
        if ("datos".equals(accion)) {
            // --- ACTUALIZAR DATOS DE CONTACTO ---
            String nombre = request.getParameter("nombre");
            String email = request.getParameter("email");
            String telefono = request.getParameter("telefono");
            
            // Creamos un objeto temporal solo con los datos nuevos
            Usuario u = new Usuario();
            u.setIdUsuario(usuarioSesion.getIdUsuario());
            u.setNombre(nombre);
            u.setEmail(email);
            u.setTelefono(telefono);
            
            if (mu.actualizarDatosContacto(u)) {
                // Actualizamos la sesión (Mantenemos ID, Rol y Password del original)
                usuarioSesion.setNombre(nombre);
                usuarioSesion.setEmail(email);
                usuarioSesion.setTelefono(telefono);
                
                session.setAttribute("usuario", usuarioSesion);
                response.sendRedirect("cliente_dashboard.jsp?perfil_updated=1");
            } else {
                response.sendRedirect("cliente_dashboard.jsp?error=1");
            }

        } else if ("pass".equals(accion)) {
            // --- CAMBIAR CONTRASEÑA (Ahora validando contra BD) ---
            String passActualIngresada = request.getParameter("current_pass");
            String passNueva = request.getParameter("new_pass");
            String passConfirm = request.getParameter("confirm_pass");
            
            // 1. Validar contra la BASE DE DATOS (Seguro)
            if (!mu.verificarContrasenaActual(usuarioSesion.getIdUsuario(), passActualIngresada)) {
                response.sendRedirect("cliente_dashboard.jsp?error_pass=incorrecta");
                return;
            }
            
            // 2. Validar coincidencia
            if (!passNueva.equals(passConfirm)) {
                response.sendRedirect("cliente_dashboard.jsp?error_pass=nocoincide");
                return;
            }
            
            // 3. Actualizar en BD
            if (mu.actualizarPassword(usuarioSesion.getIdUsuario(), passNueva)) {
                // Actualizamos la sesión también para que no quede desfasada
                usuarioSesion.setPass(passNueva);
                session.setAttribute("usuario", usuarioSesion);
                response.sendRedirect("cliente_dashboard.jsp?pass_updated=1");
            } else {
                response.sendRedirect("cliente_dashboard.jsp?error=1");
            }
        } else {
            response.sendRedirect("cliente_dashboard.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}