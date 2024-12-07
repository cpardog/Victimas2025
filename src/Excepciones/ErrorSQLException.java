/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Excepciones;

import java.sql.SQLException;

/**
 * Esta es una clase personalizada de SQLException
 * @author Admin
 */
public class ErrorSQLException  extends SQLException{

    public ErrorSQLException(String reason, Throwable cause) {
        super(reason, cause);
    }
    
    
}
