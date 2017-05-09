package com.example.personal.tiendaonline;

import java.util.ArrayList;

/**
 * Created by Personal on 05/05/2017.
 */

public class ListaCompras {

    private static ArrayList<Compras> listaComprasVirt;
    private static final ListaCompras ourInstance = new ListaCompras();


    public static ListaCompras getInstance() {
        return ourInstance;
    }

    private ListaCompras() {
    }

    public ArrayList<Compras> getListaComprasVirt() {
        if(listaComprasVirt==null){
            listaComprasVirt=new ArrayList<>();
        }

        return ListaCompras.listaComprasVirt;
    }

    public ArrayList<Compras> aniadirComprasVirt(Compras comp) {
        if(ListaCompras.listaComprasVirt==null){
            ListaCompras.listaComprasVirt = new ArrayList<>();
        }
        ListaCompras.listaComprasVirt.add(comp);
        return ListaCompras.listaComprasVirt;
    }

    public void setListaComprasVirt(ArrayList<Compras> listaComprasVirt) {
        if(ListaCompras.listaComprasVirt==null){
            ListaCompras.listaComprasVirt = new ArrayList<>();
        }
        ListaCompras.listaComprasVirt = listaComprasVirt;
    }

    public ListaCompras getOurInstance() {
        return ourInstance;
    }
}
