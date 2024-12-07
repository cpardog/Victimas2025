/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.Beneficiario;
import Modelo.Conexion;
import Utilidades.CrudBasico;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Admin
 */
public class BeneficiarioImpl implements CrudBasico {

    private static final String SQL_INSERT = "INSERT INTO beneficiarios (resolucion_id,id_beneficiario,nombre_beneficiario,apellido_beneficiario) VALUES(?,?,?,?)";

    @Override
    public int insert(Beneficiario bene) {
        Connection conn = null;
        PreparedStatement ps = null;
        int rows = 0;
        try {
            conn = Conexion.getConexion();
            ps = conn.prepareStatement(SQL_INSERT);

            //Datos de la resolucion
            ps.setInt(1, bene.getResolucion_id());
            ps.setString(2, bene.getId_beneficiario());
            ps.setString(3, bene.getNombre_beneficiario());
            ps.setString(4, bene.getApellido_beneficiario());

            System.out.println("ejecutando query:" + SQL_INSERT);
            rows = ps.executeUpdate();
            System.out.println("Registros afectados:" + rows);
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(ps);
            Conexion.close(conn);
        }
        return rows;
    }

    @Override
    public int update(Beneficiario bene, String beneanterior) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int delete(Beneficiario bene) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int selectcantbene(int numres) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public ArrayList<Beneficiario> selectbene(int num_res) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public ArrayList<Beneficiario> select() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
