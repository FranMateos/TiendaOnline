package com.example.personal.tiendaonline;

import java.nio.DoubleBuffer;
import java.util.HashMap;

/**
 * Created by Personal on 30/04/2017.
 */
public class Tienda {

    private String nombre;
    private String direccion;
    private String correo;
    private String telefono;
    private String imagen;
    private double latitud;
    private double longitud;

    private static Tienda ourInstance = new Tienda();

    public static Tienda getInstance() {
        return ourInstance;
    }

    private Tienda() {
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return getInstance();
    }

    public void cargarDatosTiendaHashMap(HashMap mapa){

        this.nombre= (String) mapa.get("nombre");
        this.direccion= (String) mapa.get("direccion");
        this.correo= (String) mapa.get("correo");
        this.telefono= (String) mapa.get("telefono");
        this.latitud= (Double) mapa.get("latitud");
        this.longitud= (Double) mapa.get("longitud");
        this.imagen= (String) mapa.get("imagen");

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public static Tienda getOurInstance() {
        return ourInstance;
    }

    public static void setOurInstance(Tienda ourInstance) {
        Tienda.ourInstance = ourInstance;
    }
}
