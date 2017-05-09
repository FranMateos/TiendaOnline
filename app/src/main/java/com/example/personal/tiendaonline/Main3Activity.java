package com.example.personal.tiendaonline;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class Main3Activity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE=1;
    static final int REQUEST_TAKE_PHOTO=1;
    private String pathFoto;
    private Context context;
    private String nombreImagen;
    private EditText nombreTienda;
    private EditText correoTienda;
    private EditText direccionTienda;
    private EditText telefonoTienda;
    private EditText latitudTienda;
    private EditText longitudTienda;
    private EditText imagenTienda;
    private ImageView aniadirProducto;
    private String urlImagenTienda;
    private String urlImagen;
    FirebaseStorage storage;
    StorageReference storageRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        context=this.getApplicationContext();

        storage=FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://tiendaonline-1a3b9.appspot.com");

        nombreTienda= (EditText) findViewById(R.id.editText6);
        correoTienda= (EditText) findViewById(R.id.editText7);
        direccionTienda= (EditText) findViewById(R.id.editText8);
        telefonoTienda= (EditText) findViewById(R.id.editText9);
        latitudTienda= (EditText) findViewById(R.id.editText11);
        longitudTienda= (EditText) findViewById(R.id.editText12);
        imagenTienda= (EditText) findViewById(R.id.imageView3);
        aniadirProducto= (ImageView) findViewById(R.id.imageView2);

        nombreTienda.setText(Tienda.getInstance().getNombre());
        correoTienda.setText(Tienda.getInstance().getCorreo());
        direccionTienda.setText(Tienda.getInstance().getDireccion());
        telefonoTienda.setText(Tienda.getInstance().getTelefono());
        latitudTienda.setText(Tienda.getInstance().getLatitud() + "");
        longitudTienda.setText(Tienda.getInstance().getLongitud()+"");
        imagenTienda.setText(Tienda.getInstance().getImagen());
        urlImagen=Tienda.getInstance().getImagen();
        
        imagenTienda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sacarFoto();
                /*Toast.makeText(context, "Ha sido pulsado en imagen", Toast.LENGTH_SHORT).show();
                System.out.println("Has pulsado en imagen");
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, 1020);*/
            }
        });
        
        aniadirProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(context, Main4Activity.class);
                startActivity(intent);
                Toast.makeText(context, "Ahora pasamos a añadir productos", Toast.LENGTH_SHORT).show();
                System.out.println("Has pulsado en productos");
                Snackbar.make(v, "Ahora pasamos a añadir productos", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    public void sacarFoto() {
        /*Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }*/
        Toast.makeText(context, "Ha sido pulsado en imagen", Toast.LENGTH_SHORT).show();
        System.out.println("Has pulsado en imagen");
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, 1020);
        File photofile=null;
        try{
            photofile=crearImagen();

        }catch(IOException ex){
            Log.d("ERROR","No creado");
        }
        if(photofile!=null){
            Uri photoURI= Uri.fromFile(photofile);
            galleryIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(galleryIntent, 1020);
        }

        //subirImagenesLocal(photofile);

    }

    public File crearImagen() throws IOException {

        //DateFormat hora= DateFormat.getDateInstance(DateFormat.LONG, Locale.FRENCH);
        nombreImagen = new Date().getTime() +"";
        File directorio=getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imagen = File.createTempFile(nombreImagen, ".jpg", directorio);
        pathFoto=imagen.getAbsolutePath();
        return imagen;

    }

    public void galleryAddPic(){
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(pathFoto);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
        Toast.makeText(this, "El path de la foto es" + pathFoto,  Toast.LENGTH_LONG).show();
        Toast.makeText(this, "Imagen añadida satisfactoriamente", Toast.LENGTH_SHORT).show();
        subirImagenesLocal();
    }

    private void setPic(){
        View imagen =  findViewById(R.id.button6);
        int targetW = imagen.getWidth();
        int targetH = imagen.getWidth();
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
        Toast.makeText(this, "Imagen escalada satisfactoriamente", Toast.LENGTH_SHORT).show();
    }

    public void subirImagenesLocal(){

        File foto = new File(pathFoto);
        FirebaseStorage storage=FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://tiendaonline-1a3b9.appspot.com");
        StorageReference stref=storageRef.child(nombreImagen + ".jpg");
        UploadTask uploadTask=null;
        Uri file =Uri.fromFile(foto);
        uploadTask = stref.putFile(file);

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
                Toast.makeText(getApplicationContext(), "La subida ha sido correcta", Toast.LENGTH_LONG).show();
                System.out.println("La subida ha sido correcta");
            }
        });

    }

    public void subirImagenesLocalGaleria(File foto){

        //File foto = new File(pathFoto);
        FirebaseStorage storage=FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://tiendaonline-1a3b9.appspot.com");
        StorageReference stref=storageRef.child(nombreImagen + ".jpg");
        UploadTask uploadTask=null;
        Uri file =Uri.fromFile(foto);
        System.out.println("La url de la imagen local es: " + file);
        uploadTask = stref.putFile(file);
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
                Toast.makeText(getApplicationContext(), "La subida ha sido correcta", Toast.LENGTH_LONG).show();
                System.out.println("La subida ha sido correcta");
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            if(data != null){
                subirImagenesLocal();
            }
        }
        if (requestCode == 1020) {
            if (resultCode ==  RESULT_OK) {
                Bundle extras = data.getExtras();
                Uri selectedImage = data.getData();
                File file=new File(String.valueOf(selectedImage));
                subirImagenesLocalGaleria(file);
            }
        }

    }

    public void guardarEnBaseDeDatos(View view){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("tienda");
        //se genera un nuevo elemento
        HashMap mapa=new HashMap();
        mapa.put("correo",correoTienda.getText().toString());
        mapa.put("direccion",direccionTienda.getText().toString());
        mapa.put("latitud",Double.parseDouble(latitudTienda.getText().toString()));
        mapa.put("longitud",Double.parseDouble(longitudTienda.getText().toString()));
        mapa.put("nombre",nombreTienda.getText().toString());
        mapa.put("telefono",telefonoTienda.getText().toString());
        mapa.put("imagen", imagenTienda.getText().toString());
        myRef.setValue(mapa);
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        Toast.makeText(view.getContext(), "Información actualizada correctamente", Toast.LENGTH_SHORT).show();
        Snackbar.make(view, "Información actualizada correctamente", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

    }

    public void cancelar(View view){

        Intent intent=new Intent(context, MainActivity.class);
        startActivity(intent);

    }

}
