package com.Digis01.FArceCajeroCliente.ML;

public class CajeroInventario {

    public int idcajero;
    public String ubicacion;
    public int saldo;

    private int idinventario;
    private double denominacion;
    private int cantidad;
    private String tipo;

    public int getIdinventario() {
        return idinventario;
    }

    public double getDenominacion() {
        return denominacion;
    }

    public int getCantidad() {
        return cantidad;
    }

    public String getTipo() {
        return tipo;
    }

    public CajeroInventario() {

    }

    public CajeroInventario(int idcajero, String ubicacion, int saldo,
            int idinventario, double denominacion, int cantidad, String tipo) {
        this.idcajero = idcajero;
        this.ubicacion = ubicacion;
        this.saldo = saldo;
        this.idinventario = idinventario;
        this.denominacion = denominacion;
        this.cantidad = cantidad;
        this.tipo = tipo;
    }

    public CajeroInventario(int idinventario, double denominacion, int cantidad, String tipo) {
        this.idinventario = idinventario;
        this.denominacion = denominacion;
        this.cantidad = cantidad;
        this.tipo = tipo;
    }

}
