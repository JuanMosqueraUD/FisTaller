package com.empresa.pedidos.modelo;

public class StockPlanta {
    private Articulo articulo;
    private Planta planta;
    private int cantidadExistente;
    private int stockMinimo;

    public StockPlanta(Articulo articulo, Planta planta, int cantidadExistente, int stockMinimo) {
        this.articulo = articulo;
        this.planta = planta;
        this.cantidadExistente = cantidadExistente;
        this.stockMinimo = stockMinimo;
    }

    // Getters
    public Articulo getArticulo() { return articulo; }
    public Planta getPlanta() { return planta; }
    public int getCantidadExistente() { return cantidadExistente; }
    public int getStockMinimo() { return stockMinimo; }

    public void descontarStock(int cantidad) {
        if (cantidad <= cantidadExistente) {
            cantidadExistente -= cantidad;
        }
    }

    // Métodos para actualizar stock directamente
    public void setCantidadExistente(int cantidadExistente) {
        this.cantidadExistente = cantidadExistente;
    }

    public void setStockMinimo(int stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public void agregarStock(int cantidad) {
        this.cantidadExistente += cantidad;
    }
}
