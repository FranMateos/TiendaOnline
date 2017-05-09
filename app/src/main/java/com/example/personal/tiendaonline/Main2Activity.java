package com.example.personal.tiendaonline;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.personal.tiendaonline.objetos.ComprasAdapter;
import com.google.android.gms.maps.MapView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Main2Activity extends AppCompatActivity{

    private ListView lv;
    private TextView tv;
    private ArrayList<Compras> list;
    private EditText et;
    private Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ctx=this;
        if(list==null){
            list=new ArrayList<>();
        }
        list=ListaCompras.getInstance().getListaComprasVirt();
        ListaCompras.getInstance();
        tv= (TextView) findViewById(R.id.totalTextView);
        et= (EditText) findViewById(R.id.editText4);
        if(ListaCompras.getInstance().getListaComprasVirt()!=null){
        lv= (ListView) findViewById(R.id.listacompras);
        ComprasAdapter ca=new ComprasAdapter(this, android.R.layout.simple_list_item_1, list);
        lv.setAdapter(ca);}
        tv.setText("Total " + sumaTotal() + "€" );

    }

    public void comprarFinal(View view){

        if(list.size()>0){
        int i=0;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("compras");
        //se genera un nuevo elemento
        HashMap mapa=new HashMap();
        mapa.put("comprador","Prueba");
        mapa.put("fecha", list.get(0).getFecha());
            mapa.put("importe", sumaTotal() + "€");
        mapa.put("productos", 0);
        String cad="compra" + new Date().getTime();
        myRef.child(cad).setValue(mapa);
        DatabaseReference myRef2 = database.getReference("compras").child(cad).child("productos");
        HashMap mapa2=new HashMap();
        for(Compras cmp:list) {
            mapa2.put("codprod", cmp.getCodprod());
            mapa2.put("cantidad", cmp.getCantidad());
            mapa2.put("nombre", cmp.getNombre());
            mapa2.put("precio", cmp.getPrecio());
            myRef2.child("producto_" + i).setValue(mapa2);
            i++;
        }
        Toast.makeText(this, "La compra se ha realizado correctamente", Toast.LENGTH_SHORT).show();
        list.clear();}else{
            Toast.makeText(this, "Debes añadir productos", Toast.LENGTH_SHORT).show();
        }
    }

    public void cancelarCompra(View view){

        Toast.makeText(this, "La compra ha sido cancelada correctamente", Toast.LENGTH_SHORT).show();
        Intent intent= new Intent(this, MainActivity.class);
        if(list!=null){
        list.clear();}
        startActivity(intent);

    }

    public double sumaTotal(){

        int suma=0;
        if(list!=null){
        for(Compras cmp:ListaCompras.getInstance().getListaComprasVirt()){

            suma+=cmp.getCantidad()*cmp.getPrecio();

        }}

        return suma;
    }

}
