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

        // Calcular el costo total con descuento
        double precioUnitario = articulo.getPrecio();
        double descuento = pedido.getCliente().getPorcentajeDescuento() / 100.0;
        double precioConDescuento = precioUnitario * (1 - descuento);
        double costoTotal = precioConDescuento * cantidad;

        // Verificar si el cliente tiene saldo suficiente
        if (pedido.getCliente().getSaldo() < costoTotal) {
            System.out.println("Saldo insuficiente. Costo: $" + costoTotal +
                             ", Saldo disponible: $" + pedido.getCliente().getSaldo());
            return false;
        }

        // Buscar UNA planta que tenga suficiente stock
        List<StockPlanta> disponibles = gestorStock.buscarPorArticulo(articulo);
        for (StockPlanta sp : disponibles) {
            if (sp.getCantidadExistente() >= cantidad) {
                // Descontar del stock usando el método directo
                sp.descontarStock(cantidad);

                // Descontar del saldo del cliente
                pedido.getCliente().setSaldo(pedido.getCliente().getSaldo() - costoTotal);

                DetallePedido detalle = new DetallePedido(articulo, cantidad, cantidad);
                pedido.agregarDetalle(detalle);

                System.out.println("Compra realizada. Descuento aplicado: " +
                                 (pedido.getCliente().getPorcentajeDescuento()) + "%");
                System.out.println("Costo total: $" + costoTotal +
                                 " (precio original: $" + (precioUnitario * cantidad) + ")");
                return true;
            }
        }

        System.out.println("Stock insuficiente para el artículo: " + articulo.getNombre());
        return false;
    }

    // Nuevo método para calcular el total de un pedido con descuentos
    public double calcularTotalPedido(Pedido pedido) {
        double total = 0;
        double descuento = pedido.getCliente().getPorcentajeDescuento() / 100.0;

        for (DetallePedido detalle : pedido.getDetalles()) {
            double precio = detalle.getArticulo().getPrecio();
            double precioConDescuento = precio * (1 - descuento);
            total += precioConDescuento * detalle.getCantidad();
        }
        return total;
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }
}
