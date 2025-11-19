/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.Usuario;
import java.util.ArrayList;

/**
 * Esta clase genera el HTML para la gestión de Usuarios en el panel de Admin.
 */
public class ControladorUsuario {

    /**
     * Genera la tabla HTML para "Gestionar Usuarios" (Admin)
     */
    public String getUsuariosHTML() {
        ModeloUsuario mu = new ModeloUsuario();
        ArrayList<Usuario> lista = mu.getAllUsuarios(); // Admin ve a TODOS
        String htmlcode = "";

        if (lista.isEmpty()) {
            return "<tr><td colspan='5'>No hay usuarios registrados.</td></tr>";
        }

        for (Usuario u : lista) {
            htmlcode += "<tr>\n"
                    + "<td>" + u.getIdUsuario() + "</td>\n"
                    + "<td>" + u.getNombre() + "</td>\n"
                    + "<td>" + u.getEmail() + "</td>\n"
                    + "<td><span class='badge badge-secondary'>" + u.getRol() + "</span></td>\n"
                    + "<td>\n"
                    // Botón de Modificar (CRUD - Update)
                    + "<a href='modificar_usuario.jsp?id=" + u.getIdUsuario() + "' class='btn btn-warning btn-sm mr-2'>\n" // (Opcional, si lo quieres añadir)
                    + "<i class='fas fa-edit'></i>\n"
                    + "</a>\n"
                    // Botón de Eliminar (CRUD - Delete)
                    + "<form action='eliminarUsuario' method='POST' style='display:inline;' onsubmit='return confirm(\"¿Estás seguro de eliminar a " + u.getNombre() + "?\");'>\n"
                    + "<input type='hidden' name='idUsuario' value='" + u.getIdUsuario() + "'>\n"
                    + "<button type='submit' class='btn btn-danger btn-sm' " + (u.getIdUsuario() == 1 ? "disabled" : "") + ">\n"
                    + "<i class='fas fa-trash'></i>\n"
                    + "</button>\n"
                    + "</form>\n"
                    + "</td>\n"
                    + "</tr>\n";
        }
        return htmlcode;
    }
    
    /**
     * Genera las <option> para un <select> de clientes.
     * Usado en el formulario de agendar del administrador.
     */
    public String getClientesDropdownHTML() {
        ModeloUsuario mu = new ModeloUsuario();
        
        ArrayList<Usuario> lista = mu.getAllClientes(); 
        String htmlcode = "";

        if (lista.isEmpty()) {
            return "<option value=''>No hay clientes registrados</option>";
        }
        
        for (Usuario u : lista) {
             htmlcode += "<option value='" + u.getIdUsuario() + "'>" + u.getNombre() + " (Email: " + u.getEmail() + ")</option>\n";
        }
        return htmlcode;
    }
}