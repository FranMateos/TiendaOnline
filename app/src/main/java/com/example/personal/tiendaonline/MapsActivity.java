package com.example.personal.tiendaonline;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Context context;
    private HashMap values;
    private Tienda tienda;
    private DatabaseReference myRef;
    private TextView nombre;
    private TextView direccion;
    private TextView correo;
    private TextView telefono;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        context=this;
        tienda=Tienda.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference("tienda");
        nombre= (TextView) findViewById(R.id.textView2);
        direccion= (TextView) findViewById(R.id.textView5);
        correo= (TextView) findViewById(R.id.textView7);
        telefono= (TextView) findViewById(R.id.textView6);
        metodosBaseDatos();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        correo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, EnviarCorreo.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng tiendaOnl = new LatLng(tienda.getLatitud(), tienda.getLongitud());
        mMap.addMarker(new MarkerOptions().position(tiendaOnl).title(tienda.getNombre()));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(tiendaOnl));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(18), 2000, null);

        //localizacion en google places https://www.google.es/maps/place/@lat,long

    }

    public void metodosBaseDatos(){

        //obtiene el nombre del TAG, en este caso tienda
        myRef.getKey();
        //referencias dinámicas
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            //cuando cambian los datos
            public void onDataChange(DataSnapshot dataSnapshot) {
                //obtiene una copia de los datos
                HashMap values = (HashMap) dataSnapshot.getValue();
                //list.add(values);
                if(tienda==null){
                tienda.cargarDatosTiendaHashMap(values);}
                nombre.setText(tienda.getNombre());
                direccion.setText(tienda.getDireccion());
                correo.setPaintFlags(correo.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                correo.setText(tienda.getCorreo());
                telefono.setText(tienda.getTelefono());
            }

            @Override
            //cuando se cancela la conexión por algún motivo
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, "ERROR de conexión con la base de Datos, entró en el onCancelled()", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
