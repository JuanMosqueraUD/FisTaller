package com.empresa.pedidos.modelo;

public class Articulo {
    private String codigo;
    private String nombre;
    private String descripcion;

    public Articulo(String codigo, String nombre, String descripcion) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    // Getters
    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }

    public void setCodigo(String codigo) { this.codigo = codigo;}
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

}
