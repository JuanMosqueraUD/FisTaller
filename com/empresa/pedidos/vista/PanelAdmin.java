package com.empresa.pedidos.vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import com.empresa.pedidos.modelo.*;
import com.empresa.pedidos.controlador.*;
import java.util.*;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class PanelAdmin extends JPanel {
    private GestorStockPlanta gestorStock;
    private ControladorPedido controladorPedido;
    private Map<String, Cliente> clientes;
    private List<Articulo> articulos;
    private List<Planta> plantas;

    private JTabbedPane pestañas;
    private static int contadorClientes = 1; // Para generar códigos automáticos

    public PanelAdmin(GestorStockPlanta gestorStock, ControladorPedido controladorPedido,
                     Map<String, Cliente> clientes, List<Articulo> articulos, List<Planta> plantas) {
        this.gestorStock = gestorStock;
        this.controladorPedido = controladorPedido;
        this.clientes = clientes;
        this.articulos = articulos;
        this.plantas = plantas;

        // Inicializar contador basado en clientes existentes
        if (clientes != null && !clientes.isEmpty()) {
            contadorClientes = clientes.size() + 1;
        }

        initComponentes();
    }

    private void initComponentes() {
        setLayout(new BorderLayout());

        JLabel titulo = new JLabel("Panel de Administración");
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        add(titulo, BorderLayout.NORTH);

        pestañas = new JTabbedPane();
        pestañas.addTab("Clientes", crearPanelClientes());
        pestañas.addTab("Artículos", crearPanelArticulos());
        pestañas.addTab("Stock", crearTablaStock());
        pestañas.addTab("Pedidos", crearTablaPedidos());

        add(pestañas, BorderLayout.CENTER);
    }

    private JPanel crearPanelClientes() {
        JPanel panel = new JPanel(new BorderLayout());

        // Tabla de clientes
        String[] colClientes = {"Código", "Nombre", "Dirección", "Saldo", "Límite", "% Desc."};
        DefaultTableModel modeloClientes = new DefaultTableModel(colClientes, 0);

        if (clientes != null) {
            for (Cliente cliente : clientes.values()) {
                modeloClientes.addRow(new Object[]{
                    cliente.getCodigo(),
                    cliente.getNombre(),
                    cliente.getDireccionEnvio(),
                    "$" + cliente.getSaldo(),
                    "$" + cliente.getLimiteCredito(),
                    cliente.getPorcentajeDescuento() + "%"
                });
            }
        }

        JTable tablaClientes = new JTable(modeloClientes);
        panel.add(new JScrollPane(tablaClientes), BorderLayout.CENTER);

        // Panel de botones para clientes
        JPanel panelBotonesClientes = new JPanel(new FlowLayout());
        JButton btnNuevoCliente = new JButton("Nuevo Cliente");
        JButton btnEditarCliente = new JButton("Editar Cliente");
        JButton btnEliminarCliente = new JButton("Eliminar Cliente");

        panelBotonesClientes.add(btnNuevoCliente);
        panelBotonesClientes.add(btnEditarCliente);
        panelBotonesClientes.add(btnEliminarCliente);
        panel.add(panelBotonesClientes, BorderLayout.SOUTH);

        // Event listeners para clientes
        btnNuevoCliente.addActionListener(e -> crearNuevoCliente(modeloClientes));
        btnEditarCliente.addActionListener(e -> editarCliente(tablaClientes, modeloClientes));
        btnEliminarCliente.addActionListener(e -> eliminarCliente(tablaClientes, modeloClientes));

        return panel;
    }

    private JPanel crearPanelArticulos() {
        JPanel panel = new JPanel(new BorderLayout());

        // Tabla de artículos
        String[] colArticulos = {"Código", "Nombre", "Descripción", "Precio"};
        DefaultTableModel modeloArticulos = new DefaultTableModel(colArticulos, 0);

        if (articulos != null) {
            for (Articulo articulo : articulos) {
                modeloArticulos.addRow(new Object[]{
                    articulo.getCodigo(),
                    articulo.getNombre(),
                    articulo.getDescripcion(),
                    "$" + articulo.getPrecio()
                });
            }
        }

        JTable tablaArticulos = new JTable(modeloArticulos);
        panel.add(new JScrollPane(tablaArticulos), BorderLayout.CENTER);

        // Panel de botones para artículos
        JPanel panelBotonesArticulos = new JPanel(new FlowLayout());
        JButton btnNuevoArticulo = new JButton("Nuevo Artículo");
        JButton btnEditarArticulo = new JButton("Editar Artículo");
        JButton btnEliminarArticulo = new JButton("Eliminar Artículo");

        panelBotonesArticulos.add(btnNuevoArticulo);
        panelBotonesArticulos.add(btnEditarArticulo);
        panelBotonesArticulos.add(btnEliminarArticulo);
        panel.add(panelBotonesArticulos, BorderLayout.SOUTH);

        // Event listeners para artículos
        btnNuevoArticulo.addActionListener(e -> crearNuevoArticulo(modeloArticulos));
        btnEditarArticulo.addActionListener(e -> editarArticulo(tablaArticulos, modeloArticulos));
        btnEliminarArticulo.addActionListener(e -> eliminarArticulo(tablaArticulos, modeloArticulos));

        return panel;
    }

    // Métodos para gestión de clientes
    private void crearNuevoCliente(DefaultTableModel modelo) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Nuevo Cliente", true);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Campos del formulario
        JTextField txtNombre = new JTextField(20);
        JTextField txtDireccion = new JTextField(20);
        JTextField txtSaldo = new JTextField(20);
        JTextField txtLimite = new JTextField(20);
        JTextField txtDescuento = new JTextField(20);

        // Agregar campos al dialog
        gbc.gridx = 0; gbc.gridy = 0;
        dialog.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        dialog.add(txtNombre, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        dialog.add(new JLabel("Dirección:"), gbc);
        gbc.gridx = 1;
        dialog.add(txtDireccion, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        dialog.add(new JLabel("Saldo:"), gbc);
        gbc.gridx = 1;
        dialog.add(txtSaldo, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        dialog.add(new JLabel("Límite Crédito:"), gbc);
        gbc.gridx = 1;
        dialog.add(txtLimite, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        dialog.add(new JLabel("% Descuento:"), gbc);
        gbc.gridx = 1;
        dialog.add(txtDescuento, gbc);

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        gbc.gridx = 0; gbc.gridy = 5;
        gbc.gridwidth = 2;
        dialog.add(panelBotones, gbc);

        // Event listeners
        btnGuardar.addActionListener(e -> {
            try {
                String nombre = txtNombre.getText().trim();
                String direccion = txtDireccion.getText().trim();
                double saldo = Double.parseDouble(txtSaldo.getText().trim());
                double limite = Double.parseDouble(txtLimite.getText().trim());
                double descuento = Double.parseDouble(txtDescuento.getText().trim());

                if (nombre.isEmpty() || direccion.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Nombre y dirección son obligatorios");
                    return;
                }

                // Generar código automático
                String codigo = "C" + String.format("%03d", contadorClientes++);

                // Crear cliente y agregarlo al mapa
                Cliente nuevoCliente = new Cliente(codigo, nombre, direccion, saldo, limite, descuento);
                clientes.put(codigo, nuevoCliente);

                // Agregar a la tabla
                modelo.addRow(new Object[]{
                    codigo,
                    nombre,
                    direccion,
                    "$" + saldo,
                    "$" + limite,
                    descuento + "%"
                });

                JOptionPane.showMessageDialog(dialog, "Cliente creado exitosamente con código: " + codigo);
                dialog.dispose();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Error en los valores numéricos");
            }
        });

        btnCancelar.addActionListener(e -> dialog.dispose());

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void crearNuevoArticulo(DefaultTableModel modelo) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Nuevo Artículo", true);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Campos del formulario
        JTextField txtCodigo = new JTextField(20);
        JTextField txtNombre = new JTextField(20);
        JTextField txtDescripcion = new JTextField(20);
        JTextField txtPrecio = new JTextField(20);
        JComboBox<String> comboPlanta = new JComboBox<>();
        JTextField txtStockInicial = new JTextField(20);
        JTextField txtStockMinimo = new JTextField(20);

        // Llenar combo de plantas
        if (plantas != null) {
            for (Planta planta : plantas) {
                comboPlanta.addItem(planta.getNombre());
            }
        }

        // Agregar campos al dialog
        gbc.gridx = 0; gbc.gridy = 0;
        dialog.add(new JLabel("Código:"), gbc);
        gbc.gridx = 1;
        dialog.add(txtCodigo, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        dialog.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        dialog.add(txtNombre, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        dialog.add(new JLabel("Descripción:"), gbc);
        gbc.gridx = 1;
        dialog.add(txtDescripcion, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        dialog.add(new JLabel("Precio:"), gbc);
        gbc.gridx = 1;
        dialog.add(txtPrecio, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        dialog.add(new JLabel("Planta Productora:"), gbc);
        gbc.gridx = 1;
        dialog.add(comboPlanta, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        dialog.add(new JLabel("Stock Inicial:"), gbc);
        gbc.gridx = 1;
        dialog.add(txtStockInicial, gbc);

        gbc.gridx = 0; gbc.gridy = 6;
        dialog.add(new JLabel("Stock Mínimo:"), gbc);
        gbc.gridx = 1;
        dialog.add(txtStockMinimo, gbc);

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        gbc.gridx = 0; gbc.gridy = 7;
        gbc.gridwidth = 2;
        dialog.add(panelBotones, gbc);

        // Event listeners
        btnGuardar.addActionListener(e -> {
            try {
                String codigo = txtCodigo.getText().trim();
                String nombre = txtNombre.getText().trim();
                String descripcion = txtDescripcion.getText().trim();
                double precio = Double.parseDouble(txtPrecio.getText().trim());
                String plantaNombre = (String) comboPlanta.getSelectedItem();
                int stockInicial = Integer.parseInt(txtStockInicial.getText().trim());
                int stockMinimo = Integer.parseInt(txtStockMinimo.getText().trim());

                if (codigo.isEmpty() || nombre.isEmpty() || descripcion.isEmpty() || plantaNombre == null) {
                    JOptionPane.showMessageDialog(dialog, "Todos los campos son obligatorios");
                    return;
                }

                // Verificar que el código no exista
                boolean codigoExiste = articulos.stream().anyMatch(a -> a.getCodigo().equals(codigo));
                if (codigoExiste) {
                    JOptionPane.showMessageDialog(dialog, "El código ya existe");
                    return;
                }

                // Crear artículo y agregarlo a la lista
                Articulo nuevoArticulo = new Articulo(codigo, nombre, descripcion, precio);
                articulos.add(nuevoArticulo);

                // Buscar la planta seleccionada
                Planta plantaSeleccionada = plantas.stream()
                    .filter(p -> p.getNombre().equals(plantaNombre))
                    .findFirst().orElse(null);

                if (plantaSeleccionada != null) {
                    // Registrar stock inicial en la planta
                    gestorStock.registrarStock(nuevoArticulo, plantaSeleccionada, stockInicial, stockMinimo);
                }

                // Agregar a la tabla
                modelo.addRow(new Object[]{
                    codigo,
                    nombre,
                    descripcion,
                    "$" + precio
                });

                JOptionPane.showMessageDialog(dialog,
                    "Artículo creado exitosamente\nStock inicial de " + stockInicial +
                    " unidades registrado en " + plantaNombre);
                dialog.dispose();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Error en los valores numéricos");
            }
        });

        btnCancelar.addActionListener(e -> dialog.dispose());

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void editarCliente(JTable tabla, DefaultTableModel modelo) {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente para editar");
            return;
        }

        String codigo = tabla.getValueAt(fila, 0).toString();
        Cliente cliente = clientes.get(codigo);
        if (cliente == null) {
            JOptionPane.showMessageDialog(this, "Cliente no encontrado");
            return;
        }

        // Dialog de edición similar al de creación pero con valores precargados
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Editar Cliente", true);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JTextField txtNombre = new JTextField(cliente.getNombre(), 20);
        JTextField txtDireccion = new JTextField(cliente.getDireccionEnvio(), 20);
        JTextField txtSaldo = new JTextField(String.valueOf(cliente.getSaldo()), 20);
        JTextField txtLimite = new JTextField(String.valueOf(cliente.getLimiteCredito()), 20);
        JTextField txtDescuento = new JTextField(String.valueOf(cliente.getPorcentajeDescuento()), 20);

        // Layout similar al de crear
        gbc.gridx = 0; gbc.gridy = 0;
        dialog.add(new JLabel("Código: " + codigo), gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        dialog.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        dialog.add(txtNombre, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        dialog.add(new JLabel("Dirección:"), gbc);
        gbc.gridx = 1;
        dialog.add(txtDireccion, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        dialog.add(new JLabel("Saldo:"), gbc);
        gbc.gridx = 1;
        dialog.add(txtSaldo, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        dialog.add(new JLabel("Límite Crédito:"), gbc);
        gbc.gridx = 1;
        dialog.add(txtLimite, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        dialog.add(new JLabel("% Descuento:"), gbc);
        gbc.gridx = 1;
        dialog.add(txtDescuento, gbc);

        JPanel panelBotones = new JPanel(new FlowLayout());
        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        gbc.gridx = 0; gbc.gridy = 6;
        gbc.gridwidth = 2;
        dialog.add(panelBotones, gbc);

        btnGuardar.addActionListener(e -> {
            try {
                cliente.setNombre(txtNombre.getText().trim());
                cliente.setDireccionEnvio(txtDireccion.getText().trim());
                cliente.setSaldo(Double.parseDouble(txtSaldo.getText().trim()));
                cliente.setLimiteCredito(Double.parseDouble(txtLimite.getText().trim()));
                cliente.setPorcentajeDescuento(Double.parseDouble(txtDescuento.getText().trim()));

                // Actualizar tabla
                modelo.setValueAt(cliente.getNombre(), fila, 1);
                modelo.setValueAt(cliente.getDireccionEnvio(), fila, 2);
                modelo.setValueAt("$" + cliente.getSaldo(), fila, 3);
                modelo.setValueAt("$" + cliente.getLimiteCredito(), fila, 4);
                modelo.setValueAt(cliente.getPorcentajeDescuento() + "%", fila, 5);

                JOptionPane.showMessageDialog(dialog, "Cliente actualizado exitosamente");
                dialog.dispose();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Error en los valores numéricos");
            }
        });

        btnCancelar.addActionListener(e -> dialog.dispose());

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void eliminarCliente(JTable tabla, DefaultTableModel modelo) {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente para eliminar");
            return;
        }

        String codigo = tabla.getValueAt(fila, 0).toString();
        String nombre = tabla.getValueAt(fila, 1).toString();

        int confirmacion = JOptionPane.showConfirmDialog(this,
            "¿Está seguro de eliminar el cliente " + nombre + "?",
            "Confirmar eliminación",
            JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            clientes.remove(codigo);
            modelo.removeRow(fila);
            JOptionPane.showMessageDialog(this, "Cliente eliminado exitosamente");
        }
    }

    private void editarArticulo(JTable tabla, DefaultTableModel modelo) {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un artículo para editar");
            return;
        }

        String codigo = tabla.getValueAt(fila, 0).toString();
        Articulo articulo = articulos.stream()
            .filter(a -> a.getCodigo().equals(codigo))
            .findFirst().orElse(null);

        if (articulo == null) {
            JOptionPane.showMessageDialog(this, "Artículo no encontrado");
            return;
        }

        // Dialog de edición simple para precio
        String nuevoPrecio = JOptionPane.showInputDialog(this,
            "Nuevo precio para " + articulo.getNombre() + ":",
            articulo.getPrecio());

        if (nuevoPrecio != null) {
            try {
                double precio = Double.parseDouble(nuevoPrecio);
                articulo.setPrecio(precio);
                modelo.setValueAt("$" + precio, fila, 3);
                JOptionPane.showMessageDialog(this, "Precio actualizado exitosamente");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Precio inválido");
            }
        }
    }

    private void eliminarArticulo(JTable tabla, DefaultTableModel modelo) {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un artículo para eliminar");
            return;
        }

        String codigo = tabla.getValueAt(fila, 0).toString();
        String nombre = tabla.getValueAt(fila, 1).toString();

        int confirmacion = JOptionPane.showConfirmDialog(this,
            "¿Está seguro de eliminar el artículo " + nombre + "?\nEsto también eliminará su stock en todas las plantas.",
            "Confirmar eliminación",
            JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            // Buscar y eliminar artículo
            Articulo articuloAEliminar = articulos.stream()
                .filter(a -> a.getCodigo().equals(codigo))
                .findFirst().orElse(null);

            if (articuloAEliminar != null) {
                // Eliminar stock de todas las plantas
                for (Planta planta : plantas) {
                    gestorStock.eliminarStock(articuloAEliminar, planta);
                }

                // Eliminar de la lista de artículos
                articulos.remove(articuloAEliminar);
                modelo.removeRow(fila);

                JOptionPane.showMessageDialog(this, "Artículo eliminado exitosamente");
            }
        }
    }

    private JScrollPane crearTablaStock() {
        String[] colStock = {"Planta", "Artículo", "Cantidad", "Stock Mínimo"};
        DefaultTableModel modeloStock = new DefaultTableModel(colStock, 0);

        if (gestorStock != null) {
            for (StockPlanta stock : gestorStock.listarStock()) {
                modeloStock.addRow(new Object[]{
                    stock.getPlanta().getNombre(),
                    stock.getArticulo().getNombre(),
                    stock.getCantidadExistente(),
                    stock.getStockMinimo()
                });
            }
        }

        JTable tablaStock = new JTable(modeloStock);
        return new JScrollPane(tablaStock);
    }

    private JPanel crearTablaPedidos() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] colPedidos = {"ID", "Cliente", "Fecha", "Dirección", "Items", "Total"};
        DefaultTableModel modeloPedidos = new DefaultTableModel(colPedidos, 0);

        if (controladorPedido != null) {
            for (Pedido pedido : controladorPedido.getPedidos()) {
                double total = pedido.getDetalles().stream()
                    .mapToDouble(d -> d.getArticulo().getPrecio() * d.getCantidad())
                    .sum();
                modeloPedidos.addRow(new Object[]{
                    pedido.getIdPedido(),
                    pedido.getCliente().getNombre(),
                    pedido.getFecha(),
                    pedido.getDireccionEnvio(),
                    pedido.getDetalles().size(),
                    String.format("$%.2f", total)
                });
            }
        }

        JTable tablaPedidos = new JTable(modeloPedidos);
        panel.add(new JScrollPane(tablaPedidos), BorderLayout.CENTER);

        // Panel para mostrar detalles del pedido seleccionado
        JTextArea areaDetalles = new JTextArea(6, 40);
        areaDetalles.setEditable(false);
        areaDetalles.setBorder(BorderFactory.createTitledBorder("Detalles del Pedido"));
        panel.add(new JScrollPane(areaDetalles), BorderLayout.SOUTH);

        // Listener para mostrar detalles cuando se selecciona un pedido
        tablaPedidos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int fila = tablaPedidos.getSelectedRow();
                if (fila != -1 && controladorPedido != null) {
                    int idPedido = (Integer) tablaPedidos.getValueAt(fila, 0);
                    Pedido pedido = controladorPedido.buscarPedidoPorId(idPedido);
                    if (pedido != null) {
                        mostrarDetallesPedido(areaDetalles, pedido);
                    }
                }
            }
        });

        return panel;
    }

    private void mostrarDetallesPedido(JTextArea areaDetalles, Pedido pedido) {
        areaDetalles.setText("DETALLES DEL PEDIDO #" + pedido.getIdPedido() + "\n");
        areaDetalles.append("Cliente: " + pedido.getCliente().getNombre() + "\n");
        areaDetalles.append("Código Cliente: " + pedido.getCliente().getCodigo() + "\n");
        areaDetalles.append("Fecha: " + pedido.getFecha() + "\n");
        areaDetalles.append("Dirección: " + pedido.getDireccionEnvio() + "\n\n");
        areaDetalles.append("Artículos:\n");

        double totalPedido = 0;
        for (DetallePedido detalle : pedido.getDetalles()) {
            double subtotal = detalle.getArticulo().getPrecio() * detalle.getCantidad();
            totalPedido += subtotal;
            areaDetalles.append(String.format("- %s x%d = $%.2f\n",
                detalle.getArticulo().getNombre(),
                detalle.getCantidad(),
                subtotal));
        }

        areaDetalles.append(String.format("\nTOTAL: $%.2f", totalPedido));
    }

    public void refrescarDatos() {
        pestañas.removeAll();
        pestañas.addTab("Clientes", crearPanelClientes());
        pestañas.addTab("Artículos", crearPanelArticulos());
        pestañas.addTab("Stock", crearTablaStock());
        pestañas.addTab("Pedidos", crearTablaPedidos());
        revalidate();
        repaint();
    }
}
