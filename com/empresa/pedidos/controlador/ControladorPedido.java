package com.empresa.pedidos.controlador;

import com.empresa.pedidos.modelo.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ControladorPedido {
    private List<Pedido> pedidos = new ArrayList<>();
    private GestorStockPlanta gestorStock;

    public ControladorPedido(GestorStockPlanta gestorStock) {
        this.gestorStock = gestorStock;
    }

    public Pedido crearPedido(Cliente cliente, Date fecha) {
        Pedido pedido = new Pedido(cliente, fecha);
        pedidos.add(pedido);
        return pedido;
    }

    public Pedido buscarPedidoPorId(int id) {
        for (Pedido p : pedidos) {
            if (p.getIdPedido() == id) {
                return p;
            }
        }
        return null;
    }

    public boolean agregarDetalleAPedido(int idPedido, Articulo articulo, int cantidad) {
        Pedido pedido = buscarPedidoPorId(idPedido);
        if (pedido == null) return false;

        // Buscar UNA planta que tenga suficiente stock
        List<StockPlanta> disponibles = gestorStock.buscarPorArticulo(articulo);
        for (StockPlanta sp : disponibles) {
            if (sp.getCantidadExistente() >= cantidad) {
                gestorStock.actualizarStock(articulo, sp.getPlanta(),
                        sp.getCantidadExistente() - cantidad,
                        sp.getStockMinimo());

                DetallePedido detalle = new DetallePedido(articulo, cantidad, cantidad);
                pedido.agregarDetalle(detalle);
                return true;
            }
        }

        System.out.println("Stock insuficiente para el artículo: " + articulo.getNombre());
        return false;
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }
}
