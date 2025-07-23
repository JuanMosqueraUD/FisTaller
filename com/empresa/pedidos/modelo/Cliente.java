package com.empresa.pedidos.modelo;

public class Cliente {
    private String codigo;
    private String nombre;
    private String direccionEnvio;
    private double saldo;
    private double limiteCredito;
    private double porcentajeDescuento;

    public Cliente(String codigo, String nombre, String direccionEnvio, double saldo, double limiteCredito, double porcentajeDescuento) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.direccionEnvio = direccionEnvio;
        this.saldo = saldo;
        this.limiteCredito = limiteCredito;
        this.porcentajeDescuento = porcentajeDescuento;
    }

    // Getters y setters
    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public String getDireccionEnvio() { return direccionEnvio; }
    public double getSaldo() { return saldo; }
    public double getLimiteCredito() { return limiteCredito; }
    public double getPorcentajeDescuento() { return porcentajeDescuento; }

    public void setSaldo(double saldo) { this.saldo = saldo; }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public void setDireccionEnvio(String direccionEnvio) {
        this.direccionEnvio = direccionEnvio;
    }
    public void setLimiteCredito(double limiteCredito) {
        this.limiteCredito = limiteCredito;
    }
    public void setPorcentajeDescuento(double porcentajeDescuento) {
        this.porcentajeDescuento = porcentajeDescuento;
    }

}
