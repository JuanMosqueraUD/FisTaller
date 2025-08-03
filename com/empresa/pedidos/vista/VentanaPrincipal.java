package com.empresa.pedidos.vista;

import javax.swing.*;
import java.awt.*;
import com.empresa.pedidos.modelo.*;
import com.empresa.pedidos.controlador.*;
import java.util.*;
import java.util.List;

public class VentanaPrincipal extends JFrame {
    private CardLayout cardLayout;
    private JPanel panelContenedor;

    // Referencias al backend
    private static GestorStockPlanta gestorStock;
    private static ControladorPedido controladorPedido;
    private static Map<String, Cliente> clientes;
    private static List<Articulo> articulos;
    private static List<Planta> plantas;

    // Paneles modulares
    private PanelCliente panelCliente;
    private PanelAdmin panelAdmin;
    private PanelPlanta panelPlanta;

    public VentanaPrincipal() {
        setTitle("Gestión de Pedidos");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        initComponentes();
    }

    // Método estático para inicializar los datos desde Main
    public static void inicializarDatos(GestorStockPlanta gestor, ControladorPedido ctrl,
                                       Map<String, Cliente> clientesMap, List<Articulo> arts, List<Planta> plants) {
        gestorStock = gestor;
        controladorPedido = ctrl;
        clientes = clientesMap;
        articulos = arts;
        plantas = plants;
    }

    private void initComponentes() {
        cardLayout = new CardLayout();
        panelContenedor = new JPanel(cardLayout);

        // Crear paneles modulares
        JPanel panelBienvenida = crearPanelBienvenida();
        panelCliente = new PanelCliente(gestorStock, controladorPedido, clientes, articulos);
        panelAdmin = new PanelAdmin(gestorStock, controladorPedido, clientes, articulos, plantas);
        panelPlanta = new PanelPlanta(gestorStock, articulos, plantas);

        panelContenedor.add(panelBienvenida, "Bienvenida");
        panelContenedor.add(panelCliente, "Cliente");
        panelContenedor.add(panelAdmin, "Admin");
        panelContenedor.add(panelPlanta, "Planta");

        setLayout(new BorderLayout());
        add(crearMenuRoles(), BorderLayout.NORTH);
        add(panelContenedor, BorderLayout.CENTER);
    }

    private JPanel crearPanelBienvenida() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel etiqueta = new JLabel("<html><center>Bienvenido al Sistema de Gestión de Pedidos<br/>Seleccione su rol en el menú superior</center></html>");
        etiqueta.setHorizontalAlignment(SwingConstants.CENTER);
        etiqueta.setFont(new Font("Arial", Font.PLAIN, 16));
        panel.add(etiqueta, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearMenuRoles() {
        JPanel panelMenu = new JPanel(new FlowLayout());
        panelMenu.setBorder(BorderFactory.createEtchedBorder());

        JButton btnCliente = new JButton("Cliente");
        JButton btnAdmin = new JButton("Administrador");
        JButton btnPlanta = new JButton("Planta");
        JButton btnRefrescar = new JButton("Refrescar Datos");

        // Estilo de botones
        btnCliente.setPreferredSize(new Dimension(120, 30));
        btnAdmin.setPreferredSize(new Dimension(120, 30));
        btnPlanta.setPreferredSize(new Dimension(120, 30));
        btnRefrescar.setPreferredSize(new Dimension(120, 30));

        btnCliente.addActionListener(e -> {
            cardLayout.show(panelContenedor, "Cliente");
            panelCliente.refrescarDatos();
        });

        btnAdmin.addActionListener(e -> {
            cardLayout.show(panelContenedor, "Admin");
            panelAdmin.refrescarDatos();
        });

        btnPlanta.addActionListener(e -> {
            cardLayout.show(panelContenedor, "Planta");
            panelPlanta.refrescarDatos();
        });

        btnRefrescar.addActionListener(e -> {
            // Refrescar todos los paneles
            panelCliente.refrescarDatos();
            panelAdmin.refrescarDatos();
            panelPlanta.refrescarDatos();
            JOptionPane.showMessageDialog(this, "Datos actualizados");
        });

        panelMenu.add(btnCliente);
        panelMenu.add(btnAdmin);
        panelMenu.add(btnPlanta);
        panelMenu.add(Box.createHorizontalStrut(20)); // Separador
        panelMenu.add(btnRefrescar);

        return panelMenu;
    }
}
