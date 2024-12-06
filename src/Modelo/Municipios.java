/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author Carlos Pardo
 */
public class Municipios {
    private Integer codigo_Departamento;
    private String nombre_Municipio;

    public Municipios() {
    }

    public Municipios(Integer codigo_Departamento, String nombre_Municipio) {
        this.codigo_Departamento = codigo_Departamento;
        this.nombre_Municipio = nombre_Municipio;
    }

    public String getNombre_Municipio() {
        return nombre_Municipio;
    }

    public void setNombre_Municipio(String nombre_Municipio) {
        this.nombre_Municipio = nombre_Municipio;
    }

    public Integer getCodigo_Departamento() {
        return codigo_Departamento;
    }

    public void setCodigo_Departamento(Integer codigo_Departamento) {
        this.codigo_Departamento = codigo_Departamento;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Municipios{");
        sb.append("codigo_Departamento=").append(codigo_Departamento);
        sb.append(", nombre_Municipio=").append(nombre_Municipio);
        sb.append('}');
        return sb.toString();
    }
    
    
}
