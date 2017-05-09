package com.example.personal.tiendaonline;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Personal on 19/04/2017.
 */

public class ProductosAdapter extends RecyclerView.Adapter<ProductosAdapter.ProductosViewHolder> implements View.OnClickListener{

    private ArrayList<Productos> listado;
    private MainActivity mainActivity;
    private View.OnClickListener listener;

    public ProductosAdapter(ArrayList<Productos> listado, MainActivity mainActivity) {
        this.listado = listado;
        this.mainActivity=mainActivity;
    }

    @Override
    public void onClick(View v) {

        if(listener != null)
            listener.onClick(v);

    }

    public static class ProductosViewHolder extends RecyclerView.ViewHolder{

        public WebView imagen;
        public TextView titulo;
        public TextView descripcion;
        public TextView precio;

        public ProductosViewHolder(View itemView) {
            super(itemView);
            imagen = (WebView) itemView.findViewById(R.id.imagenProducto);
            int ancho=imagen.getWidth();
            titulo = (TextView) itemView.findViewById(R.id.nombreProducto);
            descripcion = (TextView) itemView.findViewById(R.id.descripcionProducto);
            precio = (TextView) itemView.findViewById(R.id.precioProducto);
        }
    }

    @Override
    public int getItemCount() {
        return listado.size();
    }

    @Override
    public ProductosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vista_productos, parent, false);
        v.setOnClickListener(this);
        return new ProductosViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ProductosViewHolder holder, int position) {

        holder.imagen.loadUrl(listado.get(position).getImagen());
        holder.titulo.setText(listado.get(position).getNombre());
        holder.descripcion.setText(listado.get(position).getDescripción());
        holder.precio.setText(listado.get(position).getPrecio() + " €");
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }



}
