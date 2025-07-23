package com.empresa.pedidos.modelo;

public class DetallePedido {
    private Articulo articulo;
    private int cantidad;
    private int cantidadPendiente;

    public DetallePedido(Articulo articulo, int cantidad, int cantidadPendiente) {
        this.articulo = articulo;
        this.cantidad = cantidad;
        this.cantidadPendiente = cantidadPendiente;
    }

    // Getters
    public Articulo getArticulo() { return articulo; }
    public int getCantidad() { return cantidad; }
    public int getCantidadPendiente() { return cantidadPendiente; }
}
