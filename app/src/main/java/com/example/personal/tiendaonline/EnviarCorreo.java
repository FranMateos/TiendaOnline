package com.example.personal.tiendaonline;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class EnviarCorreo extends AppCompatActivity {

    private EditText nombre;
    private EditText correo;
    private EditText mensaje;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviar_correo);
        nombre= (EditText) findViewById(R.id.editText);
        correo= (EditText) findViewById(R.id.editText2);
        mensaje= (EditText) findViewById(R.id.editText3);
    }

    public void enviarCorreo(View view){

        String[] TO = {Tienda.getInstance().getCorreo()}; //Direcciones email  a enviar.

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, TO );
        //emailIntent.putExtra(Intent., TO);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, nombre.getText().toString() + " solicita ayuda...");
        emailIntent.putExtra(Intent.EXTRA_TEXT, mensaje.getText().toString() + "\n\n Su correo es " + correo.getText().toString());
        emailIntent.setType("message/rfc822");
        try {
            startActivity(Intent.createChooser(emailIntent, "Enviar email."));
            Log.i("EMAIL", "Enviando email...");
            Toast.makeText(this, "Enviando email...", Toast.LENGTH_SHORT).show();
        }
        catch (android.content.ActivityNotFoundException e) {
            Toast.makeText(this, "No existe ningún cliente de email instalado!.", Toast.LENGTH_SHORT).show();
        }

    }

    public void borradoDeCampos(View view){

        nombre.setText("");
        correo.setText("");
        mensaje.setText("");

    }

}
