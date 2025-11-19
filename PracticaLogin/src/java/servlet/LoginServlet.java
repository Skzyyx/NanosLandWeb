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

@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        String email = request.getParameter("email");
        String pass = request.getParameter("pass");
        
        ModeloUsuario mu = new ModeloUsuario();
        Usuario usuario = mu.autenticacion(email, pass);

        if (usuario != null) {
            HttpSession objSession = request.getSession(true);
            objSession.setAttribute("usuario", usuario); 
            
            // REDIRECCIÓN BASADA EN ROL
            switch (usuario.getRol()) {
                case "admin":
                    response.sendRedirect("admin_dashboard.jsp");
                    break;
                case "trabajador":
                    response.sendRedirect("worker_dashboard.jsp");
                    break;
                case "cliente":
                    response.sendRedirect("cliente_dashboard.jsp");
                    break;
                default:
                    response.sendRedirect("login.jsp");
            }
        } else {
            response.sendRedirect("login.jsp?error=1");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
}