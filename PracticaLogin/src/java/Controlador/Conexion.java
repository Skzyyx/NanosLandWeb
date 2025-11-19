/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */package Controlador;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class Conexion {
 
    private final String USERNAME = "root";
    private final String PASSWORD = "root"; 
    private final String HOST = "localhost";
    private final String PORT = "3306";
    private final String DATABASE = "nanos_land"; 
    private final String CLASSNAME = "com.mysql.cj.jdbc.Driver";
    private final String URL = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE;

    
    public Conexion() {
       
        try {
            Class.forName(CLASSNAME);
        } catch(ClassNotFoundException e) {
            System.err.println("Error en: " + e);
        }
    }
     
    
    public Connection getConexion() {
        Connection con = null;
        try {
            con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch(SQLException e) {
            System.err.println("Error al conectar: " + e);
        }
        return con; 
    }
}