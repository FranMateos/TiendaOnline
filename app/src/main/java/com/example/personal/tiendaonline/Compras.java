package com.example.personal.tiendaonline;

import java.util.ArrayList;

/**
 * Created by Personal on 04/05/2017.
 */

public class Compras {

    private String nombre;
    private String codcomp;
    private int cantidad;
    private String codprod;
    private String comprador;
    private String fecha;
    private String imagen;
    private double precio;
    private static Compras cp;

    private Compras() {
    }

    public Compras(String nombre, String codcomp, int cantidad, String codprod, String comprador, String fecha, String imagen, double precio) {
        this.nombre = nombre;
        this.codcomp = codcomp;
        this.cantidad = cantidad;
        this.codprod = codprod;
        this.comprador = comprador;
        this.fecha = fecha;
        this.imagen = imagen;
        this.precio = precio;
        ListaCompras.getInstance().aniadirComprasVirt(this);
    }

    public String getCodcomp() {
        return codcomp;
    }

    public void setCodcomp(String codcomp) {
        this.codcomp = codcomp;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getCodprod() {
        return codprod;
    }

    public void setCodprod(String codprod) {
        this.codprod = codprod;
    }

    public String getComprador() {
        return comprador;
    }

    public void setComprador(String comprador) {
        this.comprador = comprador;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }


}
