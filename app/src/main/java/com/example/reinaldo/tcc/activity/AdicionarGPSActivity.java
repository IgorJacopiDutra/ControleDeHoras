package com.example.reinaldo.tcc.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reinaldo.tcc.R;
import com.example.reinaldo.tcc.model.Funcoes;
import com.example.reinaldo.tcc.model.GPS;
import com.example.reinaldo.tcc.model.Local;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.reinaldo.tcc.model.GPS.InserirGPSFirebase;

public class AdicionarGPSActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private TextView tvLatitude, tvLongitude, tvCidade, tvEstado, tvPais, tvCep;
    private Location location; // Devolver as coordenadas de GPS
    private LocationManager locationManager; // Qual é o provedor de serviço de Internet de localização disponível (GPS)
    private Address endereco; // Trazer os dados do endereço, rua, estado...
    private EditText edAddGps;
    private Spinner spn_locaisGPS;
    long numChildren;
    private GPS gpsAtual;

    double longitude = 0.0;
    double latitude = 0.0;
    GoogleApiClient mapGoogleApiClient;
    private Button btLocalizacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_gps);

        spn_locaisGPS = findViewById(R.id.spn_locaisGPS);
        edAddGps = findViewById(R.id.edAddGps);
        tvLatitude = (TextView) findViewById(R.id.tvLatitude);
        tvLongitude = (TextView) findViewById(R.id.tvLongitude);
        tvCidade = (TextView) findViewById(R.id.tvCidade);
        tvEstado = (TextView) findViewById(R.id.tvEstado);
        tvPais = (TextView) findViewById(R.id.tvPais);
        tvCep =(TextView) findViewById(R.id.tvCep);
        btLocalizacao = (Button) findViewById(R.id.btLocalizacao);

        numChildren = 0;
        // Recuperar Wifi
        gpsAtual = (GPS) getIntent().getSerializableExtra("gpsSelecionado");
        if ( gpsAtual != null ){
            edAddGps.setText(gpsAtual.getNomeGPS());
            numChildren = gpsAtual.getId();
        } else {
            DatabaseReference gpsTeste = Funcoes.pegarReferencia("GPS",getApplicationContext());
            Query pesquisa = gpsTeste.orderByChild("id").limitToFirst(1);

            pesquisa.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot objSnapshot:dataSnapshot.getChildren()){
                        GPS dadosGPS = objSnapshot.getValue(GPS.class);
                        //Toast.makeText(getApplicationContext(), String.valueOf(dadosUsuario.getId()) , Toast.LENGTH_LONG).show();
                        numChildren = dadosGPS.getId() + 1;
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        btLocalizacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (GetLocalization(AdicionarGPSActivity.this)) {
                    if (ActivityCompat.checkSelfPermission(AdicionarGPSActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(AdicionarGPSActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        return;
                    }
                    Location location = LocationServices.FusedLocationApi.getLastLocation(mapGoogleApiClient);
                    if (location != null) {
                        tvLatitude.setText(String.valueOf(location.getLatitude()));
                        tvLongitude.setText(String.valueOf(location.getLongitude()));

                        try{
                            endereco = buscarEndereco(location.getLatitude(),location.getLongitude());
                            tvCidade.setText(endereco.getLocality());
                            tvEstado.setText(endereco.getAdminArea());
                            tvPais.setText(endereco.getCountryName());
                            //tvBairro.setText(endereco.getSubLocality());
                            tvCep.setText(endereco.getPostalCode());
                            edAddGps.setText(endereco.getSubLocality());

                        } catch (IOException e){

                        }

                    } else {
                        showSettingsAlert();
                    }
                }
            }
        });

        // Fazer funcionar o Spinner de uma array-prestabelecida
        //ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
              //  R.array.locais_array, android.R.layout.simple_spinner_item);
        // Specify the activity_login to use when the list of choices appears
       // adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        //spn_locais.setAdapter(adapter);

        if (mapGoogleApiClient == null) {
            mapGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }




        Funcoes.pegarReferencia("LOCAL", getApplicationContext()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<String> locais = new ArrayList<String>();
                //        Local local =  dataSnapshot.getValue(Local.class);
                    for (DataSnapshot objSnapshot:dataSnapshot.getChildren()){
                        Local local =  objSnapshot.getValue(Local.class);
                        locais.add(local.getNomeLocal());
                        spn_locaisGPS = findViewById(R.id.spn_locaisGPS);
                        ArrayAdapter<String> locaisAdapter = new ArrayAdapter<String>(AdicionarGPSActivity.this, android.R.layout.simple_spinner_item, locais);
                        locaisAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spn_locaisGPS.setAdapter(locaisAdapter);

                    }

                }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        mapGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mapGoogleApiClient.isConnected()) {
            mapGoogleApiClient.disconnect();
        }
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

    public void showSettingsAlert(){
        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(AdicionarGPSActivity.this);

        // Titulo do dialogo
        alertDialog.setTitle("GPS");

        // Mensagem do dialogo
        alertDialog.setMessage("GPS não está habilitado. Deseja configurar?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Configurar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                AdicionarGPSActivity.this.startActivity(intent);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_adicionar_gps, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.itemSalvar:
                if ( (spn_locaisGPS.getSelectedItem() != null) && (edAddGps.getText().length() != 0) ){
                   SQLiteDatabase bancoDados = openOrCreateDatabase("app", MODE_PRIVATE, null); // tvLatitude.getText().toString() / tvLongitude.getText().toString()
                   if (InserirGPSFirebase(Long.toString(numChildren), edAddGps.getText().toString(), tvLatitude.getText().toString(), tvLongitude.getText().toString(), spn_locaisGPS.getSelectedItem().toString(), tvCidade.getText().toString(), tvEstado.getText().toString(), tvPais.getText().toString(), edAddGps.getText().toString(), tvCep.getText().toString() ,getApplicationContext())) {
                       Toast.makeText(AdicionarGPSActivity.this, "Salvo Com Sucesso!", Toast.LENGTH_LONG).show();
                       startActivity(new Intent(AdicionarGPSActivity.this, MainActivity.class));
                   } else {
                       Toast.makeText(AdicionarGPSActivity.this, "Erro Ao Salvar o GPS!", Toast.LENGTH_LONG).show();
                   }
                   finish();
                   break;
                } else {
                    Toast.makeText(AdicionarGPSActivity.this, "Por Favor, Preencha Todos os Campo.", Toast.LENGTH_LONG).show();
                }

        }

        return super.onOptionsItemSelected(item);
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

    public Address buscarEndereco(double latitude, double longitude)throws IOException{

        Geocoder geocoder;
        Address address = null;
        List<Address> addresses;

        geocoder = new Geocoder(getApplicationContext());

        addresses = geocoder.getFromLocation(latitude,longitude,1);

        if (addresses.size() >0){
            address = addresses.get(0);

        }

        return address;
    }

}
