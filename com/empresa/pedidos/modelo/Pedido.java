package com.empresa.pedidos.modelo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Pedido {
    private static int contador = 1;

    private int idPedido;
    private Cliente cliente;
    private String direccionEnvio;
    private Date fecha;
    private List<DetallePedido> detalles;

    public Pedido(Cliente cliente, Date fecha) {
        this.cliente = cliente;
        this.direccionEnvio = cliente.getDireccionEnvio();
        this.fecha = fecha;
        this.detalles = new ArrayList<>();
    }

    public void agregarDetalle(DetallePedido detalle) {
        detalles.add(detalle);
    }

    // Getters
    public int getIdPedido() { return idPedido; }
    public Cliente getCliente() { return cliente; }
    public String getDireccionEnvio() { return direccionEnvio; }
    public Date getFecha() { return fecha; }
    public List<DetallePedido> getDetalles() { return detalles; }
}
