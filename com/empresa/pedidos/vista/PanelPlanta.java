package com.empresa.pedidos.vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import com.empresa.pedidos.modelo.*;
import com.empresa.pedidos.controlador.*;
import java.util.*;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;

public class PanelPlanta extends JPanel {
    private GestorStockPlanta gestorStock;
    private List<Articulo> articulos;
    private List<Planta> plantas;

    private JComboBox<String> comboPlanta;
    private JTable tablaStockPlanta;
    private DefaultTableModel modeloStockPlanta;
    private JLabel lblAlertas;

    public PanelPlanta(GestorStockPlanta gestorStock, List<Articulo> articulos, List<Planta> plantas) {
        this.gestorStock = gestorStock;
        this.articulos = articulos;
        this.plantas = plantas;

        initComponentes();
        cargarDatos();
    }

    private void initComponentes() {
        setLayout(new BorderLayout());

        JLabel titulo = new JLabel("Panel de Planta");
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));

        // Panel superior con selector de planta y alertas
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.add(titulo, BorderLayout.NORTH);

        JPanel panelSelector = new JPanel(new FlowLayout());
        JLabel lblPlanta = new JLabel("Seleccionar Planta:");
        comboPlanta = new JComboBox<>();
        panelSelector.add(lblPlanta);
        panelSelector.add(comboPlanta);

        // Etiqueta de alertas de stock bajo
        lblAlertas = new JLabel(" ");
        lblAlertas.setForeground(Color.RED);
        lblAlertas.setFont(new Font("Arial", Font.BOLD, 12));
        panelSelector.add(Box.createHorizontalStrut(20));
        panelSelector.add(lblAlertas);

        panelSuperior.add(panelSelector, BorderLayout.CENTER);
        add(panelSuperior, BorderLayout.NORTH);

        // Tabla de stock específica de la planta seleccionada
        String[] colStock = {"Artículo", "Cantidad Actual", "Stock Mínimo", "Nueva Cantidad", "Estado"};
        modeloStockPlanta = new DefaultTableModel(colStock, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3; // Solo la columna "Nueva Cantidad" es editable
            }
        };

        tablaStockPlanta = new JTable(modeloStockPlanta);

        // Renderer personalizado para colorear filas según el stock
        tablaStockPlanta.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (!isSelected) {
                    String estado = (String) table.getValueAt(row, 4);
                    if ("CRÍTICO".equals(estado)) {
                        c.setBackground(new Color(255, 200, 200)); // Rojo claro
                    } else if ("BAJO".equals(estado)) {
                        c.setBackground(new Color(255, 255, 200)); // Amarillo claro
                    } else {
                        c.setBackground(Color.WHITE);
                    }
                } else {
                    c.setBackground(table.getSelectionBackground());
                }

                return c;
            }
        });

        JScrollPane scrollStock = new JScrollPane(tablaStockPlanta);
        add(scrollStock, BorderLayout.CENTER);

        // Botones de acción
        JPanel panelBotones = new JPanel(new FlowLayout());
        JButton btnActualizar = new JButton("Actualizar Vista");
        JButton btnReabastecer = new JButton("Reabastecer Stock");
        JButton btnAgregarStock = new JButton("Agregar Stock");
        JButton btnVerAlertas = new JButton("Ver Todas las Alertas");

        panelBotones.add(btnActualizar);
        panelBotones.add(btnReabastecer);
        panelBotones.add(btnAgregarStock);
        panelBotones.add(btnVerAlertas);
        add(panelBotones, BorderLayout.SOUTH);

        // Event listeners
        comboPlanta.addActionListener(e -> actualizarTablaStock());
        btnActualizar.addActionListener(e -> actualizarTablaStock());
        btnReabastecer.addActionListener(this::reabastecer);
        btnAgregarStock.addActionListener(this::agregarStock);
        btnVerAlertas.addActionListener(this::mostrarTodasLasAlertas);
    }

    private void cargarDatos() {
        // Cargar plantas
        comboPlanta.removeAllItems();
        if (plantas != null) {
            for (Planta planta : plantas) {
                comboPlanta.addItem(planta.getNombre());
            }
        }

        // Cargar stock
        actualizarTablaStock();
    }

    private void actualizarTablaStock() {
        modeloStockPlanta.setRowCount(0);
        String plantaSeleccionada = (String) comboPlanta.getSelectedItem();

        if (plantaSeleccionada != null && gestorStock != null && plantas != null) {
            Planta planta = plantas.stream()
                .filter(p -> p.getNombre().equals(plantaSeleccionada))
                .findFirst().orElse(null);

            if (planta != null) {
                List<StockPlanta> stocksPlanta = gestorStock.buscarPorPlanta(planta);
                int alertasCriticas = 0;
                int alertasBajas = 0;

                for (StockPlanta stock : stocksPlanta) {
                    String estado;
                    if (stock.getCantidadExistente() == 0) {
                        estado = "CRÍTICO";
                        alertasCriticas++;
                    } else if (stock.getCantidadExistente() <= stock.getStockMinimo()) {
                        estado = "BAJO";
                        alertasBajas++;
                    } else {
                        estado = "NORMAL";
                    }

                    modeloStockPlanta.addRow(new Object[]{
                        stock.getArticulo().getNombre(),
                        stock.getCantidadExistente(),
                        stock.getStockMinimo(),
                        stock.getCantidadExistente(), // Valor inicial para nueva cantidad
                        estado
                    });
                }

                // Actualizar etiqueta de alertas
                actualizarEtiquetaAlertas(alertasCriticas, alertasBajas);
            }
        }
    }

    private void actualizarEtiquetaAlertas(int criticas, int bajas) {
        if (criticas > 0 || bajas > 0) {
            String mensaje = "⚠️ ALERTAS: ";
            if (criticas > 0) {
                mensaje += criticas + " artículo(s) SIN STOCK";
            }
            if (bajas > 0) {
                if (criticas > 0) mensaje += ", ";
                mensaje += bajas + " con stock BAJO";
            }
            lblAlertas.setText(mensaje);
        } else {
            lblAlertas.setText("✅ Todos los stocks están en niveles normales");
            lblAlertas.setForeground(Color.GREEN);
        }
    }

    private void reabastecer(ActionEvent e) {
        int fila = tablaStockPlanta.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un artículo de la tabla");
            return;
        }

        String plantaSeleccionada = (String) comboPlanta.getSelectedItem();
        String articuloNombre = tablaStockPlanta.getValueAt(fila, 0).toString();

        try {
            int nuevaCantidad = Integer.parseInt(tablaStockPlanta.getValueAt(fila, 3).toString());

            if (plantaSeleccionada != null && gestorStock != null) {
                Planta planta = plantas.stream()
                    .filter(p -> p.getNombre().equals(plantaSeleccionada))
                    .findFirst().orElse(null);

                Articulo articulo = articulos.stream()
                    .filter(a -> a.getNombre().equals(articuloNombre))
                    .findFirst().orElse(null);

                if (planta != null && articulo != null) {
                    StockPlanta stockActual = gestorStock.buscarStock(articulo, planta);
                    if (stockActual != null) {
                        // Usar el método directo del modelo para actualizar
                        stockActual.setCantidadExistente(nuevaCantidad);
                        actualizarTablaStock(); // Refrescar para actualizar colores y alertas
                        JOptionPane.showMessageDialog(this,
                            "Stock actualizado: " + articuloNombre + " en " + plantaSeleccionada);
                    }
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Cantidad inválida");
        }
    }

    private void agregarStock(ActionEvent e) {
        int fila = tablaStockPlanta.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un artículo de la tabla");
            return;
        }

        String cantidadStr = JOptionPane.showInputDialog(this, "Cantidad a agregar:");
        if (cantidadStr == null || cantidadStr.trim().isEmpty()) {
            return;
        }

        try {
            int cantidadAgregar = Integer.parseInt(cantidadStr);
            if (cantidadAgregar <= 0) {
                JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor a 0");
                return;
            }

            String plantaSeleccionada = (String) comboPlanta.getSelectedItem();
            String articuloNombre = tablaStockPlanta.getValueAt(fila, 0).toString();

            if (plantaSeleccionada != null && gestorStock != null) {
                Planta planta = plantas.stream()
                    .filter(p -> p.getNombre().equals(plantaSeleccionada))
                    .findFirst().orElse(null);

                Articulo articulo = articulos.stream()
                    .filter(a -> a.getNombre().equals(articuloNombre))
                    .findFirst().orElse(null);

                if (planta != null && articulo != null) {
                    StockPlanta stockActual = gestorStock.buscarStock(articulo, planta);
                    if (stockActual != null) {
                        // Usar el método agregarStock del modelo
                        stockActual.agregarStock(cantidadAgregar);
                        actualizarTablaStock(); // Refrescar para actualizar colores y alertas
                        JOptionPane.showMessageDialog(this,
                            "Se agregaron " + cantidadAgregar + " unidades. Stock actual: " +
                            stockActual.getCantidadExistente());
                    }
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Cantidad inválida");
        }
    }

    private void mostrarTodasLasAlertas(ActionEvent e) {
        if (gestorStock != null) {
            List<StockPlanta> stockBajo = gestorStock.obtenerStockBajoMinimo();

            if (stockBajo.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "✅ No hay alertas de stock.\nTodos los artículos están en niveles normales.");
                return;
            }

            StringBuilder mensaje = new StringBuilder("🚨 ALERTAS DE STOCK EN TODAS LAS PLANTAS:\n\n");

            for (StockPlanta stock : stockBajo) {
                mensaje.append(String.format("• %s en %s: %d unidades (mín: %d)\n",
                    stock.getArticulo().getNombre(),
                    stock.getPlanta().getNombre(),
                    stock.getCantidadExistente(),
                    stock.getStockMinimo()));
            }

            mensaje.append("\n¡Se requiere reabastecimiento urgente!");

            JTextArea textArea = new JTextArea(mensaje.toString());
            textArea.setEditable(false);
            textArea.setRows(10);
            textArea.setColumns(40);

            JOptionPane.showMessageDialog(this, new JScrollPane(textArea),
                "Alertas de Stock - Todas las Plantas", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void refrescarDatos() {
        cargarDatos();
    }
}
