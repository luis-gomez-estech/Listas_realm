package com.luisgomez.listas_realm.modelos;

import com.luisgomez.listas_realm.aplicacion.MiAplicacion;

import java.util.Date;

import io.realm.RealmObject;

public class LibroModelo extends RealmObject{

    private int id;
    private String nombre;
    private String descripcion;
    private Date fecha;

    //Para relacionar con otros modelos
    //private RealmList<TuModelo> listaObjeto;

    //Realm necesita un contructor vacio
    public LibroModelo() {

    }

    public LibroModelo(String nombre, String descripcion) {
        this.id = MiAplicacion.usuarioid.incrementAndGet();
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fecha = new Date();
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public Date getFecha() {
        return fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
