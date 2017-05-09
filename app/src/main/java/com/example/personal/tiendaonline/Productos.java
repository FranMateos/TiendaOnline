package com.example.personal.tiendaonline;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Personal on 19/04/2017.
 */

public class Productos {

    private String codigo;
    private String nombre;
    private String descripción;
    private double precio;
    private int cantidad;
    private boolean disponible;
    private String talla;
    private String color;
    private String imagen;
    private String imagenSmall;
    public static ArrayList<Productos> listaProd=new ArrayList<>();

    public Productos() {
    }

    public Productos(String codigo, String nombre, String descripción, double precio, int cantidad, boolean disponible, String imagen, String imagenSmall) {
        this.codigo=codigo;
        this.nombre = nombre;
        this.descripción = descripción;
        this.precio = precio;
        this.cantidad = cantidad;
        this.disponible = disponible;
        this.imagen = imagen;
        this.imagenSmall = imagenSmall;
    }

    public void cargarDatosProductosHashMap(HashMap mapa){

        this.nombre= (String) mapa.get("nombre");
        this.descripción = (String) mapa.get("descripcion");
        this.precio = (long) mapa.get("precio");
        this.cantidad = (int) mapa.get("cantidad");
        this.disponible = (boolean) mapa.get("disponible");
        this.imagen = (String) mapa.get("urlimagen");
        insertarProducto();

    }

    public void insertarProducto(){

        listaProd.add(this);

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripción() {
        return descripción;
    }

    public void setDescripción(String descripción) {
        this.descripción = descripción;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public long getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getTalla() {
        return talla;
    }

    public void setTalla(String talla) {
        this.talla = talla;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getImagenSmall() {
        return imagenSmall;
    }

    public void setImagenSmall(String imagenSmall) {
        this.imagenSmall = imagenSmall;
    }
}
