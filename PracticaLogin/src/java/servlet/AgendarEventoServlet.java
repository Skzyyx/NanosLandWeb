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

@WebServlet(name = "AgendarEventoServlet", urlPatterns = {"/agendarEvento"})
public class AgendarEventoServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8"); // Para tildes
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuario");
        if (!"admin".equals(usuarioLogueado.getRol())) {
            response.sendRedirect("logout.jsp");
            return;
        }

        try {
            String nombreEvento = request.getParameter("nombre_evento");
            String fechaStr = request.getParameter("fecha_evento"); 
            String paquete = request.getParameter("paquete");
            double costo = Double.parseDouble(request.getParameter("costo"));
            int idCliente = Integer.parseInt(request.getParameter("id_cliente"));

            // Obtener las actividades manuales del formulario
            String[] actividadesManuales = request.getParameterValues("actividades");

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            Date fechaEvento = sdf.parse(fechaStr);

            Evento evento = new Evento();
            evento.setIdCliente(idCliente);
            evento.setNombreEvento(nombreEvento);
            evento.setFechaEvento(fechaEvento);
            evento.setPaquete(paquete);
            evento.setCosto(costo);

            ModeloEvento me = new ModeloEvento();
            int idEventoGenerado = me.agregarEventoRetornandoId(evento);

            if (idEventoGenerado != -1) {
                
                // --- CORRECCIÓN: YA NO AGREGAMOS ACTIVIDADES AUTOMÁTICAS ---
                // Solo se guardarán las que el administrador haya escrito explícitamente.

                // Procesar actividades manuales
                if (actividadesManuales != null) {
                    for (String actividad : actividadesManuales) {
                        if (actividad != null && !actividad.trim().isEmpty()) {
                            me.agregarActividad(idEventoGenerado, actividad.trim());
                        }
                    }
                }
                
                // Si no agregó ninguna actividad manual, podríamos agregar una por defecto
                // para que la lista no quede vacía (Opcional, descomentar si se desea)
                /*
                if (actividadesManuales == null || actividadesManuales.length == 0) {
                    me.agregarActividad(idEventoGenerado, "Verificar detalles del evento");
                }
                */

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