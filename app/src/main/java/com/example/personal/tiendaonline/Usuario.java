package com.example.personal.tiendaonline;

import java.util.HashMap;

/**
 * Created by Personal on 19/04/2017.
 */

public class Usuario {

    private String nombre;
    private String apellidos;
    private String direcion;
    private String correo;
    private String telefono;
    private String usuario;
    private String contrasenia;
    private boolean administrador;

    private static Usuario ourInstance = new Usuario();

    public static Usuario getInstance() {
        return ourInstance;
    }

    private Usuario() {
    }

    public void cargarDatosUsuarioHashMap(HashMap mapa){

        this.usuario= (String) mapa.get("usuario");
        this.nombre= (String) mapa.get("nombre");
        this.apellidos= (String) mapa.get("apellidos");
        this.contrasenia= (String) mapa.get("contrasenia");
        this.correo= (String) mapa.get("correo");
        this.telefono= (String) mapa.get("telefono");
        this.administrador= (Boolean) mapa.get("administrador");
        this.direcion= (String) mapa.get("direccion");

    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return getInstance();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getDirecion() {
        return direcion;
    }

    public void setDirecion(String direcion) {
        this.direcion = direcion;
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

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public boolean isAdministrador() {
        return administrador;
    }

    public void setAdministrador(boolean administrador) {
        this.administrador = administrador;
    }
}
