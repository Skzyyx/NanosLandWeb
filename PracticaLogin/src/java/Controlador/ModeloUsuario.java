/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */package Controlador;

import Modelo.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList; 

// VERSIÓN COMPLETA Y FINAL
public class ModeloUsuario extends Conexion {

    // REGISTRO (para Clientes)
    public boolean registrarCliente(Usuario usuario) {
        PreparedStatement pst = null;
        Connection con = getConexion(); 
        String passPlano = usuario.getPass(); 
        String sql = "INSERT INTO usuarios (nombre, email, pass, rol, telefono) VALUES (?, ?, ?, 'cliente', ?)";

        try {
            pst = con.prepareStatement(sql);
            pst.setString(1, usuario.getNombre());
            pst.setString(2, usuario.getEmail());
            pst.setString(3, passPlano);
            pst.setString(4, usuario.getTelefono());
            if (pst.executeUpdate() == 1) {
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error en registrarCliente: " + e);
        } finally {
            cerrarRecursos(null, pst, con);
        }
        return false;
    }

    // AUTENTICACIÓN (para todos los roles)
    public Usuario autenticacion(String email, String pass) {
        PreparedStatement pst = null;
        ResultSet rs = null;
        Connection con = getConexion();
        String sql = "SELECT * FROM usuarios WHERE email = ?";

        try {
            pst = con.prepareStatement(sql);
            pst.setString(1, email);
            rs = pst.executeQuery();

            if (rs.next()) {
                if (pass.equals(rs.getString("pass"))) {
                    return new Usuario(
                        rs.getInt("id_usuario"),
                        rs.getString("nombre"),
                        rs.getString("email"),
                        rs.getString("rol"),
                        rs.getString("telefono")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error en autenticacion: " + e);
        } finally {
            cerrarRecursos(rs, pst, con);
        }
        return null;
    }
    
    // MODIFICAR (Perfil de Usuario)
    public boolean modificarUsuario(Usuario usuario) {
        PreparedStatement pst = null;
        Connection con = getConexion();
        String sql = "UPDATE usuarios SET nombre = ?, email = ?, telefono = ? WHERE id_usuario = ?";
        
        try {
            pst = con.prepareStatement(sql);
            pst.setString(1, usuario.getNombre());
            pst.setString(2, usuario.getEmail());
            pst.setString(3, usuario.getTelefono());
            pst.setInt(4, usuario.getIdUsuario());
            if (pst.executeUpdate() == 1) {
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error en modificarUsuario: " + e);
        } finally {
             cerrarRecursos(null, pst, con);
        }
        return false;
    }
    
    // --- MÉTODOS NUEVOS PARA EL ADMIN ---

    /**
     * CONSULTAR: Obtiene TODOS los usuarios (para el Admin)
     */
    public ArrayList<Usuario> getAllUsuarios() {
        ArrayList<Usuario> usuarios = new ArrayList<>();
        PreparedStatement pst = null;
        ResultSet rs = null;
        Connection con = getConexion();
        String sql = "SELECT * FROM usuarios ORDER BY rol, nombre";

        try {
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();

            while (rs.next()) {
                usuarios.add(new Usuario(
                    rs.getInt("id_usuario"),
                    rs.getString("nombre"),
                    rs.getString("email"),
                    rs.getString("rol"),
                    rs.getString("telefono")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error en getAllUsuarios: " + e);
        } finally {
            cerrarRecursos(rs, pst, con);
        }
        return usuarios;
    }

    /**
     * REGISTRAR (Admin): Registra cualquier tipo de rol.
     */
    public boolean registrarUsuarioAdmin(Usuario usuario) {
        PreparedStatement pst = null;
        Connection con = getConexion();
        String passPlano = usuario.getPass(); 
        String sql = "INSERT INTO usuarios (nombre, email, pass, rol, telefono) VALUES (?, ?, ?, ?, ?)";

        try {
            pst = con.prepareStatement(sql);
            pst.setString(1, usuario.getNombre());
            pst.setString(2, usuario.getEmail());
            pst.setString(3, passPlano);
            pst.setString(4, usuario.getRol()); 
            pst.setString(5, usuario.getTelefono());
            
            if (pst.executeUpdate() == 1) {
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error en registrarUsuarioAdmin: " + e);
        } finally {
            cerrarRecursos(null, pst, con);
        }
        return false;
    }

    /**
     * ELIMINAR: Borra un usuario (para el Admin)
     */
    public boolean eliminarUsuario(int idUsuario) {
        // PRECAUCIÓN: No se debe poder borrar el admin 1
        if (idUsuario == 1) {
            System.err.println("Intento de borrar al admin principal (ID 1). Denegado.");
            return false;
        }
        
        PreparedStatement pst = null;
        Connection con = getConexion();
        String sql = "DELETE FROM usuarios WHERE id_usuario = ?";
        
        try {
            pst = con.prepareStatement(sql);
            pst.setInt(1, idUsuario);
            if (pst.executeUpdate() == 1) {
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error en eliminarUsuario: " + e);
        } finally {
             cerrarRecursos(null, pst, con);
        }
        return false;
    }

    public ArrayList<Usuario> getAllClientes() {
        ArrayList<Usuario> usuarios = new ArrayList<>();
        PreparedStatement pst = null;
        ResultSet rs = null;
        Connection con = getConexion();
        String sql = "SELECT * FROM usuarios WHERE rol = 'cliente' ORDER BY nombre";

        try {
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();

            while (rs.next()) {
                usuarios.add(new Usuario(
                    rs.getInt("id_usuario"),
                    rs.getString("nombre"),
                    rs.getString("email"),
                    rs.getString("rol"),
                    rs.getString("telefono")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error en getAllClientes: " + e);
        } finally {
            cerrarRecursos(rs, pst, con); 
        }
        return usuarios;
    }
    // Método helper para cerrar todo
    private void cerrarRecursos(ResultSet rs, PreparedStatement pst, Connection con) {
        try {
            if (rs != null) rs.close();
            if (pst != null) pst.close();
            if (con != null) con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}