/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */package servlet;

import Controlador.ModeloEvento;
import Modelo.Evento;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "ModificarEventoServlet", urlPatterns = {"/modificarEvento"})
public class ModificarEventoServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8"); // Importante para las actividades
        
        try {
            int idEvento = Integer.parseInt(request.getParameter("id_evento"));
            String nombreEvento = request.getParameter("nombre_evento");
            String fechaStr = request.getParameter("fecha_evento");
            String paquete = request.getParameter("paquete");
            double costo = Double.parseDouble(request.getParameter("costo"));
            String estado = request.getParameter("estado");
            
            // Capturar las actividades editadas
            String[] actividades = request.getParameterValues("actividades");

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            Date fechaEvento = sdf.parse(fechaStr);

            Evento evento = new Evento();
            evento.setIdEvento(idEvento);
            evento.setNombreEvento(nombreEvento);
            evento.setFechaEvento(fechaEvento);
            evento.setPaquete(paquete);
            evento.setCosto(costo);
            evento.setEstado(estado);

            ModeloEvento me = new ModeloEvento();
            
            // 1. Actualizar datos básicos del evento
            boolean exitoEvento = me.actualizarEventoCompleto(evento);
            
            // 2. Actualizar la lista de actividades
            me.actualizarActividadesEvento(idEvento, actividades);

            if (exitoEvento) {
                response.sendRedirect("admin_dashboard.jsp?event_updated=1");
            } else {
                response.sendRedirect("admin_dashboard.jsp?error=1");
            }

        } catch (ParseException | NumberFormatException e) {
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