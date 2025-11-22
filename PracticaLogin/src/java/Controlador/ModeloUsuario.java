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

    // AUTENTICACIÓN
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

    // --- MÉTODOS ADMIN ---
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

    public boolean eliminarUsuario(int idUsuario) {
        if (idUsuario == 1) {
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

    public Usuario getUsuarioPorId(int idUsuario) {
        PreparedStatement pst = null;
        ResultSet rs = null;
        Connection con = getConexion();
        String sql = "SELECT * FROM usuarios WHERE id_usuario = ?";
        try {
            pst = con.prepareStatement(sql);
            pst.setInt(1, idUsuario);
            rs = pst.executeQuery();
            if (rs.next()) {
                return new Usuario(
                        rs.getInt("id_usuario"),
                        rs.getString("nombre"),
                        rs.getString("email"),
                        rs.getString("rol"),
                        rs.getString("telefono")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error en getUsuarioPorId: " + e);
        } finally {
            cerrarRecursos(rs, pst, con);
        }
        return null;
    }

    // ---  MÉTODO PARA VALIDAR EMAIL ---
    public boolean existeEmail(String email) {
        PreparedStatement pst = null;
        ResultSet rs = null;
        Connection con = getConexion();
        String sql = "SELECT id_usuario FROM usuarios WHERE email = ?";

        try {
            pst = con.prepareStatement(sql);
            pst.setString(1, email);
            rs = pst.executeQuery();

            if (rs.next()) {
                return true; // ¡El correo ya existe!
            }
        } catch (SQLException e) {
            System.err.println("Error en existeEmail: " + e);
        } finally {
            cerrarRecursos(rs, pst, con);
        }
        return false;
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
    
    public boolean modificarUsuario(Usuario usuario) {
        PreparedStatement pst = null;
        Connection con = getConexion();
        String sql = "UPDATE usuarios SET nombre = ?, email = ?, telefono = ?, rol = ? WHERE id_usuario = ?";
        
        try {
            pst = con.prepareStatement(sql);
            pst.setString(1, usuario.getNombre());
            pst.setString(2, usuario.getEmail());
            pst.setString(3, usuario.getTelefono());
            pst.setString(4, usuario.getRol());
            pst.setInt(5, usuario.getIdUsuario());
            
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


    public boolean actualizarPerfilCliente(Usuario usuario) {
        PreparedStatement pst = null;
        Connection con = getConexion();
        // NO actualizamos el 'rol' aquí por seguridad
        String sql = "UPDATE usuarios SET nombre = ?, email = ?, telefono = ?, pass = ? WHERE id_usuario = ?";

        try {
            pst = con.prepareStatement(sql);
            pst.setString(1, usuario.getNombre());
            pst.setString(2, usuario.getEmail());
            pst.setString(3, usuario.getTelefono());
            pst.setString(4, usuario.getPass());
            pst.setInt(5, usuario.getIdUsuario());

            if (pst.executeUpdate() == 1) {
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error en actualizarPerfilCliente: " + e);
        } finally {
            cerrarRecursos(null, pst, con);
        }
        return false;
    }

    // --- NUEVOS MÉTODOS PARA MODIFICACIÓN SEGURA ---
    // 1. Solo actualiza info de contacto (sin tocar password)
    public boolean actualizarDatosContacto(Usuario usuario) {
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
            System.err.println("Error en actualizarDatosContacto: " + e);
        } finally {
            cerrarRecursos(null, pst, con);
        }
        return false;
    }

    // 2. Solo actualiza la contraseña
    public boolean actualizarPassword(int idUsuario, String nuevaPass) {
        PreparedStatement pst = null;
        Connection con = getConexion();
        String sql = "UPDATE usuarios SET pass = ? WHERE id_usuario = ?";

        try {
            pst = con.prepareStatement(sql);
            pst.setString(1, nuevaPass);
            pst.setInt(2, idUsuario);

            if (pst.executeUpdate() == 1) {
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error en actualizarPassword: " + e);
        } finally {
            cerrarRecursos(null, pst, con);
        }
        return false;
    }
    
    public boolean verificarContrasenaActual(int idUsuario, String passwordActual) {
        PreparedStatement pst = null;
        ResultSet rs = null;
        Connection con = getConexion();
        String sql = "SELECT pass FROM usuarios WHERE id_usuario = ?";
        
        try {
            pst = con.prepareStatement(sql);
            pst.setInt(1, idUsuario);
            rs = pst.executeQuery();
            
            if (rs.next()) {
                String passBD = rs.getString("pass");
                // Comparamos lo que ingresó el usuario con lo que hay en la BD
                return passBD.equals(passwordActual);
            }
        } catch (SQLException e) {
            System.err.println("Error en verificarContrasenaActual: " + e);
        } finally {
            cerrarRecursos(rs, pst, con);
        }
        return false;
    }
}
