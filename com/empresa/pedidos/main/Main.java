package com.empresa.pedidos.main;

import com.empresa.pedidos.modelo.*;

import java.util.Date;

public class Main {
    public static void main(String[] args) {
        // Crear cliente
        Cliente cliente = new Cliente("C01", "Juan", "Calle 123", 300000, 1000000, 10);

        // Crear artículos
        Articulo teclado = new Articulo("A01", "Teclado Mecánico", "Teclado retroiluminado");
        Articulo mouse = new Articulo("A02", "Mouse Gamer", "Mouse con sensor óptico");

        // Crear plantas
        Planta plantaNorte = new Planta("Planta Norte");
        Planta plantaSur = new Planta("Planta Sur");

        // Crear stock en planta
        StockPlanta stock1 = new StockPlanta(teclado, plantaNorte, 10, 3);
        StockPlanta stock2 = new StockPlanta(mouse, plantaSur, 7, 2);

        // Crear pedido
        Pedido pedido = new Pedido(cliente, cliente.getDireccionEnvio(), new Date());

        // Agregar detalles de pedido
        pedido.agregarDetalle(new DetallePedido(teclado, 3, 1));
        pedido.agregarDetalle(new DetallePedido(mouse, 1, 0));

        // Mostrar resumen del pedido
        System.out.println("=== Pedido ===");
        System.out.println("Cliente: " + cliente.getNombre());
        System.out.println("Dirección de envío: " + pedido.getDireccionEnvio());
        System.out.println("Fecha: " + pedido.getFecha());

        System.out.println("\nArtículos solicitados:");
        for (DetallePedido detalle : pedido.getDetalles()) {
            System.out.println("- " + detalle.getArticulo().getNombre()
                    + " | Cantidad: " + detalle.getCantidad()
                    + " | Pendiente: " + detalle.getCantidadPendiente());
        }

        System.out.println("\nInformación de stock:");
        System.out.println(teclado.getNombre() + " - " + stock1.getPlanta().getNombre() +
                ": " + stock1.getCantidadExistente() + " en existencia, mínimo " + stock1.getStockMinimo());

        System.out.println(mouse.getNombre() + " - " + stock2.getPlanta().getNombre() +
                ": " + stock2.getCantidadExistente() + " en existencia, mínimo " + stock2.getStockMinimo());
    }
}
