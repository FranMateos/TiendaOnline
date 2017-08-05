package com.example.personal.tiendaonline.Tareas;

import android.util.Log;
import android.widget.Toast;

import com.example.personal.tiendaonline.MainActivity;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Personal on 05/08/2017.
 */

public class Temporizador {

    private static final long ONE_MINUTE_IN_MILLIS=60000;//millisecs
    private static Timer tim;

    public static void iniciarTareaDaemon(MainActivity mainActivity, int minutos){

        // Date date = new Date();
        // el demonio se sigue ejecutando en segundo plano hasta que finalices la actividad
        Calendar date = Calendar.getInstance();
        long t= date.getTimeInMillis();
        Date aniadirMinutos=new Date(t + (minutos * ONE_MINUTE_IN_MILLIS));
        Toast.makeText(mainActivity, "Entra en iniciar Daemon", Toast.LENGTH_SHORT).show();
        tim = new Timer(true);
        tim.schedule(lanzarTarea(), aniadirMinutos);

    }

    public static void detenerTareaDaemon(){

        tim.cancel();

    }

    public static TimerTask lanzarTarea() {

        // Toast.makeText(mainActivity, "Entra a lanzarTarea", Toast.LENGTH_SHORT).show();
        Log.d("Lanzar Tarea", "Lanzar tarea iniciado");
        TimerTask tt = new TimerTask() {
            int i=0;
            @Override
            public void run() {
                while (true){
                    // Toast.makeText(context, "Demonio inside!!!", Toast.LENGTH_SHORT).show();
                    Log.d("Demonio", "Demonio Inside "+ (i++) + "!!!");
                    try {
                        Thread.sleep(1000);
                        i++;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        break;
                    }}
            }
        };
        return tt;

    }


}
