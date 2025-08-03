package com.empresa.pedidos.vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.empresa.pedidos.modelo.*;
import com.empresa.pedidos.controlador.*;
import java.util.*;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class PanelCliente extends JPanel {
    private GestorStockPlanta gestorStock;
    private ControladorPedido controladorPedido;
    private Map<String, Cliente> clientes;
    private List<Articulo> articulos;

    private JComboBox<String> comboClientes;
    private JTable tablaArticulos;
    private DefaultTableModel modeloTabla;
    private JTextArea areaPedido;
    private Pedido pedidoActual;

    public PanelCliente(GestorStockPlanta gestorStock, ControladorPedido controladorPedido,
                       Map<String, Cliente> clientes, List<Articulo> articulos) {
        this.gestorStock = gestorStock;
        this.controladorPedido = controladorPedido;
        this.clientes = clientes;
        this.articulos = articulos;

        initComponentes();
        cargarDatos();
    }

    private void initComponentes() {
        setLayout(new BorderLayout());

        // Panel superior con selección de cliente
        JPanel panelSuperior = new JPanel(new FlowLayout());
        JLabel lblCliente = new JLabel("Cliente:");
        comboClientes = new JComboBox<>();
        if (clientes != null) {
            for (Cliente cliente : clientes.values()) {
                comboClientes.addItem(cliente.getCodigo() + " - " + cliente.getNombre());
            }
        }

        // Agregar etiqueta de saldo
        JLabel lblSaldo = new JLabel("Saldo: $0.00");
        lblSaldo.setFont(new Font("Arial", Font.BOLD, 12));

        panelSuperior.add(lblCliente);
        panelSuperior.add(comboClientes);
        panelSuperior.add(Box.createHorizontalStrut(20));
        panelSuperior.add(lblSaldo);

        // Actualizar saldo cuando cambie el cliente
        comboClientes.addActionListener(e -> {
            if (comboClientes.getSelectedItem() != null) {
                String clienteSeleccionado = comboClientes.getSelectedItem().toString();
                String codigoCliente = clienteSeleccionado.split(" - ")[0];
                Cliente cliente = clientes.get(codigoCliente);
                if (cliente != null) {
                    lblSaldo.setText("Saldo: $" + String.format("%.2f", cliente.getSaldo()) +
                                   " (Descuento: " + cliente.getPorcentajeDescuento() + "%)");
                }
            }
        });

        add(panelSuperior, BorderLayout.NORTH);

        // Panel central con tabla de artículos
        JPanel panelCentral = new JPanel(new BorderLayout());
        String[] columnas = {"Código", "Nombre", "Descripción", "Precio", "Stock Total", "Cantidad"};

        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // Solo columna cantidad editable
            }
        };

        tablaArticulos = new JTable(modeloTabla);
        JScrollPane scroll = new JScrollPane(tablaArticulos);
        panelCentral.add(scroll, BorderLayout.CENTER);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        JButton btnCrearPedido = new JButton("Crear Nuevo Pedido");
        JButton btnAgregar = new JButton("Agregar al Pedido");
        JButton btnFinalizarPedido = new JButton("Finalizar Pedido");
        JButton btnActualizar = new JButton("Actualizar Stock");

        panelBotones.add(btnCrearPedido);
        panelBotones.add(btnAgregar);
        panelBotones.add(btnFinalizarPedido);
        panelBotones.add(btnActualizar);
        panelCentral.add(panelBotones, BorderLayout.SOUTH);

        add(panelCentral, BorderLayout.CENTER);

        // Panel inferior para mostrar pedido actual
        areaPedido = new JTextArea(6, 40);
        areaPedido.setEditable(false);
        areaPedido.setBorder(BorderFactory.createTitledBorder("Pedido Actual"));
        add(new JScrollPane(areaPedido), BorderLayout.SOUTH);

        // Event listeners
        btnCrearPedido.addActionListener(this::crearNuevoPedido);
        btnAgregar.addActionListener(this::agregarArticuloAPedido);
        btnFinalizarPedido.addActionListener(this::finalizarPedido);
        btnActualizar.addActionListener(e -> actualizarTablaStock());
    }

    private void cargarDatos() {
        // Cargar clientes
        comboClientes.removeAllItems();
        if (clientes != null) {
            for (Cliente cliente : clientes.values()) {
                comboClientes.addItem(cliente.getCodigo() + " - " + cliente.getNombre());
            }
        }

        // Cargar artículos
        actualizarTablaStock();
    }

    private void actualizarTablaStock() {
        modeloTabla.setRowCount(0);
        if (articulos != null && gestorStock != null) {
            for (Articulo articulo : articulos) {
                int stockTotal = gestorStock.buscarPorArticulo(articulo).stream()
                    .mapToInt(StockPlanta::getCantidadExistente).sum();
                modeloTabla.addRow(new Object[]{
                    articulo.getCodigo(),
                    articulo.getNombre(),
                    articulo.getDescripcion(),
                    "$" + articulo.getPrecio(),
                    stockTotal,
                    1 // cantidad por defecto
                });
            }
        }
    }

    private void crearNuevoPedido(ActionEvent e) {
        if (comboClientes.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente primero");
            return;
        }

        String clienteSeleccionado = comboClientes.getSelectedItem().toString();
        String codigoCliente = clienteSeleccionado.split(" - ")[0];
        Cliente cliente = clientes.get(codigoCliente);

        if (cliente != null) {
            pedidoActual = controladorPedido.crearPedido(cliente, new Date());
            areaPedido.setText("Nuevo pedido creado:\n");
            areaPedido.append("ID Pedido: " + pedidoActual.getIdPedido() + "\n");
            areaPedido.append("Cliente: " + cliente.getNombre() + "\n");
            areaPedido.append("Dirección: " + cliente.getDireccionEnvio() + "\n\n");
            areaPedido.append("Artículos:\n");
        }
    }

    private void agregarArticuloAPedido(ActionEvent e) {
        if (pedidoActual == null) {
            JOptionPane.showMessageDialog(this, "Debe crear un pedido primero");
            return;
        }

        int fila = tablaArticulos.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un artículo de la tabla");
            return;
        }

        String codigoArticulo = tablaArticulos.getValueAt(fila, 0).toString();
        int cantidad = Integer.parseInt(tablaArticulos.getValueAt(fila, 5).toString());

        // Buscar el artículo
        Articulo articulo = articulos.stream()
            .filter(a -> a.getCodigo().equals(codigoArticulo))
            .findFirst().orElse(null);

        if (articulo != null) {
            // Calcular precio con descuento para mostrar
            Cliente cliente = pedidoActual.getCliente();
            double precioOriginal = articulo.getPrecio() * cantidad;
            double descuento = cliente.getPorcentajeDescuento() / 100.0;
            double precioConDescuento = precioOriginal * (1 - descuento);

            boolean agregado = controladorPedido.agregarDetalleAPedido(
                pedidoActual.getIdPedido(), articulo, cantidad);

            if (agregado) {
                areaPedido.append(String.format("- %s x%d: $%.2f (desc. %.0f%%) = $%.2f\n",
                    articulo.getNombre(), cantidad, precioOriginal,
                    cliente.getPorcentajeDescuento(), precioConDescuento));

                // Actualizar stock en tabla y saldo del cliente
                actualizarTablaStock();

                // Actualizar etiqueta de saldo
                Component[] components = ((JPanel) getComponent(0)).getComponents();
                for (Component comp : components) {
                    if (comp instanceof JLabel && ((JLabel) comp).getText().startsWith("Saldo:")) {
                        ((JLabel) comp).setText("Saldo: $" + String.format("%.2f", cliente.getSaldo()) +
                                              " (Descuento: " + cliente.getPorcentajeDescuento() + "%)");
                        break;
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this,
                    "Error: Stock insuficiente o saldo insuficiente\n" +
                    "Costo: $" + String.format("%.2f", precioConDescuento) +
                    "\nSaldo disponible: $" + String.format("%.2f", cliente.getSaldo()));
            }
        }
    }

    private void finalizarPedido(ActionEvent e) {
        if (pedidoActual != null) {
            double total = controladorPedido.calcularTotalPedido(pedidoActual);
            areaPedido.append("\n=== PEDIDO FINALIZADO ===\n");
            areaPedido.append("Total con descuentos: $" + String.format("%.2f", total) + "\n");
            pedidoActual = null;
        }
    }

    public void refrescarDatos() {
        cargarDatos();
    }
}
