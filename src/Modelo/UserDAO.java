package Modelo;

import java.sql.*;
import java.util.ArrayList;

public class UserDAO {

    private static final String SQL_SELECT = "SELECT * FROM users";
    private static final String SQL_INSERT = "INSERT INTO users (login,clave,correo,estado,rolnombre) VALUES(?,?,?,?,?)";
    private static final String SQL_UPDATE = "UPDATE users SET login=?,clave=?,correo=?,estado=?,rolnombre=? WHERE id=?";
    private static final String SQL_DELETE = "DELETE FROM users WHERE id=?";
    private static final String SQL_CANT_USER = "SELECT COUNT(*)  FROM users WHERE login=? AND clave=?";
    private static final String SQL_SELECT_USER = "SELECT rolnombre FROM users WHERE login=? AND clave=?";

    public ArrayList<User> select() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        User usuario = null;
        ArrayList<User> usuarios;
        usuarios = new ArrayList<>();

        try {
            conn = Conexion.getConexion();
            stmt = conn.prepareStatement(SQL_SELECT);
            rs = stmt.executeQuery();
            while (rs.next()) {
                //Identificacion de registro a nivel de base de datos
                int id = rs.getInt("id");

                //Datos personales
                //String nombre = rs.getString("nombre");
                String login = rs.getString("login");
                String clave = rs.getString("clave");
                String correo = rs.getString("correo");
                String estado = rs.getString("estado");
                String nombrerol = rs.getString("rolnombre");
                usuario = new User();
                usuario.setId_usuario(id);
                usuario.setLogin(login);
                usuario.setClave(clave);
                usuario.setEmail(correo);
                usuario.setActivo(estado);
                usuario.setRolNombre(nombrerol);
                usuarios.add(usuario);
            }

        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(rs);
            Conexion.close(stmt);
            Conexion.close(conn);
        }

        return usuarios;
    }

    /**
     * Procedimiento para Insertar Usuarios en la Base de datos
     *
     * @param usuario
     * @return Parámetro si loogró insertarse con exito en la DB
     */
    public int insert(User usuario) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;
        try {
            conn = Conexion.getConexion();
            stmt = conn.prepareStatement(SQL_INSERT);

            //Datos personales
            stmt.setString(1, usuario.getLogin());
            stmt.setString(2, usuario.getClave());
            stmt.setString(3, usuario.getEmail());
            stmt.setString(4, usuario.getActivo());
            stmt.setString(5, usuario.getRolNombre());
            
            // Se  deben setear el resto de parametros
            System.out.println("ejecutando query:" + SQL_INSERT);
            rows = stmt.executeUpdate();
            System.out.println("Registros afectados:" + rows);
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(stmt);
            Conexion.close(conn);
        }
        return rows;
    }

    /**
     * Procedimiento para Actualizar Usuario en la Base de datos
     *
     * @param usuario
     * @return Parámetro si logró actualizar con exito en la DB
     */
    public int update(User usuario) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;

        try {
            conn = Conexion.getConexion();
            System.out.println("ejecutando query: " + SQL_UPDATE);
            stmt = conn.prepareStatement(SQL_UPDATE);

            //Datos personales
            stmt.setString(1, usuario.getLogin());
            stmt.setString(2, usuario.getClave());
            stmt.setString(3, usuario.getEmail());
            stmt.setString(4, usuario.getActivo());
            stmt.setString(5, usuario.getRolNombre());
            stmt.setInt(6, usuario.getId_usuario());

            rows = stmt.executeUpdate();
            System.out.println("Registro actualizado:" + rows);

        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(stmt);
            Conexion.close(conn);
        }
        return rows;
    }

    /**
     * Procedimiento para eliminar usuario de la DB
     *
     * @param usuario
     * @return
     */
    public int delete(User usr) {
        Connection conn = null;
        PreparedStatement ps = null;
        int rows = 0;

        try {
            conn = Conexion.getConexion();
            System.out.println("Ejecutando query:" + SQL_DELETE);
            ps = conn.prepareStatement(SQL_DELETE);
            ps.setInt(1, usr.getId_usuario());
            rows = ps.executeUpdate();
            System.out.println("Registro eliminado:" + rows);
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(ps);
            Conexion.close(conn);
        }
        return rows;
    }

    public int selectcantuser(String user, String clave) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        User usuario = null;
        int qres = 0;
        try {
            conn = Conexion.getConexion();
            ps = conn.prepareStatement(SQL_CANT_USER);
            ps.setString(1, user.trim());
            ps.setString(2, clave.trim());
            rs = ps.executeQuery();
            rs.next();
            //rs.next();
            //Identificacion de registro a nivel de base de datos
            // int id_res
            //Datos de Resolucion
            qres = rs.getInt(1);
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(rs);
            Conexion.close(ps);
            Conexion.close(conn);
        }
        return qres;
    }

    public boolean esUsuarioAdmin(String login, String clave) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        User usuario = null;
        boolean resp = false;
        try {
            conn = Conexion.getConexion();
            ps = conn.prepareStatement(SQL_SELECT_USER);
            ps.setString(1, login.trim());
            ps.setString(2, clave.trim());
            rs = ps.executeQuery();
            rs.next();
            String myrol = "";
            myrol = rs.getString(1).trim();
            if (myrol.equals("admin")) {
                resp = true;
            } else {
                resp = false;
            }

        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(rs);
            Conexion.close(ps);
            Conexion.close(conn);
        }
        return resp;

    }
}
