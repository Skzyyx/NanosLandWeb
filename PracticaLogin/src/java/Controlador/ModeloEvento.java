/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */package Controlador;

import Modelo.Actividad;
import Modelo.Evento;
import com.mysql.cj.xdevapi.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

public class ModeloEvento extends Conexion {

    /**
     * CONSULTAR: Obtiene todos los eventos (para Admin y Trabajador)
     */
    public ArrayList<Evento> getAllEventos(String estadoFiltro) {
        ArrayList<Evento> eventos = new ArrayList<>();
        PreparedStatement pst = null;
        ResultSet rs = null;
        Connection con = getConexion();

        String sql = "SELECT e.*, u.nombre as nombre_cliente FROM eventos e "
                + "JOIN usuarios u ON e.id_cliente = u.id_usuario ";

        if (estadoFiltro != null && !estadoFiltro.isEmpty()) {
            sql += " WHERE e.estado = ?";
        }
        sql += " ORDER BY e.fecha_evento ASC";

        try {
            pst = con.prepareStatement(sql);
            if (estadoFiltro != null && !estadoFiltro.isEmpty()) {
                pst.setString(1, estadoFiltro);
            }
            rs = pst.executeQuery();

            while (rs.next()) {
                eventos.add(parseEventoConCliente(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cerrarRecursos(rs, pst, con);
        }
        return eventos;
    }

    /**
     * CONSULTAR: Obtiene solo los eventos "AGENDADOS" PARA UN CLIENTE. ¡AQUÍ
     * ESTÁ LA CORRECCIÓN!
     */
    public ArrayList<Evento> getEventosPorCliente(int idCliente) {
        ArrayList<Evento> eventos = new ArrayList<>();
        PreparedStatement pst = null;
        ResultSet rs = null;
        Connection con = getConexion();

        // --- ¡CAMBIO AQUÍ! ---
        // Se añadió "AND estado = 'agendado'" a la consulta.
        String sql = "SELECT * FROM eventos WHERE id_cliente = ? AND estado = 'agendado' ORDER BY fecha_evento ASC";

        try {
            pst = con.prepareStatement(sql);
            pst.setInt(1, idCliente);
            rs = pst.executeQuery();

            while (rs.next()) {
                eventos.add(parseEventoSimple(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cerrarRecursos(rs, pst, con);
        }
        return eventos;
    }

    /**
     * MODIFICAR: (Función del Trabajador - Marcar como terminado)
     */
    public boolean actualizarEstadoEvento(int idEvento, String nuevoEstado) {
        PreparedStatement pst = null;
        Connection con = getConexion();
        String sql = "UPDATE eventos SET estado = ? WHERE id_evento = ?";

        try {
            pst = con.prepareStatement(sql);
            pst.setString(1, nuevoEstado);
            pst.setInt(2, idEvento);

            if (pst.executeUpdate() == 1) {
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error en actualizarEstadoEvento: " + e);
        } finally {
            cerrarRecursos(null, pst, con);
        }
        return false;
    }

    /**
     * ELIMINAR: (Función del Admin)
     */
    public boolean eliminarEvento(int idEvento) {
        PreparedStatement pst = null;
        Connection con = getConexion();
        String sql = "DELETE FROM eventos WHERE id_evento = ?";

        try {
            pst = con.prepareStatement(sql);
            pst.setInt(1, idEvento);
            if (pst.executeUpdate() == 1) {
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error en eliminarEvento: " + e);
        } finally {
            cerrarRecursos(null, pst, con);
        }
        return false;
    }

    /**
     * CONSULTAR: Obtiene UN solo evento por su ID.
     */
    public Evento getEventoPorId(int idEvento) {
        PreparedStatement pst = null;
        ResultSet rs = null;
        Connection con = getConexion();
        String sql = "SELECT * FROM eventos WHERE id_evento = ?";

        try {
            pst = con.prepareStatement(sql);
            pst.setInt(1, idEvento);
            rs = pst.executeQuery();

            if (rs.next()) {
                return parseEventoSimple(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cerrarRecursos(rs, pst, con);
        }
        return null;
    }

    /**
     * MODIFICAR: Actualiza un evento completo desde el formulario del Admin.
     */
    public boolean actualizarEventoCompleto(Evento evento) {
        PreparedStatement pst = null;
        Connection con = getConexion();
        String sql = "UPDATE eventos SET nombre_evento = ?, fecha_evento = ?, "
                + "paquete = ?, costo = ?, estado = ? "
                + "WHERE id_evento = ?";

        try {
            pst = con.prepareStatement(sql);
            pst.setString(1, evento.getNombreEvento());
            pst.setTimestamp(2, new Timestamp(evento.getFechaEvento().getTime()));
            pst.setString(3, evento.getPaquete());
            pst.setDouble(4, evento.getCosto());
            pst.setString(5, evento.getEstado());
            pst.setInt(6, evento.getIdEvento());

            if (pst.executeUpdate() == 1) {
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error en actualizarEventoCompleto: " + e);
        } finally {
            cerrarRecursos(null, pst, con);
        }
        return false;
    }

    // --- MÉTODOS HELPER (AYUDANTES) ---
    private Evento parseEventoSimple(ResultSet rs) throws SQLException {
        Evento evento = new Evento();
        evento.setIdEvento(rs.getInt("id_evento"));
        evento.setIdCliente(rs.getInt("id_cliente"));
        evento.setNombreEvento(rs.getString("nombre_evento"));
        evento.setFechaEvento(rs.getTimestamp("fecha_evento"));
        evento.setPaquete(rs.getString("paquete"));
        evento.setCosto(rs.getDouble("costo"));
        evento.setEstado(rs.getString("estado"));
        return evento;
    }

    private Evento parseEventoConCliente(ResultSet rs) throws SQLException {
        Evento evento = parseEventoSimple(rs);
        evento.setNombreCliente(rs.getString("nombre_cliente"));
        return evento;
    }

    private void cerrarRecursos(ResultSet rs, PreparedStatement pst, Connection con) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (pst != null) {
                pst.close();
            }
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int agregarEventoRetornandoId(Evento evento) {
        PreparedStatement pst = null;
        ResultSet rs = null;
        Connection con = getConexion();
        String sql = "INSERT INTO eventos (id_cliente, nombre_evento, fecha_evento, paquete, costo, estado) VALUES (?, ?, ?, ?, ?, 'agendado')";
        int idGenerado = -1;

        try {
            // RETURN_GENERATED_KEYS es necesario para saber qué ID se creó
            pst = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            pst.setInt(1, evento.getIdCliente());
            pst.setString(2, evento.getNombreEvento());
            pst.setTimestamp(3, new Timestamp(evento.getFechaEvento().getTime()));
            pst.setString(4, evento.getPaquete());
            pst.setDouble(5, evento.getCosto());

            int filasAfectadas = pst.executeUpdate();
            if (filasAfectadas > 0) {
                rs = pst.getGeneratedKeys();
                if (rs.next()) {
                    idGenerado = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error en agregarEventoRetornandoId: " + e);
        } finally {
            cerrarRecursos(rs, pst, con);
        }
        return idGenerado;
    }

    public boolean agregarEvento(Evento evento) {
        return agregarEventoRetornandoId(evento) != -1;
    }

    public boolean agregarActividad(int idEvento, String descripcion) {
        PreparedStatement pst = null;
        Connection con = getConexion();
        String sql = "INSERT INTO actividades (id_evento, descripcion, completado) VALUES (?, ?, 0)";
        try {
            pst = con.prepareStatement(sql);
            pst.setInt(1, idEvento);
            pst.setString(2, descripcion);
            return pst.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrarRecursos(null, pst, con);
        }
        return false;
    }

    public ArrayList<Actividad> getActividadesPorEvento(int idEvento) {
        ArrayList<Actividad> lista = new ArrayList<>();
        PreparedStatement pst = null;
        ResultSet rs = null;
        Connection con = getConexion();
        String sql = "SELECT * FROM actividades WHERE id_evento = ?";
        try {
            pst = con.prepareStatement(sql);
            pst.setInt(1, idEvento);
            rs = pst.executeQuery();
            while (rs.next()) {
                lista.add(new Actividad(
                        rs.getInt("id_actividad"),
                        rs.getInt("id_evento"),
                        rs.getString("descripcion"),
                        rs.getBoolean("completado")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrarRecursos(rs, pst, con);
        }
        return lista;
    }

    public boolean toggleActividad(int idActividad, boolean estado) {
        PreparedStatement pst = null;
        Connection con = getConexion();
        String sql = "UPDATE actividades SET completado = ? WHERE id_actividad = ?";
        try {
            pst = con.prepareStatement(sql);
            pst.setInt(1, estado ? 1 : 0);
            pst.setInt(2, idActividad);
            return pst.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrarRecursos(null, pst, con);
        }
        return false;
    }

    // Verifica si TODAS las actividades de un evento están completas
    public boolean verificarTodasCompletas(int idEvento) {
        PreparedStatement pst = null;
        ResultSet rs = null;
        Connection con = getConexion();
        // Contamos cuántas hay SIN completar
        String sql = "SELECT COUNT(*) FROM actividades WHERE id_evento = ? AND completado = 0";
        try {
            pst = con.prepareStatement(sql);
            pst.setInt(1, idEvento);
            rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) == 0; // Si el conteo es 0, todo está listo
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrarRecursos(rs, pst, con);
        }
        return false; // Por seguridad
    }
    
    public void actualizarActividadesEvento(int idEvento, String[] nuevasActividades) {
        PreparedStatement pstDelete = null;
        PreparedStatement pstInsert = null;
        Connection con = getConexion();
        
        String sqlDelete = "DELETE FROM actividades WHERE id_evento = ?";
        String sqlInsert = "INSERT INTO actividades (id_evento, descripcion, completado) VALUES (?, ?, 0)";
        
        try {
            // 1. Borrar las existentes
            pstDelete = con.prepareStatement(sqlDelete);
            pstDelete.setInt(1, idEvento);
            pstDelete.executeUpdate();
            
            // 2. Insertar las nuevas si hay
            if (nuevasActividades != null && nuevasActividades.length > 0) {
                pstInsert = con.prepareStatement(sqlInsert);
                for (String act : nuevasActividades) {
                    if (act != null && !act.trim().isEmpty()) {
                        pstInsert.setInt(1, idEvento);
                        pstInsert.setString(2, act.trim());
                        pstInsert.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrarRecursos(null, pstDelete, null); // No cerramos conexión aun
            cerrarRecursos(null, pstInsert, con);  // Cerramos todo al final
        }
    }
}
