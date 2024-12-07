/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utilidades;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 *
 * @author Admin
 */
public class Funciones {

    public static java.sql.Date convertirDateASql(Date fecha) {
        long d = fecha.getTime();
        java.sql.Date ffinal = new java.sql.Date(d);
        return ffinal;
    }

    public static java.sql.Date cadenaAfecha(String cadena) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate ff = LocalDate.parse(cadena, dtf);
        java.sql.Date res = java.sql.Date.valueOf(ff);
        return res;
    }

    public static String fechaACadena(Date fecha) {
        long d = fecha.getTime();
        java.sql.Date ffinal = new java.sql.Date(d);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String unafecha = sdf.format(ffinal);
        return unafecha;
    }

}
