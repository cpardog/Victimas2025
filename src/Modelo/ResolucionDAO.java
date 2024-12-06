package Modelo;

import java.sql.*;
import java.util.ArrayList;

public class ResolucionDAO {

    private static final String SQL_SELECT = "SELECT * FROM resoluciones";
    private static final String SQL_INSERT = "INSERT INTO resoluciones (num_resolucion,cedula_principal,nom_principal,ape_principal,fecha_resolucion,fam_beneficiarias, monto_resolucion, emitida_por,fecha_registro) VALUES(?,?,?,?,?,?,?,?,?)";
    private static final String SQL_UPDATE = "UPDATE resoluciones SET num_resolucion=?, cedula_principal=?, nom_principal=?,ape_principal=?,fecha_resolucion=?, fam_beneficiarias=?, monto_resolucion=?, emitida_por=?, fecha_registro=? WHERE id_resolucion=?";
    private static final String SQL_DELETE = "DELETE FROM resoluciones WHERE id_resolucion=?";
    private static final String SQL_BUSCAR1 = "SELECT * FROM resoluciones WHERE cedula_principal LIKE  CONCAT('%',?,'%')";
    private static final String SQL_BUSCAR2 = "SELECT * FROM resoluciones WHERE num_resolucion LIKE  CONCAT('%',?,'%')";
    private static final String SQL_POR_FECHA = "SELECT fecha_resolucion, count(*) as cantidad, month(fecha_resolucion) as mes, sum(monto_resolucion) as Total, sum(fam_beneficiarias) as TotalBenef from resoluciones group by fecha_resolucion  order by fecha_resolucion";
    private static final String SQL_POR_MES = "SELECT month(fecha_resolucion) as mes, year(fecha_resolucion) as vigencia,  count(*)  as cantidad, sum(monto_resolucion) as Total from resoluciones  group by mes,vigencia order by vigencia,mes";
    private static final String SQL_SUMFB = "SELECT SUM(fam_beneficiarias) FROM resoluciones";

    /**
     * Procedimiento para obtener listado de todos los registros de la base de
     * datos
     *
     * @return un ArrayList de todos los elementos peristentes de la Base de
     * Datos
     */
    public ArrayList<Resolucion> select() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Resolucion resolucion = null;
        ArrayList<Resolucion> resoluciones = new ArrayList<>();

        try {
            conn = Conexion.getConexion();
            stmt = conn.prepareStatement(SQL_SELECT);
            rs = stmt.executeQuery();
            while (rs.next()) {
                //Identificacion de registro a nivel de base de datos
                // int id_res
                //Datos de Resolucion
                int id_res = rs.getInt("id_resolucion");
                String num_resolucion = rs.getString("num_resolucion");
                String cedula_principal = rs.getString("cedula_principal");
                String nom_principal = rs.getString("nom_principal");
                String ape_principal = rs.getString("ape_principal");
                java.sql.Date f_resol = rs.getDate(String.valueOf("fecha_resolucion"));
                int f_beneficiarias = rs.getInt("fam_beneficiarias");
                Double monto = rs.getDouble("monto_resolucion");
                String emitida_por = rs.getString("emitida_por");
                java.sql.Date f_registro = rs.getDate(String.valueOf("fecha_registro"));
                // Las asigna al Objeto
                resolucion = new Resolucion();
                resolucion.setId_resolucion(id_res);
                resolucion.setNum_resolucion(num_resolucion);
                resolucion.setCedula_principal(cedula_principal);
                resolucion.setNom_principal(nom_principal);
                resolucion.setApe_principal(ape_principal);
                resolucion.setFecha_resolucion(f_resol);
                resolucion.setFam_beneficiarias(f_beneficiarias);
                resolucion.setMonto_resolucion(monto);
                resolucion.setEmitida_por(emitida_por);
                resolucion.setFecha_registro(f_registro);
                resoluciones.add(resolucion);
            }

        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(rs);
            Conexion.close(stmt);
            Conexion.close(conn);
        }

        return resoluciones;
    }

    /**
     * Procedimiento para obtener los registros agrupados por fecha
     *
     * @return un ArrayList
     */
    public ArrayList<Resumen> selectPorFecha() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Resolucion resolucion = null;
        ArrayList<Resumen> resumenes = new ArrayList<>();

        try {
            conn = Conexion.getConexion();
            stmt = conn.prepareStatement(SQL_POR_FECHA);
            rs = stmt.executeQuery();
            while (rs.next()) {
                String f_resol = rs.getString(String.valueOf("fecha_resolucion"));
                String cantidad = rs.getString("cantidad");
                String mes = rs.getString("mes");
                String total_monto = rs.getString("Total");
                String total_Benef = rs.getString("TotalBenef");
                Resumen resumen = new Resumen();
                resumen.setCampo1(f_resol);
                resumen.setCampo2(cantidad);
                resumen.setCampo3(mes);
                resumen.setCampo4(total_monto);
                resumen.setCampo5(total_Benef);
                resumenes.add(resumen);
            }

        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(rs);
            Conexion.close(stmt);
            Conexion.close(conn);
        }

        return resumenes;
    }

    /**
     * Procedimiento para obtener los registros agrupados por Mes
     *
     * @return un ArrayList
     */
    public ArrayList<Resumen> selectPorMes() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Resolucion resolucion = null;
        ArrayList<Resumen> resumenes = new ArrayList<>();

        try {
            conn = Conexion.getConexion();
            stmt = conn.prepareStatement(SQL_POR_MES);
            rs = stmt.executeQuery();
            while (rs.next()) {
                String fecha = rs.getString(String.valueOf("mes"));
                String vigencia = rs.getString("vigencia");
                String cantidad = rs.getString("cantidad");
                String total_monto = rs.getString("Total");
                Resumen resumen = new Resumen();
                resumen.setCampo1(fecha);
                resumen.setCampo2(vigencia);
                resumen.setCampo3(cantidad);
                resumen.setCampo4(total_monto);
                resumenes.add(resumen);
            }
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(rs);
            Conexion.close(stmt);
            Conexion.close(conn);
        }

        return resumenes;
    }

    /**
     * Procedimiento miembro para insertar una resolución en la base de datos
     *
     * @param resolucion
     * @return Devuelve un número de los registros afectados segun el motor de
     * la base de datos
     */
    public int insert(Resolucion resolucion) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;
        try {
            conn = Conexion.getConexion();
            stmt = conn.prepareStatement(SQL_INSERT);

            //Datos de la resolucion
            stmt.setString(1, resolucion.getNum_resolucion());
            stmt.setString(2, resolucion.getCedula_principal());
            stmt.setString(3, resolucion.getNom_principal());
            stmt.setString(4, resolucion.getApe_principal());
            stmt.setDate(5, (java.sql.Date) resolucion.getFecha_resolucion());
            stmt.setInt(6, resolucion.getFam_beneficiarias());
            stmt.setDouble(7, (Double) resolucion.getMonto_resolucion());
            stmt.setString(8, resolucion.getEmitida_por());
            stmt.setDate(9, (java.sql.Date) resolucion.getFecha_registro());
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
     * Procedimiento miembro para Actualizar una resolución en la base de datos
     *
     * @param resol
     * @return Devuelve un número de los registros afectados segun el motor de
     * la base de datos
     */
    public int update(Resolucion resol) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;

        try {
            conn = Conexion.getConexion();
            System.out.println("ejecutando query: " + SQL_UPDATE);
            stmt = conn.prepareStatement(SQL_UPDATE);

            //Datos de la resolución
            stmt.setString(1, resol.getNum_resolucion());
            stmt.setString(2, resol.getCedula_principal());
            stmt.setString(3, resol.getNom_principal());
            stmt.setString(4, resol.getApe_principal());
            stmt.setDate(5, (java.sql.Date) resol.getFecha_resolucion());
            stmt.setInt(6, resol.getFam_beneficiarias());
            stmt.setDouble(7, (Double) resol.getMonto_resolucion());
            stmt.setString(8, resol.getEmitida_por());
            stmt.setDate(9, (java.sql.Date) resol.getFecha_registro());
            stmt.setInt(10, resol.getId_resolucion());
            rows = stmt.executeUpdate();
            //System.out.println("Registro actualizado:" + rows);

        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(stmt);
            Conexion.close(conn);
        }
        return rows;
    }

    /**
     * Procedimiento miembro para eliminar una resolución en la base de datos
     *
     * @param resolucion
     * @return Devuelve un número de los registros afectados segun el motor de
     * la base de datos
     */
    public int delete(Resolucion resolucion) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;

        try {
            conn = Conexion.getConexion();
            System.out.println("Ejecutando query:" + SQL_DELETE);
            stmt = conn.prepareStatement(SQL_DELETE);
            stmt.setInt(1, resolucion.getId_resolucion());
            rows = stmt.executeUpdate();
            System.out.println("Registro eliminado:" + rows);
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(stmt);
            Conexion.close(conn);
        }
        return rows;
    }

    /**
     * Proceimiento para devolver una busqueda basada en cedula
     *
     * @param cadenabuscar __ La CC a Buscar
     * @return Un ArrayList
     */
    public ArrayList<Resolucion> buscarPorCedulaPrincipal(String cadenabuscar) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Resolucion resolucion = null;
        ArrayList<Resolucion> resoluciones = new ArrayList<>();

        try {
            conn = Conexion.getConexion();
            stmt = conn.prepareStatement(SQL_BUSCAR1);
            stmt.setString(1, cadenabuscar.trim());
            rs = stmt.executeQuery();
            while (rs.next()) {
                //Identificacion de registro a nivel de base de datos
                // int id_res
                //Datos de Resolucion
                int id_res = rs.getInt("id_resolucion");
                String num_resolucion = rs.getString("num_resolucion");
                String cedula_principal = rs.getString("cedula_principal");
                String nom_principal = rs.getString("nom_principal");
                String ape_principal = rs.getString("ape_principal");
                java.sql.Date f_resol = rs.getDate(String.valueOf("fecha_resolucion"));
                int f_beneficiarias = rs.getInt("fam_beneficiarias");
                Double monto = rs.getDouble("monto_resolucion");
                String emitida_por = rs.getString("emitida_por");
                java.sql.Date f_registro = rs.getDate(String.valueOf("fecha_registro"));
                // Creo el objeto y las asigna al Objeto
                resolucion = new Resolucion();
                resolucion.setId_resolucion(id_res);
                resolucion.setNum_resolucion(num_resolucion);
                resolucion.setCedula_principal(cedula_principal);
                resolucion.setNom_principal(nom_principal);
                resolucion.setApe_principal(ape_principal);
                resolucion.setFecha_resolucion(f_resol);
                resolucion.setFam_beneficiarias(f_beneficiarias);
                resolucion.setMonto_resolucion(monto);
                resolucion.setEmitida_por(emitida_por);
                resolucion.setFecha_registro(f_registro);
                resoluciones.add(resolucion);
            }

        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(rs);
            Conexion.close(stmt);
            Conexion.close(conn);
        }
        return resoluciones;
    }

    /**
     * Proceimiento para devolver una busqueda basada en Número de Resolución
     *
     * @param cadenabuscar __ La Resolución a Buscar
     * @return Un ArrayList
     */
    public ArrayList<Resolucion> buscarPorNumeroResolucion(String cadenabuscar) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Resolucion resolucion = null;
        ArrayList<Resolucion> resoluciones = new ArrayList<>();

        try {
            conn = Conexion.getConexion();
            stmt = conn.prepareStatement(SQL_BUSCAR2);
            stmt.setString(1, cadenabuscar.trim());
            rs = stmt.executeQuery();
            while (rs.next()) {
                //Identificacion de registro a nivel de base de datos
                // int id_res
                //Datos de Resolucion
                int id_res = rs.getInt("id_resolucion");
                String num_resolucion = rs.getString("num_resolucion");
                String cedula_principal = rs.getString("cedula_principal");
                String nom_principal = rs.getString("nom_principal");
                String ape_principal = rs.getString("ape_principal");
                java.sql.Date f_resol = rs.getDate(String.valueOf("fecha_resolucion"));
                int f_beneficiarias = rs.getInt("fam_beneficiarias");
                Double monto = rs.getDouble("monto_resolucion");
                String emitida_por = rs.getString("emitida_por");
                java.sql.Date f_registro = rs.getDate(String.valueOf("fecha_registro"));
                // Creo el objeto y las asigna al Objeto
                resolucion = new Resolucion();
                resolucion.setId_resolucion(id_res);
                resolucion.setNum_resolucion(num_resolucion);
                resolucion.setCedula_principal(cedula_principal);
                resolucion.setNom_principal(nom_principal);
                resolucion.setApe_principal(ape_principal);
                resolucion.setFecha_resolucion(f_resol);
                resolucion.setFam_beneficiarias(f_beneficiarias);
                resolucion.setMonto_resolucion(monto);
                resolucion.setEmitida_por(emitida_por);
                resolucion.setFecha_registro(f_registro);
                resoluciones.add(resolucion);
            }

        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(rs);
            Conexion.close(stmt);
            Conexion.close(conn);
        }
        return resoluciones;
    }

    /**
     * Obtengo total de la tabla Beneficiarios
     *
     * @return un Entero con el escalar buscado
     */
    public int selectcantbene() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Beneficiario bene = null;
        int qres = 0;
        try {
            conn = Conexion.getConexion();
            ps = conn.prepareStatement(SQL_SUMFB);
            rs = ps.executeQuery();
            rs.next();
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
}
