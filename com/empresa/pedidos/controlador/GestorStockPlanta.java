package com.empresa.pedidos.controlador;

import com.empresa.pedidos.modelo.Articulo;
import com.empresa.pedidos.modelo.Planta;
import com.empresa.pedidos.modelo.StockPlanta;

import java.util.ArrayList;
import java.util.List;

public class GestorStockPlanta {
    private List<StockPlanta> listaStock = new ArrayList<>();

    // Crear o registrar nuevo stock
    public boolean registrarStock(Articulo articulo, Planta planta, int cantidad, int stockMinimo) {
        if (buscarStock(articulo, planta) != null) {
            return false; // Ya existe
        }
        listaStock.add(new StockPlanta(articulo, planta, cantidad, stockMinimo));
        return true;
    }

    // Leer o listar todos los registros de stock
    public List<StockPlanta> listarStock() {
        return new ArrayList<>(listaStock); // Devolver copia para evitar modificaciones externas
    }

    // Buscar stock por artículo y planta
    public StockPlanta buscarStock(Articulo articulo, Planta planta) {
        for (StockPlanta s : listaStock) {
            if (s.getArticulo().equals(articulo) && s.getPlanta().equals(planta)) {
                return s;
            }
        }
        return null;
    }

    // Buscar todos los stock de un artículo
    public List<StockPlanta> buscarPorArticulo(Articulo articulo) {
        List<StockPlanta> resultado = new ArrayList<>();
        for (StockPlanta s : listaStock) {
            if (s.getArticulo().equals(articulo)) {
                resultado.add(s);
            }
        }
        return resultado;
    }

    // Buscar todos los stock en una planta
    public List<StockPlanta> buscarPorPlanta(Planta planta) {
        List<StockPlanta> resultado = new ArrayList<>();
        for (StockPlanta s : listaStock) {
            if (s.getPlanta().equals(planta)) {
                resultado.add(s);
            }
        }
        return resultado;
    }

    // Actualizar stock existente
    public boolean actualizarStock(Articulo articulo, Planta planta, int nuevaCantidad, int nuevoStockMinimo) {
        StockPlanta existente = buscarStock(articulo, planta);
        if (existente != null) {
            // Eliminar y reemplazar
            listaStock.remove(existente);
            listaStock.add(new StockPlanta(articulo, planta, nuevaCantidad, nuevoStockMinimo));
            return true;
        }
        return false;
    }

    // Eliminar un registro de stock
    public boolean eliminarStock(Articulo articulo, Planta planta) {
        StockPlanta existente = buscarStock(articulo, planta);
        if (existente != null) {
            listaStock.remove(existente);
            return true;
        }
        return false;
    }

    // Nuevo método para detectar stock bajo el mínimo
    public List<StockPlanta> obtenerStockBajoMinimo() {
        List<StockPlanta> stockBajo = new ArrayList<>();
        for (StockPlanta sp : listaStock) {
            if (sp.getCantidadExistente() <= sp.getStockMinimo()) {
                stockBajo.add(sp);
            }
        }
        return stockBajo;
    }

    // Método para obtener stock bajo mínimo de una planta específica
    public List<StockPlanta> obtenerStockBajoMinimoPorPlanta(Planta planta) {
        List<StockPlanta> stockBajo = new ArrayList<>();
        for (StockPlanta sp : listaStock) {
            if (sp.getPlanta().equals(planta) && sp.getCantidadExistente() <= sp.getStockMinimo()) {
                stockBajo.add(sp);
            }
        }
        return stockBajo;
    }

    // Método para verificar si un artículo específico está bajo stock mínimo
    public boolean esStockBajoMinimo(Articulo articulo, Planta planta) {
        StockPlanta stock = buscarStock(articulo, planta);
        return stock != null && stock.getCantidadExistente() <= stock.getStockMinimo();
    }
}
