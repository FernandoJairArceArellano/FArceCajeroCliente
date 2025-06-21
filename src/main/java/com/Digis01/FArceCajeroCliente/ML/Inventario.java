package com.Digis01.FArceCajeroCliente.ML;

public class Inventario {

    private int idinventario;
    private int idcajero;
    private double denominacion;
    private int cantidad;
    private String tipo;

    public int getIdinventario() {
        return idinventario;
    }

    public void setIdinventario(int idinventario) {
        this.idinventario = idinventario;
    }

    public int getIdcajero() {
        return idcajero;
    }

    public void setIdcajero(int idcajero) {
        this.idcajero = idcajero;
    }

    public double getDenominacion() {
        return denominacion;
    }

    public void setDenominacion(double denominacion) {
        this.denominacion = denominacion;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

}
