package com.example.personal.tiendaonline;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity{

    private EditText usuarioUsuario;
    private EditText contraseniaUsuario;
    private EditText nombreUsuario;
    private EditText apellidosUsuario;
    private EditText correoUsuario;
    private EditText direccionUsuario;
    private EditText telefonoUsuario;
    private ArrayList<Usuario> list;
    private HashMap values;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        usuarioUsuario= (EditText) findViewById(R.id.editText18);
        contraseniaUsuario= (EditText) findViewById(R.id.editText19);
        nombreUsuario= (EditText) findViewById(R.id.editText20);
        apellidosUsuario= (EditText) findViewById(R.id.editText21);
        correoUsuario= (EditText) findViewById(R.id.editText22);
        direccionUsuario= (EditText) findViewById(R.id.editText23);
        telefonoUsuario= (EditText) findViewById(R.id.editText24);

        DatabaseReference myUserRef = FirebaseDatabase.getInstance().getReference("usuario");
        myUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                values = (HashMap) dataSnapshot.getValue();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        myUserRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                int i=1;
                HashMap val= (HashMap) dataSnapshot.getValue();
                /*Productos prod=new Productos();
                prod.cargarDatosProductosHashMap(val);*/
                //codigo, String nombre, String descripción, double precio, int cantidad, boolean disponible, String imagen
                Usuario.getInstance().cargarDatosUsuarioHashMap(val);

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


    }

    public void guardarUsuario(View view) {

        if(usuarioUsuario.getText().toString().equals("administrador") && contraseniaUsuario.getText().toString().equals("administrador")){
            if(contraseniaUsuario.getText().toString().equals("administrador")){
            MainActivity.registrado=true;
            Toast.makeText(this, "Administrador de Sistema logueado", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, Main3Activity.class);
            startActivity(intent);}else{
                Snackbar.make(view, "La contraseña no es correcta", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }else {
            int i = 0;
            FirebaseDatabase database = FirebaseDatabase.getInstance();

            String cad = "user" + (values.size() + 1);
            DatabaseReference myRef2 = database.getReference(usuarioUsuario.getText().toString()).child(cad);
            HashMap mapa2 = new HashMap();
            //mapa2.put("usuario", usuarioUsuario.getText().toString());
            mapa2.put("nombre", nombreUsuario.getText().toString());
            mapa2.put("contrasenia", contraseniaUsuario.getText().toString());
            mapa2.put("administrador", false);
            mapa2.put("apellidos", apellidosUsuario.getText().toString());
            mapa2.put("correo", correoUsuario.getText().toString());
            mapa2.put("direccion", direccionUsuario.getText().toString());
            mapa2.put("direccion", telefonoUsuario.getText().toString());
            myRef2.setValue(mapa2);
            Snackbar.make(view, "El usuario ha sido añadido correctamente", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

        }

        Snackbar.make(view, "El usuario ha sido añadido correctamente", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

    }

    public void cancelar(View view){

        Intent intent=new Intent(this, MainActivity.class);
        startActivity(intent);

    }

}

