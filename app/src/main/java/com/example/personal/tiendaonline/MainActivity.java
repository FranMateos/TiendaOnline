package com.example.personal.tiendaonline;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.example.personal.tiendaonline.Tareas.Temporizador;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private ArrayList<Productos> list;
    private static RecyclerView recycler;
    private ProductosAdapter adapter;
    private Context context;
    private DatabaseReference myRef;
    private DatabaseReference myShopRef;
    private Productos producto;
    private FirebaseDatabase database;
    private DatabaseReference myImgRef;
    private Tienda tienda;
    private File foto;
    private ArrayList<Compras> listCp;
    private String nombreImagen;
    private Calendar fec;
    private NavigationView navigationView;
    public static boolean registrado;
    private Menu config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //instanciar el objeto Firebase de tipo singleton
       database=FirebaseDatabase.getInstance();
        //hacer referencia a la base de datos
        myRef = FirebaseDatabase.getInstance().getReference("producto");
        myShopRef = FirebaseDatabase.getInstance().getReference("tienda");
        context=this.getApplicationContext();
        // lanzar tarea programada
        Temporizador.iniciarTareaDaemon(this, 1);
        // crear objeto barra de herramientas
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        list=new ArrayList<>();
        adapter=new ProductosAdapter(list, this);

        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(context, "Has pulsado el item " + recycler.getChildLayoutPosition(v), Toast.LENGTH_SHORT).show();
                generarCompra(recycler.getChildLayoutPosition(v), v);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Sección de contacto
                Intent intent= new Intent(context, MapsActivity.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        metodosBaseDatos();
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            if(registrado){
            Intent intent=new Intent(context, Main3Activity.class);
            startActivity(intent);}else{
                Intent reg=new Intent(context, LoginActivity.class);
                startActivity(reg);
            }
            return true;
        }if(id == R.id.carrito){
            Intent intent=new Intent(context, Main2Activity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_gallery) {

            Intent reg=new Intent(this, LoginActivity.class);
            startActivity(reg);

        } else if (id == R.id.nav_slideshow) {

            Intent conf=new Intent(this, Main2Activity.class);
            startActivity(conf);

        } else if (id == R.id.nav_manage) {

            if(registrado){
                Intent intent=new Intent(context, Main3Activity.class);
                startActivity(intent);}else{
                Intent reg=new Intent(context, LoginActivity.class);
                startActivity(reg);
            }

        } else if (id == R.id.nav_country) {

            Intent mapa=new Intent(this, MapsActivity.class);
            startActivity(mapa);

        }

        else if (id == R.id.nav_share) {

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, tienda.getNombre() + "\n" + tienda.getTelefono() + "\n" + tienda.getCorreo() + "\n" + tienda.getDireccion());
            sendIntent.setType("text/plain");
            startActivity(Intent.createChooser(sendIntent, "Compartir con..."));

        } else if (id == R.id.nav_send) {

            Intent envio=new Intent(this, EnviarCorreo.class);
            startActivity(envio);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void metodosBaseDatos(){

        //obtiene el nombre del TAG, en este caso tiendaonline-1a3b9
        myRef.getKey();
        //referencias dinámicas
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            //cuando cambian los datos
            public void onDataChange(DataSnapshot dataSnapshot) {

                HashMap values = (HashMap) dataSnapshot.getValue();
                //list.add(values);
                //Toast.makeText(context,"Los datos de la base de datos son: " + values, Toast.LENGTH_SHORT).show();
                //Toast.makeText(context, "Los valores de prod1 son: " + values.get("prod1"), Toast.LENGTH_SHORT).show();
                /*tienda=Tienda.getInstance();
                tienda.cargarDatosTiendaHashMap(values);*/
            }

            @Override
            //cuando se cancela la conexión por algún motivo
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, "ERROR de conexión con la base de Datos, entró en el onCancelled()", Toast.LENGTH_SHORT).show();
            }
        });

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                int i=1;
                HashMap val= (HashMap) dataSnapshot.getValue();
                /*Productos prod=new Productos();
                prod.cargarDatosProductosHashMap(val);*/
                //codigo, String nombre, String descripción, double precio, int cantidad, boolean disponible, String imagen
                if(list==null){
                    list=new ArrayList<Productos>();
                }
                list.add(new Productos(dataSnapshot.getKey(),
                        (String) val.get("nombre"),
                        (String) val.get("descripcion"),
                        Double.parseDouble((String) val.get("precio")),
                        Integer.parseInt((String) val.get("cantidad")),
                        (boolean) val.get("disponible"),
                        (String) val.get("urlimagen"),
                        (String) val.get("urlimagensmall")
                        ));
                cargarRecycler();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        myShopRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap values = (HashMap) dataSnapshot.getValue();
                tienda=Tienda.getInstance();
                tienda.cargarDatosTiendaHashMap(values);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void actualizarDatosBaseDatos(){

        /*DatabaseReference usersRef = myRef.child("users");*/
/*
        Map<String, Productos> users = new HashMap<String, Productos>();
        users.put("alanisawesome", new Productos("prod2","Producto1", "Descripcion producto 1",20, 21,true, "http://apprendiz.tk/imagenes/albacete_imagen1.png"));
        users.put("gracehop", new Productos("prod2", "Producto2", "Descripcion producto 2",20, 21,true, "http://apprendiz.tk/imagenes/albacete_imagen1.png"));

*/
    }

    public void cargarRecycler(){

        recycler= (RecyclerView) findViewById(R.id.recyclerProductos);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recycler.setAdapter(adapter);
        //guardarEnBaseDeDatos();
    }

    public void getList() {
/*
        if(list==null){
            list=new ArrayList<>();
        list.add(producto);
        list.add(new Productos("prod1","Producto1", "Descripcion producto 1",20, 21,true, "http://apprendiz.tk/imagenes/albacete_imagen1.png" ));
        list.add(new Productos("prod2", "Producto2", "Descripcion producto 1",20, 21,true, "http://apprendiz.tk/imagenes/albacete_imagen1.png" ));
    }*/
}

    public void setList(ArrayList<Productos> list) {
        this.list = list;
    }

    public void generarCompra(int posicion, View view){

        ArrayList<Compras> lcom=ListaCompras.getInstance().getListaComprasVirt();
        boolean actualizado=false;

        if(fec==null){
            fec= new GregorianCalendar();
        }
        String fecha=fec.get(Calendar.DAY_OF_MONTH) + "-" + (fec.get(Calendar.MONTH)+1) + "-" + fec.get(Calendar.YEAR);
        if(lcom.size()>0){
        for(Compras com:lcom){
            if(com.getNombre().equals(list.get(posicion).getNombre())){
                com.setCantidad(com.getCantidad() + 1);
                actualizado=true;
            }
        }}
        if(!actualizado){

        new Compras(list.get(posicion).getNombre(), "codcomp", 1, list.get(posicion).getCodigo(), "comprador", fecha, list.get(posicion).getImagenSmall(), list.get(posicion).getPrecio());}
        Toast.makeText(this, "El producto ha sido añadido correctamente", Toast.LENGTH_SHORT).show();
        Snackbar.make(view, "El producto ha sido añadido correctamente", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        //Toast.makeText(context, "El arrayList tiene como nombre de producto " + ListaCompras.getInstance().getListaComprasVirt().get(posicion).getNombre() , Toast.LENGTH_SHORT).show();
        
    }

}
