/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */package Controlador;

import Modelo.Evento;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date; 
import java.util.concurrent.TimeUnit; 

public class ControladorEvento {

    /**
     * Esta función es para el TRABAJADOR
     * 
     */
    public String getEventosPendientesHTML() {
        ModeloEvento me = new ModeloEvento();
        ArrayList<Evento> lista = me.getAllEventos("agendado");
        String htmlcode = "";
        
        if(lista.isEmpty()) {
            return "<tr><td colspan='5' class='text-center'>No hay eventos pendientes.</td></tr>";
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy 'a las' HH:mm");

        for (Evento evento : lista) {
            htmlcode += "<tr>\n"
                    + "<td>" + evento.getNombreEvento() + "</td>\n"
                    + "<td>" + evento.getNombreCliente() + "</td>\n"
                    + "<td>" + sdf.format(evento.getFechaEvento()) + "</td>\n"
                    + "<td>" + evento.getPaquete() + "</td>\n"
                    + "<td>\n"
                    // ---  (onsubmit) ---
                    + "<form action='marcarTerminado' method='POST' style='display:inline;' onsubmit='return confirm(\"¿Confirmas que el evento " + evento.getNombreEvento() + " ha finalizado?\");'>\n"
                    + "<input type='hidden' name='idEvento' value='" + evento.getIdEvento() + "'>\n"
                    + "<button type='submit' class='btn btn-success btn-sm'>Marcar Terminado</button>\n"
                    + "</form>\n"
                    + "</td>\n"
                    + "</tr>\n";
        }
        return htmlcode;
    }
    
    /**
     * Esta función es para el CLIENTE (Semáforo y Colores)
     */
    public String getMisEventosHTML(int idCliente) {
        ModeloEvento me = new ModeloEvento();
        ArrayList<Evento> lista = me.getEventosPorCliente(idCliente); 
        String htmlcode = "";

        if (lista.isEmpty()) {
            return "<tr><td colspan='4' class='text-center'>No tienes eventos agendados.</td></tr>";
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy 'a las' HH:mm");
        Date fechaActual = new Date();

        for (Evento evento : lista) {
            long diferenciaEnMillies = evento.getFechaEvento().getTime() - fechaActual.getTime();
            long diasRestantes = TimeUnit.DAYS.convert(diferenciaEnMillies, TimeUnit.MILLISECONDS);
            
            String claseColor = "";
            String mensajeAlerta = "";
            
            if (diasRestantes < 0) {
                claseColor = "table-secondary"; 
                mensajeAlerta = "El evento ya pasó";
            } else if (diasRestantes <= 3) {
                claseColor = "table-danger"; 
                mensajeAlerta = "¡URGENTE! Faltan " + diasRestantes + " días";
            } else if (diasRestantes <= 7) {
                claseColor = "table-warning"; 
                mensajeAlerta = "Próximo (Faltan " + diasRestantes + " días)";
            } else {
                claseColor = "table-success"; 
                mensajeAlerta = "Faltan " + diasRestantes + " días";
            }

            htmlcode += "<tr class='" + claseColor + "'>\n"
                    + "<td><strong>" + evento.getNombreEvento() + "</strong></td>\n"
                    + "<td>" + sdf.format(evento.getFechaEvento()) + "</td>\n"
                    + "<td>" + evento.getPaquete() + "</td>\n"
                    + "<td><strong>" + mensajeAlerta + "</strong></td>\n"
                    + "</tr>\n";
        }
        return htmlcode;
    }
    
    /**
     * Esta función es para el ADMIN
     */
    public String getTodosEventosHTML() {
        ModeloEvento me = new ModeloEvento();
        ArrayList<Evento> lista = me.getAllEventos(null);
        String htmlcode = "";

        if (lista.isEmpty()) {
            return "<tr><td colspan='7' class='text-center'>No hay eventos en el sistema.</td></tr>";
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        for (Evento evento : lista) {
            String estadoBadge;
            if ("agendado".equals(evento.getEstado())) {
                estadoBadge = "<span class='badge badge-info'>Agendado</span>";
            } else {
                estadoBadge = "<span class='badge badge-success'>Terminado</span>";
            }
            
            htmlcode += "<tr>\n"
                    + "<td>" + evento.getIdEvento() + "</td>\n"
                    + "<td>" + evento.getNombreEvento() + "</td>\n"
                    + "<td>" + evento.getNombreCliente() + "</td>\n"
                    + "<td>" + sdf.format(evento.getFechaEvento()) + "</td>\n"
                    + "<td>" + evento.getPaquete() + "</td>\n"
                    + "<td>" + estadoBadge + "</td>\n"
                    + "<td>\n"
                    + "<a href='modificar_evento.jsp?id=" + evento.getIdEvento() + "' class='btn btn-warning btn-sm mr-2'><i class='fas fa-edit'></i></a>\n"
                    + "<form action='eliminarEvento' method='POST' style='display:inline;' onsubmit='return confirm(\"¿Estás seguro de eliminar este evento?\");'>\n"
                    + "<input type='hidden' name='idEvento' value='" + evento.getIdEvento() + "'>\n"
                    + "<button type='submit' class='btn btn-danger btn-sm'><i class='fas fa-trash'></i></button>\n"
                    + "</form>\n"
                    + "</td>\n"
                    + "</tr>\n";
        }
        return htmlcode;
    }
}