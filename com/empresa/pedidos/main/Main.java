package com.empresa.pedidos.main;

import com.empresa.pedidos.modelo.*;
import com.empresa.pedidos.controlador.*;
import com.empresa.pedidos.vista.VentanaPrincipal;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;

public class Main {
    // Estructura para gestionar múltiples clientes
    private static Map<String, Cliente> clientes = new HashMap<>();
    private static int contadorClientes = 1;

    public static void main(String[] args) {
        // Crear datos de prueba del backend
        GestorStockPlanta gestor = new GestorStockPlanta();
        ControladorPedido ctrl = new ControladorPedido(gestor);

        // Crear clientes
        crearCliente("Alma Marla Gozo", "Avenida 69", 500000, 1500000, 15);

        // Crear plantas
        List<Planta> plantas = new ArrayList<>();
        Planta planta1 = new Planta("Planta Norte");
        Planta planta2 = new Planta("Planta Sur");
        plantas.add(planta1);
        plantas.add(planta2);

        // Crear artículos
        List<Articulo> articulos = new ArrayList<>();
        Articulo a1 = new Articulo("A001", "Mouse", "Mouse óptico", 50.0);
        Articulo a2 = new Articulo("A002", "Teclado", "Teclado mecánico", 100.0);
        articulos.add(a1);
        articulos.add(a2);

        // Registrar stock inicial
        gestor.registrarStock(a1, planta1, 200, 50);
        gestor.registrarStock(a1, planta2, 100, 30);
        gestor.registrarStock(a2, planta1, 150, 40);

        // Crear pedido de ejemplo para demostrar funcionamiento
        Pedido pedido = ctrl.crearPedido(buscarCliente("C001"), new Date());
        int idPedido = pedido.getIdPedido();
        boolean agregado1 = ctrl.agregarDetalleAPedido(idPedido, a1, 5);  // Mouse
        boolean agregado2 = ctrl.agregarDetalleAPedido(idPedido, a2, 3);  // Teclado

        // Mostrar información en consola
        System.out.println("\n=== Sistema de Gestión de pedidos - Rappi===");
        System.out.println("Pedido #" + idPedido + " creado para " + buscarCliente("C001").getNombre());
        if (agregado1) System.out.println("Artículo A001 agregado exitosamente.");
        if (agregado2) System.out.println("Artículo A002 agregado exitosamente.");

        // Mostrar resumen del pedido
        System.out.println("\nDetalles del pedido:");
        for (DetallePedido d : pedido.getDetalles()) {
            System.out.println("- " + d.getArticulo().getNombre() + " x" + d.getCantidad());
        }

        // Ver stock actualizado
        System.out.println("\nStock actualizado:");
        for (StockPlanta sp : gestor.listarStock()) {
            System.out.println("Artículo: " + sp.getArticulo().getNombre() +
                               ", Planta: " + sp.getPlanta().getNombre() +
                               ", Cantidad: " + sp.getCantidadExistente());
        }

        // Lanzar la GUI con todos los datos del backend
        SwingUtilities.invokeLater(() -> {
            // Inicializar los datos en la ventana principal
            VentanaPrincipal.inicializarDatos(gestor, ctrl, clientes, articulos, plantas);
            VentanaPrincipal ventana = new VentanaPrincipal();
            ventana.setVisible(true);
        });
    }


    // Método para crear cliente con código automático
    public static void crearCliente(String nombre, String direccion, int saldo, int limite, double descuento){
        String codigoAutomatico = "C" + String.format("%03d", contadorClientes);
        Cliente nuevoCliente = new Cliente(codigoAutomatico, nombre, direccion, saldo, limite, descuento);
        clientes.put(codigoAutomatico, nuevoCliente);
        contadorClientes++;
        System.out.println("Cliente creado: " + nuevoCliente.getNombre() + " con cóck-digo: " + codigoAutomatico);
    }
    
    // Método para buscar cliente por código
    public static Cliente buscarCliente(String codigo) {
        return clientes.get(codigo);
    }
    
    // Método para listar todos los clientes
    public static void listarClientes() {
        System.out.println("\n=== Lista de Clientes ===");
        for (Map.Entry<String, Cliente> entry : clientes.entrySet()) {
            Cliente cliente = entry.getValue();
            System.out.println("Código: " + cliente.getCodigo() + 
                             " | Nombre: " + cliente.getNombre() + 
                             " | Saldo: $" + cliente.getSaldo());
        }
    }
    
    // Método para actualizar información de un cliente
    public static void actualizarCliente(String codigo, String nuevoNombre, String nuevaDireccion) {
        Cliente cliente = clientes.get(codigo);
        if (cliente != null) {
            if (nuevoNombre != null) cliente.setNombre(nuevoNombre);
            if (nuevaDireccion != null) cliente.setDireccionEnvio(nuevaDireccion);
            System.out.println("Cliente " + codigo + " actualizado correctamente.");
        } else {
            System.out.println("Cliente con código " + codigo + " no encontrado.");
        }
    }
    
    // Método para eliminar un cliente (opcional)
    public static boolean eliminarCliente(String codigo) {
        Cliente cliente = clientes.remove(codigo);
        if (cliente != null) {
            System.out.println("Cliente " + cliente.getNombre() + " eliminado correctamente.");
            return true;
        } else {
            System.out.println("Cliente con código " + codigo + " no encontrado.");
            return false;
        }
    }



}
