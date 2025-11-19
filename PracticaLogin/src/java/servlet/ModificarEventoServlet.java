/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servlet;

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

@WebServlet(name = "ModificarEventoServlet", urlPatterns = {"/modificarEvento"})
public class ModificarEventoServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // 1. Proteger el servlet
        HttpSession session = request.getSession(false);
        Usuario usuario = (session != null) ? (Usuario) session.getAttribute("usuario") : null;
        if (usuario == null || !usuario.getRol().equals("admin")) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            // 2. Obtener todos los datos del formulario
            int idEvento = Integer.parseInt(request.getParameter("id_evento"));
            String nombreEvento = request.getParameter("nombre_evento");
            String fechaStr = request.getParameter("fecha_evento"); // "2025-11-30T15:00"
            String paquete = request.getParameter("paquete");
            double costo = Double.parseDouble(request.getParameter("costo"));
            String estado = request.getParameter("estado");
            
            // 3. Parsear la fecha
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            Date fechaEvento = sdf.parse(fechaStr);

            // 4. Crear el objeto Evento con los datos actualizados
            Evento evento = new Evento();
            evento.setIdEvento(idEvento);
            evento.setNombreEvento(nombreEvento);
            evento.setFechaEvento(fechaEvento);
            evento.setPaquete(paquete);
            evento.setCosto(costo);
            evento.setEstado(estado);

            // 5. Llamar al modelo para guardar en la BD
            ModeloEvento me = new ModeloEvento();
            me.actualizarEventoCompleto(evento);

            // 6. Redirigir al panel de admin (idealmente con un mensaje)
         
            response.sendRedirect("admin_dashboard.jsp?event_updated=1"); 

        } catch (ParseException | NumberFormatException e) {
            e.printStackTrace();
            // Si algo falla, volver al panel
            response.sendRedirect("admin_dashboard.jsp?error=3");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}