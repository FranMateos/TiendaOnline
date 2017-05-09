package com.example.personal.tiendaonline;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Main4Activity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;
    private String pathFoto;
    private Context context;
    private String nombreImagen;
    private EditText nombreProducto;
    private EditText descripcionProducto;
    private EditText precioProducto;
    private EditText cantidadProducto;
    private EditText imagenProducto;
    private String urlImagen="";
    private DatabaseReference myRef;
    private ArrayList<Productos> list;
    private String urlCompleta;
    private StorageReference strefer=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        myRef = FirebaseDatabase.getInstance().getReference("producto");

        nombreProducto = (EditText) findViewById(R.id.editText13);
        descripcionProducto = (EditText) findViewById(R.id.editText14);
        precioProducto = (EditText) findViewById(R.id.editText15);
        cantidadProducto = (EditText) findViewById(R.id.editText16);
        imagenProducto = (EditText) findViewById(R.id.imageView3);
        //referencia a la base de datos para manejar el evento escuchador de URL
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://tiendaonline-1a3b9.appspot.com");

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                HashMap val = (HashMap) dataSnapshot.getValue();

                if (list == null) {
                    list = new ArrayList<Productos>();
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

    public void guardarProducto(View view) {

        if (list.size() > 0) {
            int i = 0;
            FirebaseDatabase database = FirebaseDatabase.getInstance();

            String cad = "prod" + (list.size() + 1);

            DatabaseReference myRef2 = database.getReference("producto").child(cad);
            HashMap mapa2 = new HashMap();
            mapa2.put("nombre", nombreProducto.getText().toString());
            mapa2.put("descripcion", descripcionProducto.getText().toString());
            mapa2.put("disponible", true);
            mapa2.put("precio", precioProducto.getText().toString());
            mapa2.put("cantidad", cantidadProducto.getText().toString());
            mapa2.put("urlimagen", imagenProducto.getText().toString());
            mapa2.put("urlimagensmall", imagenProducto.getText().toString().replace("home","small"));
            myRef2.setValue(mapa2);
        }

        Snackbar.make(view, "El producto ha sido añadido correctamente", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

    }

    public void subida(){

        if (list.size() > 0) {
            int i = 0;
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            String cad = "prod" + (list.size() + 1);
        DatabaseReference myRef2 = database.getReference("producto").child(cad);
        HashMap mapa2 = new HashMap();
        mapa2.put("nombre", nombreProducto.getText().toString());
        mapa2.put("descripcion", descripcionProducto.getText().toString());
        mapa2.put("disponible", true);
        mapa2.put("precio", precioProducto.getText().toString());
        mapa2.put("cantidad", cantidadProducto.getText().toString());
        if(urlImagen.equals("")) {
            urlImagen = "http://apprendiz.tk/wp-content/uploads/2017/01/cropped-ImageLayer.png";
        }
        mapa2.put("urlimagen", urlImagen);
        myRef2.setValue(mapa2);}

    }

    public void sacarFoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
        File photofile=null;
        try{
            photofile=crearImagen();

        }catch(IOException ex){
            Log.d("ERROR","No creado");
        }
        if(photofile!=null){
            Uri photoURI= Uri.fromFile(photofile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            Toast.makeText(this, "Imagen creada satisfactoriamente", Toast.LENGTH_SHORT).show();

        }

        //subirImagenesLocal(photofile);
        setPic();

    }

    public File crearImagen() throws IOException {

        //DateFormat hora= DateFormat.getDateInstance(DateFormat.LONG, Locale.FRENCH);
        nombreImagen = new Date().getTime() +"";
        File directorio=getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imagen = File.createTempFile(nombreImagen, ".jpg", directorio);
        pathFoto=imagen.getAbsolutePath();
        return imagen;

    }

    /*public void galleryAddPic(){
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(pathFoto);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
        Toast.makeText(this, "El path de la foto es" + pathFoto,  Toast.LENGTH_LONG).show();
        Toast.makeText(this, "Imagen añadida satisfactoriamente", Toast.LENGTH_SHORT).show();
        subirImagenesLocal();
    }*/

    private void setPic(){
        View imagen =  findViewById(R.id.button6);
        int targetW = 640;
        int targetH = 640;
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds=true;
        BitmapFactory.decodeFile(pathFoto, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outWidth;

        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);
        bmOptions.inJustDecodeBounds=false;
        bmOptions.inSampleSize=scaleFactor;
        bmOptions.inPurgeable = true;
        Bitmap bitmap = BitmapFactory.decodeFile(pathFoto,bmOptions);
        //pasar imagen
        //imagen.setImageBitmap(bitmap);
        Toast.makeText(Main4Activity.this, "Imagen escalada satisfactoriamente", Toast.LENGTH_SHORT).show();
    }

    public void subirImagenesLocal() {

        File foto = new File(pathFoto);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://tiendaonline-1a3b9.appspot.com");
        urlCompleta=nombreImagen + ".jpg";
        StorageReference stref = storageRef.child(nombreImagen + ".jpg");
        UploadTask uploadTask = null;
        Uri file = Uri.fromFile(foto);
        uploadTask = stref.putFile(file);
        strefer = storageRef.child(urlCompleta);
// Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(getApplicationContext(), "Ha habido un problema en la subida, el ERROR es " + exception, Toast.LENGTH_SHORT).show();
                System.out.println("Ha habido un problema en la subida, el ERROR es " + exception);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                Toast t=Toast.makeText(Main4Activity.this, "La subida ha sido correcta", Toast.LENGTH_LONG);
                t.show();
                System.out.println("La subida ha sido correcta");
            }
        });

        /*stref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                urlImagen=uri.toString();
                System.out.println("Segun el escuchador la url es: " + urlImagen);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });*/



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == -1) {

            subirImagenesLocal();

        }

    }

    public void cancelar(View view){

        Intent intent=new Intent(context, Main3Activity.class);
        startActivity(intent);

    }

}
