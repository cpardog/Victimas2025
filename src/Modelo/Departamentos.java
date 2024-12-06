/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author Carlos Pardo
 */
public class Departamentos {

    private Integer codigo_Departamento;
    private String nombre_Departamento;

    public Departamentos() {
    }

    public Departamentos(Integer codigo_Departamento, String nombre_Departamento) {
        this.codigo_Departamento = codigo_Departamento;
        this.nombre_Departamento = nombre_Departamento;
    }

    public Integer getCodigo_Departamento() {
        return codigo_Departamento;
    }

    public void setCodigo_Departamento(Integer codigo_Departamento) {
        this.codigo_Departamento = codigo_Departamento;
    }

    public String getNombre_Departamento() {
        return nombre_Departamento;
    }

    public void setNombre_Departamento(String nombre_Departamento) {
        this.nombre_Departamento = nombre_Departamento;
    }

}
