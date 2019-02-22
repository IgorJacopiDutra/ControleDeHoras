package com.example.reinaldo.tcc.fragments;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reinaldo.tcc.R;
import com.example.reinaldo.tcc.model.Funcoes;
import com.example.reinaldo.tcc.model.Horas;
import com.example.reinaldo.tcc.model.MyService;
import com.example.reinaldo.tcc.model.Wifi;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;
import static com.example.reinaldo.tcc.model.Horas.marcarHorasFirebase;

/**
 * A simple {@link Fragment} subclass.
 */
public class PrincipalFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private TextClock txtClock;
    private Button btnMarcar, btnGravar;
    private String hora;
    private TextView tvHoraUm, tvHoraDois, tvHoraTres, tvHoraQuatro, tvDataAtual, tvInformacoes;

    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String TVHORAUM = "tvHoraUm";
    private static final String TVHORADOIS = "tvHoraDois";
    private static final String TVHORATRES = "tvHoraTres";
    private static final String TVHORAQUATRO = "tvHoraQuatro";

    private String horaUm;
    private String horaDois;
    private String horaTres;
    private String horaQuatro;

    GoogleApiClient mapGoogleApiClient1;
    private Address endereco1;
    private TextView gps;
    private TextView bairro;
    private DatabaseReference UserRef;

    private String lat;
    private String longi;

    public PrincipalFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.content_main, container, false);

        UserRef = //Funcoes.pegarReferencia("GPS", getContext());
                FirebaseDatabase.getInstance().getReference().child("GPS").child(Funcoes.pegarKey(getContext())).child("0");
        gps = view.findViewById(R.id.gps);
        bairro = view.findViewById(R.id.lat);

        //gps.setVisibility(View.INVISIBLE);
        //bairro.setVisibility(View.INVISIBLE);

        Button btnMarcar = view.findViewById(R.id.btnMarcar);

        Button btnGravar = view.findViewById(R.id.btnGravar);

        btnGravar.setVisibility(View.VISIBLE);

        btnMarcar.setVisibility(View.VISIBLE);

        txtClock = view.findViewById(R.id.txtClock);

        tvHoraUm = view.findViewById(R.id.tvHoraUm);
        tvHoraDois = view.findViewById(R.id.tvHoraDois);
        tvHoraTres = view.findViewById(R.id.tvHoraTres);
        tvHoraQuatro = view.findViewById(R.id.tvHoraQuatro);
        tvDataAtual = view.findViewById(R.id.tvDataAtual);

        tvInformacoes = view.findViewById(R.id.tvInformacoes);

        if (mapGoogleApiClient1 == null) {
            mapGoogleApiClient1 = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }



        SQLiteDatabase bancoDados1 = getActivity().openOrCreateDatabase("app", MODE_PRIVATE, null);
        Cursor cursor1 = bancoDados1.rawQuery("SELECT id, horasPrefUm, horasPrefDois, horasPrefTres, horasPrefQuatro, checkin FROM HORASPREFERNCIA", null);
        int horasPrefUm = cursor1.getColumnIndex("horasPrefUm");
        int horasPrefDois = cursor1.getColumnIndex("horasPrefDois");
        int horasPrefTres = cursor1.getColumnIndex("horasPrefTres");
        int horasPrefQuatro = cursor1.getColumnIndex("horasPrefQuatro");
        int checkin = cursor1.getColumnIndex("checkin");

        if (cursor1 != null && cursor1.moveToFirst()) {
            do {
                tvInformacoes.setText("Das " + cursor1.getString(horasPrefUm) + " às " + cursor1.getString(horasPrefDois) + " e " + cursor1.getString(horasPrefTres) + " às " + cursor1.getString(horasPrefQuatro) +  "\n" + "Controlado por: " + cursor1.getString(checkin));
            } while (cursor1.moveToNext());
        }

        //String currentDateTimeString = DateFormat.getDateInstance().format(new Date());
        tvDataAtual.setText(getCurrentTimeStamp());

        btnMarcar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                capturarHora();
                salvarDadosNaTela();

                //pegarGPS();

            }
        });



        btnGravar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                gravarHora();
                salvarDadosNaTela();


            }
        });

        carregarDados();
        atualizarViews();


        return (view);
    }

    public void capturarHora() {

        hora = txtClock.getText().toString();

        if (tvHoraUm.getText().toString().equals("--:--")) {
            tvHoraUm.setText(hora);
        } else if (tvHoraDois.getText().toString().equals("--:--")) {
            tvHoraDois.setText(hora);
        } else if (tvHoraTres.getText().toString().equals("--:--")){
            tvHoraTres.setText(hora);
        } else  if (tvHoraQuatro.getText().toString().equals("--:--")) {
            tvHoraQuatro.setText(hora);

        }

    }



    public static String getCurrentTimeStamp() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("EEE, d MMMM yyyy");
        Date now = new Date();
        String strDate = sdfDate.format(now);
        return strDate;
    }

    //gravar temporariamente horas na tela
    public void salvarDadosNaTela(){
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(TVHORAUM, tvHoraUm.getText().toString());
        editor.putString(TVHORADOIS, tvHoraDois.getText().toString());
        editor.putString(TVHORATRES, tvHoraTres.getText().toString());
        editor.putString(TVHORAQUATRO, tvHoraQuatro.getText().toString());

        editor.apply();

    }

    public void carregarDados(){
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        horaUm = sharedPreferences.getString(TVHORAUM, "--:--");
        horaDois = sharedPreferences.getString(TVHORADOIS, "--:--");
        horaTres = sharedPreferences.getString(TVHORATRES, "--:--");
        horaQuatro = sharedPreferences.getString(TVHORAQUATRO, "--:--");
    }

    public void atualizarViews(){

        tvHoraUm.setText(horaUm);
        tvHoraDois.setText(horaDois);
        tvHoraTres.setText(horaTres);
        tvHoraQuatro.setText(horaQuatro);
    }

    public void gravarHora(){

        if (marcarHorasFirebase(tvDataAtual.getText().toString(),
                tvHoraUm.getText().toString(),
                tvHoraDois.getText().toString(),
                tvHoraTres.getText().toString(),
                tvHoraQuatro.getText().toString(),
                getContext())){
            Toast.makeText(getContext(), "Horas salvas com sucesso! Espero que seu dia de trabalho tenha sido bom!", Toast.LENGTH_LONG).show();
            tvHoraUm.setText("--:--");
            tvHoraDois.setText("--:--");
            tvHoraTres.setText("--:--");
            tvHoraQuatro.setText("--:--");
        } else {
            Toast.makeText(getContext(), "Problemas em salvar suas horas, tente novamente!", Toast.LENGTH_LONG).show();
        }


    }

    public void capturarHoraGPS(String indiceHora){

        AlertDialog.Builder builder = new AlertDialog.Builder(
                getActivity());

        builder.setIcon(getResources().getDrawable(
                R.mipmap.ic_launcher_clock));

        builder.setTitle("Horário");

        builder.setMessage("Você ainda esta no local de trabalho? Podemos bater o ponto para você as: " + indiceHora);

        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                UserRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {

                            if (dataSnapshot.hasChild("latitude")) {
                                lat = dataSnapshot.child("latitude").getValue().toString();

                            }

                            if (dataSnapshot.hasChild("longitude")) {
                                longi = dataSnapshot.child("longitude").getValue().toString();
                                //bairro.setText(longitude);

                            } else {
                                Toast.makeText(getContext(), "Erro...", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                //pegarGPS();

                if (gps.getText().toString()==lat.toString() && bairro.getText().toString()==longi.toString() ) {

                    if (tvHoraUm.getText().toString().equals("--:--")) {
                        tvHoraUm.setText(hora);
                    } else if (tvHoraDois.getText().toString().equals("--:--")) {
                        tvHoraDois.setText(hora);
                    } else if (tvHoraTres.getText().toString().equals("--:--")) {
                        tvHoraTres.setText(hora);
                    } else if (tvHoraQuatro.getText().toString().equals("--:--")) {
                        tvHoraQuatro.setText(hora);

                    }

                }

            }

        });

        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {



            }
        });

        //builder.setCancelable(false);
        AlertDialog alert = builder.create();
        alert.show();

    }

    public void capturarHoraAutomatica(String indiceHora) {

        AlertDialog.Builder builder = new AlertDialog.Builder(
                getActivity());

        builder.setIcon(getResources().getDrawable(
                R.mipmap.ic_launcher_clock));

        builder.setTitle("Horário");
       /* DateFormat dateFormat2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        //String strDate = dateFormat.format(indiceHora);
        Locale BRAZIL = new Locale("pt","BR");
        dateFormat2 = DateFormat.getDateInstance(DateFormat.FULL, BRAZIL);
        indiceHora = dateFormat2.format(indiceHora);*/

        builder.setMessage("Você ainda esta no local de trabalho? Podemos bater o ponto para você as: " + indiceHora);

        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                SQLiteDatabase bancoDados3 = getActivity().openOrCreateDatabase("app", MODE_PRIVATE, null);
                bancoDados3.execSQL("CREATE TABLE IF NOT EXISTS HORASALVA (id INTEGER, hora VARCHAR)");
                Cursor cursor3 =	bancoDados3.rawQuery("SELECT id, hora FROM HORASALVA", null);
                int indiceId = cursor3.getColumnIndex("id");
                int indiceHora = cursor3.getColumnIndex("hora");
                if (cursor3 != null && cursor3.moveToFirst()) {
                    do {
                        SimpleDateFormat formatDate = new SimpleDateFormat("HH:mm");
                        //String strDate = ;
                        //Locale BRAZIL = new Locale("pt","BR");
                        //dateFormat = DateFormat.getDateInstance(DateFormat.FULL, BRAZIL);
                        hora = cursor3.getString(indiceHora);
                    } while (cursor3.moveToNext());
                }

                if (tvHoraUm.getText().toString().equals("--:--")) {
                    tvHoraUm.setText(hora);
                } else if (tvHoraDois.getText().toString().equals("--:--")) {
                    tvHoraDois.setText(hora);
                } else if (tvHoraTres.getText().toString().equals("--:--")){
                    tvHoraTres.setText(hora);
                } else  if (tvHoraQuatro.getText().toString().equals("--:--")) {
                    tvHoraQuatro.setText(hora);

                }
                salvarDadosNaTela();
                bancoDados3.execSQL("DELETE FROM HORASALVA");
            }
        });
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {



            }
        });

        //builder.setCancelable(false);
        AlertDialog alert = builder.create();
        alert.show();

    }

    public void pegarGPS(){

        if (GetLocalization(getContext())) {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                return;
            }
            Location location = LocationServices.FusedLocationApi.getLastLocation(mapGoogleApiClient1);
            if (location != null) {
                //Toast.makeText(getContext(), "lat: "+ String.valueOf(location.getLatitude()) + ", long: "+String.valueOf(location.getLongitude()), Toast.LENGTH_LONG).show();
                gps.setText(String.valueOf(location.getLatitude()));
                bairro.setText(String.valueOf(location.getLongitude()));

                try{
                    endereco1 = buscarEndereco(location.getLatitude(),location.getLongitude());
                    //tvCidade.setText(endereco1.getLocality());
                    //tvEstado.setText(endereco1.getAdminArea());
                    //tvPais.setText(endereco1.getCountryName());
                    bairro.setText(endereco1.getSubLocality());
                    //tvCep.setText(endereco1.getPostalCode());


                    //bairro.setText(endereco1.getSubLocality());

                } catch (IOException e){

                }

            } else {
                showSettingsAlert();
            }
        }
    }

    public void showSettingsAlert(){
        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(getContext());

        // Titulo do dialogo
        alertDialog.setTitle("GPS");

        // Mensagem do dialogo
        alertDialog.setMessage("GPS não está habilitado. Deseja configurar?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Configurar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                getContext().startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // visualizacao do dialogo
        alertDialog.show();
    }

    public boolean GetLocalization(Context context){
        int REQUEST_PERMISSION_LOCALIZATION = 221;
        boolean res=true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                res = false;
                ActivityCompat.requestPermissions((Activity) context, new String[]{
                                Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_PERMISSION_LOCALIZATION);

            }
        }
        return res;
    }

    public Address buscarEndereco(double latitude, double longitude)throws IOException{

        Geocoder geocoder;
        Address address = null;
        List<Address> addresses;

        geocoder = new Geocoder(getContext());

        addresses = geocoder.getFromLocation(latitude,longitude,1);

        if (addresses.size() >0){
            address = addresses.get(0);

        }

        return address;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onResume() {
        super.onResume();
        mapGoogleApiClient1.connect();

        pegarGPS();
        // Preciso fazer isso depois do onCreate
        // Essa HORASALVA é para saber se o Service salvou alguma hora por ter ficado fora da hora limite
        SQLiteDatabase bancoDados2 = getActivity().openOrCreateDatabase("app", MODE_PRIVATE, null);
        bancoDados2.execSQL("CREATE TABLE IF NOT EXISTS HORASALVA (id INTEGER, hora VARCHAR)");
        Cursor cursor2 =	bancoDados2.rawQuery("SELECT id, hora FROM HORASALVA", null);
        int indiceId = cursor2.getColumnIndex("id");
        int indiceHora = cursor2.getColumnIndex("hora");
        if (cursor2 != null && cursor2.moveToFirst()) {
            do {
                SimpleDateFormat formatDate = new SimpleDateFormat("HH:mm");

             /*   Date date = null;
                try {
                    date = formatDate.parse(cursor2.getString(indiceHora));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                // tranformar cursor2.getString(indiceHora) em DATA
                if (date != null) {
                    capturarHoraAutomatica(formatDate.format(date));
                }
                */
                capturarHoraAutomatica(cursor2.getString(indiceHora));
            } while (cursor2.moveToNext());
        }
        //bancoDados2.execSQL("DELETE FROM HORASALVA");
    }

    @Override
    public void onPause() {
        super.onPause();
    }


}
