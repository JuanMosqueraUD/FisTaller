package com.empresa.pedidos.vista;

import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {

    public VentanaPrincipal() {
        setTitle("Gestión de Pedidos");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        initComponentes();
    }

    private void initComponentes() {
        JLabel etiqueta = new JLabel("Bienvenido al sistema de pedidos");
        etiqueta.setHorizontalAlignment(SwingConstants.CENTER);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(etiqueta, BorderLayout.CENTER);
    }
}
