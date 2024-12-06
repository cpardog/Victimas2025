package Modelo;

import java.sql.*;
import java.sql.Connection;
import java.sql.ResultSet;

/**
 *
 * @author Admin
 */
public class Conexion {

    //public static final String URL = "jdbc:mysql://192.168.80.150:3306/pruebajava";

    public static final String URL = "jdbc:mysql://192.168.20.24:3306/pruebajava";

    public static final String USER = "root";

    //public static final String CLAVE = "Resoluciones2024.";
    
    public static final String CLAVE = "12345";

    public Conexion() {
    }

    public static Connection getConexion() throws ClassNotFoundException {

        Connection cnx = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            cnx = DriverManager.getConnection(URL, USER, CLAVE);
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
         catch (NullPointerException e){
             e.printStackTrace(System.out);
        }
        return cnx;
    }

    /**
     *
     * @param rs
     */
    public static void close(ResultSet rs) {
        try {
            rs.close();
        } catch (SQLException | NullPointerException  e) {
            e.printStackTrace(System.out);
        }
    }

    /**
     *
     * @param cnx
     */
    public static void close(Connection cnx) {
        try {
            cnx.close();
        } catch (SQLException | NullPointerException e) {
            e.printStackTrace(System.out);
        }
    }

    public static void close(PreparedStatement ps) {
        try {
            ps.close();
        } catch (SQLException | NullPointerException e) {
            e.printStackTrace(System.out);
        }
    }
}
