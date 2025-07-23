package com.empresa.pedidos.main;

import com.empresa.pedidos.modelo.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Main {
    // Estructura para gestionar múltiples clientes
    private static Map<String, Cliente> clientes = new HashMap<>();
    private static int contadorClientes = 1;
    public static void main(String[] args) {
        
        // Demostración del sistema de gestión automática de clientes
        System.out.println("\n=== Sistema de Gestión Automática de Clientes ===");
        
        // Crear varios clientes automáticamente
        crearCliente("María García", "Avenida 456", 500000, 1500000, 15);
        crearCliente("Pedro López", "Carrera 789", 200000, 800000, 5);
        crearCliente("Ana Rodríguez", "Calle 321", 750000, 2000000, 20);
        
        // Listar todos los clientes
        listarClientes();
        
        // Buscar un cliente específico
        System.out.println("\n=== Búsqueda de Cliente ===");
        Cliente clienteEncontrado = buscarCliente("C002");
        if (clienteEncontrado != null) {
            System.out.println("Cliente encontrado: " + clienteEncontrado.getNombre());
        }
        
        // Actualizar información de un cliente
        System.out.println("\n=== Actualización de Cliente ===");
        actualizarCliente("C003", "Pedro López Martínez", "Nueva Carrera 999");
        
        // Mostrar lista actualizada
        listarClientes();
    }

    // Método para crear cliente con código automático
    public static Cliente crearCliente(String nombre, String direccion, int saldo, int limite, double descuento){
        String codigoAutomatico = "C" + String.format("%03d", contadorClientes);
        Cliente nuevoCliente = new Cliente(codigoAutomatico, nombre, direccion, saldo, limite, descuento);
        clientes.put(codigoAutomatico, nuevoCliente);
        contadorClientes++;
        System.out.println("Cliente creado: " + nuevoCliente.getNombre() + " con código: " + codigoAutomatico);
        return nuevoCliente;
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
    public static boolean actualizarCliente(String codigo, String nuevoNombre, String nuevaDireccion) {
        Cliente cliente = clientes.get(codigo);
        if (cliente != null) {
            if (nuevoNombre != null) cliente.setNombre(nuevoNombre);
            if (nuevaDireccion != null) cliente.setDireccionEnvio(nuevaDireccion);
            System.out.println("Cliente " + codigo + " actualizado correctamente.");
            return true;
        } else {
            System.out.println("Cliente con código " + codigo + " no encontrado.");
            return false;
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
