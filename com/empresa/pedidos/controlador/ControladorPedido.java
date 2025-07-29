package com.empresa.pedidos.controlador;

import com.empresa.pedidos.modelo.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ControladorPedido {
    private List<Pedido> pedidos = new ArrayList<>();

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
        if (pedido != null) {
            DetallePedido detalle = new DetallePedido(articulo, cantidad, cantidad);
            pedido.agregarDetalle(detalle);
            return true;
        }
        return false;
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }
}
