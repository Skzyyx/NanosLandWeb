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

@WebServlet(name = "RegistroServlet", urlPatterns = {"/registrar"})
public class RegistroServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        String nombre = request.getParameter("nombre");
        String email = request.getParameter("email");
        String telefono = request.getParameter("telefono");
        String pass = request.getParameter("pass");
        String passConfirm = request.getParameter("pass2");

        if (pass == null || !pass.equals(passConfirm)) {
            response.sendRedirect("registro.jsp?error=1");
            return;
        }
        
        Usuario nuevoCliente = new Usuario();
        nuevoCliente.setNombre(nombre);
        nuevoCliente.setEmail(email);
        nuevoCliente.setTelefono(telefono);
        nuevoCliente.setPass(pass); 

        ModeloUsuario mu = new ModeloUsuario();
        boolean exito = mu.registrarCliente(nuevoCliente);
        
        if (exito) {
            response.sendRedirect("login.jsp?exito=1");
        } else {
            response.sendRedirect("registro.jsp?error=2");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("registro.jsp");
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
}