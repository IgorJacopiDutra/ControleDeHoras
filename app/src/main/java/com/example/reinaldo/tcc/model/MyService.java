package com.example.reinaldo.tcc.model;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.example.reinaldo.tcc.R;
import com.example.reinaldo.tcc.activity.MainActivity;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import static com.example.reinaldo.tcc.model.Wifi.VerificaConexao;

/**
 * Created by Igor on 03/11/2018.
 */

public class MyService extends Service {

    Integer j = 0;
    Boolean bEncontrou = false;
    boolean resultado;
    String resString, resultadoString;
    Context contexto;
    Integer contador = 0;
    Date horasPrefUmDate, horasPrefDoisDate, horasPrefTresDate, horasPrefQuatroDate;
    Date data = new Date();
    String temp;
    Date temp2;
    Boolean ativoTabela;

    double latitude, longitude;
    Location location = null;

    @Override
    public void onCreate() {
        super.onCreate();

       // Toast.makeText(this, "onCreate", Toast.LENGTH_LONG).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
      //  Toast.makeText(this, "StartCommand", Toast.LENGTH_LONG).show();
        //validarGPS();
        new CountDownTimer(86400000, 60000) { //86400000 60000
            public void onTick(long millisUntilFinished) {

                bEncontrou = false;

                SQLiteDatabase bancoDados1 = openOrCreateDatabase("app", MODE_PRIVATE, null);
                Cursor cursor1 = bancoDados1.rawQuery("SELECT id, horasPrefUm, horasPrefDois, horasPrefTres, horasPrefQuatro, checkin FROM HORASPREFERNCIA", null);
                int horasPrefUm = cursor1.getColumnIndex("horasPrefUm");
                int horasPrefDois = cursor1.getColumnIndex("horasPrefDois");
                int horasPrefTres = cursor1.getColumnIndex("horasPrefTres");
                int horasPrefQuatro = cursor1.getColumnIndex("horasPrefQuatro");
                int checkin = cursor1.getColumnIndex("checkin");

                ativoTabela = false;
                if (cursor1 != null && cursor1.moveToFirst()) {
                    do {
                        try {
                            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                            String hora1 = cursor1.getString(horasPrefUm);
                            horasPrefUmDate = sdf.parse(hora1);
                            String hora2 = cursor1.getString(horasPrefDois);
                            horasPrefDoisDate = sdf.parse(hora2);
                            String hora3 = cursor1.getString(horasPrefTres);
                            horasPrefTresDate = sdf.parse(hora3);
                            String hora4 = cursor1.getString(horasPrefQuatro);
                            horasPrefQuatroDate = sdf.parse(hora4);
                            ativoTabela = true;
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    } while (cursor1.moveToNext());
                }

                try {
                    DateFormat formatDate = new SimpleDateFormat("HH:mm");
                    temp = formatDate.format(data);
                    temp2 = formatDate.parse(temp);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (ativoTabela.equals(true)) {

                    if ((temp2.after(horasPrefUmDate)) && (temp2.before(horasPrefQuatroDate))) {
                      //  Toast.makeText(getApplicationContext(), "onStartCountDownTimer " + j.toString(), Toast.LENGTH_LONG).show();
                        // Quantos CountDownTimer esta fazendo
                        j++;

                        // Aqui é a instância par apegar de fato os nomes de wifi's disponíveis
                        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                        wifiManager.getScanResults();
                        List<ScanResult> results = wifiManager.getScanResults();

                    /* caso a gente precise saber o looping dos wifi achados!!!!!!!!!!!!!!!!!!!!!!!!
                    for(int i = 0; i < results.size(); i++){
                        Toast.makeText(getApplicationContext(), "results.get(i) " + String.valueOf(i) + " k " + " De " + String.valueOf(results.size()) + " / " + results.get(i).SSID, Toast.LENGTH_LONG).show();
                        Log.d("WIFIMANAGER", "results.get(i) " + "results.get(i) " + String.valueOf(i) + " k " + " De " + String.valueOf(results.size()) + " / " + results.get(i).SSID );
                    }*/

                        // Aqui vamos buscar os Wifi salvos no SQLlite
                        SQLiteDatabase bancoDados = openOrCreateDatabase("app", MODE_PRIVATE, null);
                        Cursor cursor = bancoDados.rawQuery("SELECT id, nomeWifi, nomeWifiLocal FROM WIFIS", null);
                        //int indiceId = cursor.getColumnIndex("id");
                        int indiceNomeWifi = cursor.getColumnIndex("nomeWifi");
                        //int indiceWifiLocal = cursor.getColumnIndex("nomeWifiLocal");
                        if (cursor != null && cursor.moveToFirst()) {
                            do {
                                Log.d("CURSOR: ", cursor.getString(indiceNomeWifi));
                            } while (cursor.moveToNext());
                        }

                        // Vamos verificar primeiro (igual no adicionar WIFI) se o WIFI conectado se é algum cadastrado no SQLLITE
                        resultado = VerificaConexao(getApplicationContext());
                        contexto = getApplicationContext();
                        ConnectivityManager cm = (ConnectivityManager) contexto.getSystemService(Context.CONNECTIVITY_SERVICE); //Pego a conectividade do contexto o qual o metodo foi chamado
                        NetworkInfo netInfo = cm.getActiveNetworkInfo(); //Crio o objeto netInfo que recebe as informacoes da NEtwork
                        if (resultado) { // Caso precise ver o TRUE ou FALSE do resultado acima String.valueOf(resultado)
                            resultadoString = netInfo.getExtraInfo();
                            resString = resultadoString.replaceAll("\"", "");
                            //System.out.println(resultadoString);
                            if (cursor != null && cursor.moveToFirst()) {
                                do {
                                    // Passamos para variaveis apenas pra ficar mais facil a comparação no IF
                                    String a = cursor.getString(indiceNomeWifi).replaceAll("\\s+", "");
                                    String b = resString.replaceAll("\\s+", "");
                                    if (a.equals(b)) {
                                        Log.d("SQLLITECOMCONECTADO", "Este Wifi Encontrado, esta também cadastrado " + resString);
                                        bEncontrou = true;
                                    } else {
                                        Log.d("SQLLITECOMCONECTADO", "Este não é cadastrado no SQLLITE/ " + a + " / " + b);
                                    }
                                } while (cursor.moveToNext());
                            }

                        }

                        // Aqui vamos comparar os dois DIPONÍVEIS e os salvos no SQLITE
                        for (int i = 0; i < results.size(); i++) {
                            if (cursor != null && cursor.moveToFirst()) {
                                do {
                                    // Passamos para variaveis apenas pra ficar mais facil a comparação no IF
                                    String a = cursor.getString(indiceNomeWifi).replaceAll("\\s+", "");
                                    String b = results.get(i).SSID.replaceAll("\\s+", "");
                                    if (a.equals(b)) {
                                        Log.d("SQLLITECOMDISPONIVEIS", "Este Wifi Encontrado, esta também cadastrado " + results.get(i).SSID);
                                        bEncontrou = true;
                                    } else {
                                        Log.d("SQLLITECOMDISPONIVEIS", "Este não é cadastrado no SQLLITE/ " + cursor.getString(indiceNomeWifi) + " / " + results.get(i).SSID);
                                    }
                                } while (cursor.moveToNext());
                            }
                        }

                        if (j > 15) {
                            if (bEncontrou.equals(true)) {

                            }
                        }

                        if (bEncontrou.equals(false)) {
                            GerarNotificacao();
                        }
                    }

                }
            }

            public void onFinish() {
             //   Toast.makeText(getApplicationContext(), "onFinishCountDownTimer", Toast.LENGTH_LONG).show();
            }

        }.start();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void GerarNotificacao() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("Mensagem", "Deseja Gravar o Horário?");
        int id = (int) (Math.random() * 1000);
        PendingIntent pi = PendingIntent.getActivity(getBaseContext(), id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(getBaseContext())
                .setContentTitle("Bater ponto?")
                .setContentText("Desconectamos do seu gerenciador. Deseja bater seu ponto no horário de desconexão?").setSmallIcon(R.mipmap.ic_launcher_clock)
                .setContentIntent(pi).build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(id, notification);
        salvarHorasEncontrada();

    }

    public void salvarHorasEncontrada() {
        contador = contador + 1;
        //Date d =new Date(new Date().getTime()+28800000);
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        String strDate = dateFormat.format(date);
        SQLiteDatabase bancoDados = openOrCreateDatabase("app", MODE_PRIVATE, null);
        bancoDados.execSQL("CREATE TABLE IF NOT EXISTS HORASALVA (id INTEGER, hora VARCHAR)");

        //Cursor cursor =	bancoDados.rawQuery("SELECT id, hora FROM HORASALVA", null);
        //int indiceId = cursor.getColumnIndex("id");
        //int indiceHora = cursor.getColumnIndex("Hora");
        //if (cursor == null) {
        bancoDados.execSQL("DELETE FROM HORASALVA");
        bancoDados.execSQL("INSERT INTO HORASALVA (id, hora) VALUES ('" + contador + "','" + strDate + "')");
        //}
    }

    public void validarGPS() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        } else {

            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            if (gps) {
                if (locationManager == null) {

                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) this);
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                } else {
                    latitude = location.getLatitude();
                    latitude = location.getLongitude();
                }

            } else {
                Log.d("GPS", "GPS DESATIVADO");
            }

        }

    }
}
