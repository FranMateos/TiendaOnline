package com.example.personal.tiendaonline.objetos;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.personal.tiendaonline.Compras;
import com.example.personal.tiendaonline.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Personal on 04/05/2017.
 */


public class ComprasAdapter extends ArrayAdapter {

    private Context ctx;
    private ArrayList<Compras> data;

    public ComprasAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<Compras> objects) {
        super(context, resource, objects);
        this.ctx=context;
        this.data=objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater=LayoutInflater.from(ctx);
        View row=inflater.inflate(R.layout.compra_row, null);
        //inflar elementos
        WebView wb= (WebView) row.findViewById(R.id.webView2);
        TextView tv= (TextView) row.findViewById(R.id.textView3);
        EditText et= (EditText) row.findViewById(R.id.editText4);
        TextView tvw= (TextView) row.findViewById(R.id.textView4);
        wb.loadUrl(data.get(position).getImagen());
        tv.setText(data.get(position).getNombre());
        et.setText(data.get(position).getCantidad() + "");
        tvw.setText(data.get(position).getPrecio() + "€");
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Toast.makeText(ctx, "Ha sido modificada la cantidad", Toast.LENGTH_SHORT).show();
                for(Compras da:data){
                    da.setCantidad(12);
                }
            }
        });

        return row;
    }
}
