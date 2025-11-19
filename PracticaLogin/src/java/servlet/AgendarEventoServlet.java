/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */package servlet;

import Controlador.ModeloEvento;
import Modelo.Evento;
import Modelo.Usuario;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "AgendarEventoServlet", urlPatterns = {"/agendarEvento"})
public class AgendarEventoServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        // 1. Verificar que el usuario esté logueado
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuario");
        
        // 2. 
        // Solo el 'admin' puede agendar. 
        if (!"admin".equals(usuarioLogueado.getRol())) {
            response.sendRedirect("logout.jsp"); // Si no es admin, lo sacamos
            return;
        }

        try {
            // 3. Obtener datos del formulario del admin
            String nombreEvento = request.getParameter("nombre_evento");
            String fechaStr = request.getParameter("fecha_evento"); 
            String paquete = request.getParameter("paquete");
            double costo = Double.parseDouble(request.getParameter("costo"));
            int idCliente = Integer.parseInt(request.getParameter("id_cliente"));

            // 4. Convertir la fecha
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            Date fechaEvento = sdf.parse(fechaStr);

            // 5. Crear el objeto Evento
            Evento evento = new Evento();
            evento.setIdCliente(idCliente); // ID del cliente seleccionado por el admin
            evento.setNombreEvento(nombreEvento);
            evento.setFechaEvento(fechaEvento);
            evento.setPaquete(paquete);
            evento.setCosto(costo);

            // 6. Guardar en la base de datos
            ModeloEvento me = new ModeloEvento();
            boolean exito = me.agregarEvento(evento);

            // 7.
            // Redirigimos de vuelta al panel del ADMIN
            if (exito) {

                response.sendRedirect("admin_dashboard.jsp?exito_agenda=1");
            } else {
                response.sendRedirect("admin_dashboard.jsp?error_agenda=1");
            }

        } catch (ParseException | NumberFormatException e) {
            e.printStackTrace();
            response.sendRedirect("admin_dashboard.jsp?error_agenda=1");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}