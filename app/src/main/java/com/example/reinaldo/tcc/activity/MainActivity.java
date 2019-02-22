package com.example.reinaldo.tcc.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reinaldo.tcc.R;
import com.example.reinaldo.tcc.fragments.ContaFragment;
import com.example.reinaldo.tcc.fragments.GpsFragment;
import com.example.reinaldo.tcc.fragments.LocalFragment;
import com.example.reinaldo.tcc.fragments.PrincipalFragment;
import com.example.reinaldo.tcc.fragments.RelatorioFragment;
import com.example.reinaldo.tcc.fragments.WifiFragment;
import com.example.reinaldo.tcc.model.Funcoes;
import com.example.reinaldo.tcc.model.GPS;
import com.example.reinaldo.tcc.model.MyService;
import com.example.reinaldo.tcc.model.Preferencias;
import com.example.reinaldo.tcc.model.Wifi;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.support.v4.app.ActivityCompat.startActivityForResult;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private FrameLayout frameLayout;
    private FloatingActionButton fabPrincipal;
    private FloatingActionButton fabLocal;
    private FloatingActionButton fabWifi;
    private FloatingActionButton fabGPS;
    private MenuItem nav_conta;
    private FirebaseAuth usuarioAtual = FirebaseAuth.getInstance();
    private DatabaseReference wifiRef;
    private DatabaseReference gpsRef;
    private DatabaseReference preferenciaRef;
    private List<Wifi> listaWifi = new ArrayList<>();
    private List<GPS> listaGPS = new ArrayList<>();
    private List<Preferencias> listaPreferencias = new ArrayList<>();

    private CircleImageView profile_image;
    private TextView tvEmailHeader;
    private FirebaseAuth mAuth;
    private DatabaseReference UserRef;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        UserRef = FirebaseDatabase.getInstance().getReference().child("IMAGEM_PROFILE").child(Funcoes.pegarKey(getApplicationContext()));

        fabPrincipal = findViewById(R.id.fabPrincipal);
        fabLocal = findViewById(R.id.fabLocal);
        fabWifi = findViewById(R.id.fabWifi);
        fabGPS = findViewById(R.id.fabGps);
        nav_conta = findViewById(R.id.nav_conta);
        listaWifi.clear();
        listaGPS.clear();
        final String CoarseLocation = Manifest.permission.ACCESS_COARSE_LOCATION;
        final String AccessWifi = Manifest.permission.ACCESS_WIFI_STATE;
        final String ChangeWifi = Manifest.permission.CHANGE_WIFI_STATE;
        final SQLiteDatabase bancoDados = openOrCreateDatabase("app", MODE_PRIVATE, null);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        // Verifica a função antes de ir buscar no Firebase para não dar erro ou perder no desempnho (Firebase precisa de internet)
        if (Funcoes.verificaConexao(getApplicationContext())) {

            // Vamos buscar os WIFIS Salvos e ATUALIZAR O SQLLITE
            try {
                bancoDados.execSQL("CREATE TABLE IF NOT EXISTS WIFIS (id INTEGER, nomeWifi VARCHAR, nomeWifiLocal VARCHAR)");
                wifiRef = Funcoes.pegarReferencia("WIFI", getApplicationContext());
                wifiRef.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Wifi wifi = dataSnapshot.getValue(Wifi.class);
                        listaWifi.add(wifi); // nomeWifi, nomeWifiLocal
                        bancoDados.execSQL("DELETE FROM WIFIS");
                        for (Wifi wifilista : listaWifi) {
                            bancoDados.execSQL("INSERT INTO WIFIS (id, nomeWifi, nomeWifiLocal) VALUES ('" + wifilista.getId() + "','" + wifilista.getNomeWifi() + "','" + wifilista.getNomeWifiLocal() + "')");
                        }
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

            } catch (Exception e) {
                e.printStackTrace();

            }

            // Vamos buscar os GPPS Salvos e ATUALIZAR O SQLLITE


            // Vamos buscar os horário de preferencia e qual checkin que ele utilizará para usar no Service
            try{
                // AINDA NÃO SEI SE É BOM FAZER ISSO TODA VEZ
                //bancoDados.execSQL("DROP TABLE HORASPREFERNCIA");
                bancoDados.execSQL("CREATE TABLE IF NOT EXISTS HORASPREFERNCIA (id INTEGER, horasPrefUm VARCHAR, horasPrefDois VARCHAR, horasPrefTres VARCHAR, horasPrefQuatro VARCHAR, checkin VARCHAR)");
                preferenciaRef = Funcoes.pegarReferencia("PREFERENCIAS",getApplicationContext());
                preferenciaRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        /*Preferencias preferencias = new Preferencias();
                        listaPreferencias.add(preferencias);
                        bancoDados.execSQL("DELETE FROM HORASPREFERNCIA");*/
                        Preferencias dadosPreferencia = dataSnapshot.getValue(Preferencias.class);
                        bancoDados.execSQL("DELETE FROM HORASPREFERNCIA");
                        bancoDados.execSQL("INSERT INTO HORASPREFERNCIA (id, horasPrefUm, horasPrefDois, horasPrefTres, horasPrefQuatro, checkin) VALUES ('" + 1 + "','" + dadosPreferencia.getHorasPrefUm() + "','" + dadosPreferencia.getHorasPrefDois() + "','" + dadosPreferencia.getHorasPrefTres() + "','" + dadosPreferencia.getHorasPrefQuatro() + "','" + dadosPreferencia.getCheckin() +"')");
                        /*for (final DataSnapshot snapshot : dataSnapshot.getChildren()){
                            Preferencias dadosUsuario = snapshot.getValue(Preferencias.class);

                            bancoDados.execSQL("INSERT INTO HORASPREFERNCIA (id, horasPrefUm, horasPrefDois, horasPrefTres, horasPrefQuatro) VALUES ('" + 1 + "','" + dadosUsuario.getHorasPrefUm() + "','" + dadosUsuario.getHorasPrefDois() + "','" + dadosUsuario.getHorasPrefTres() + "','" + dadosUsuario.getHorasPrefQuatro() + "')");
                        }*/
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }); /*
                preferenciaRef.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Preferencias preferencias = new Preferencias();
                        listaPreferencias.add(preferencias);
                        bancoDados.execSQL("DELETE FROM HORASPREFERNCIA");
                        for (Preferencias preferenciasLista : listaPreferencias) {
                            bancoDados.execSQL("INSERT INTO HORASPREFERNCIA (id, horasPrefUm, horasPrefDois, horasPrefTres, horasPrefQuatro) VALUES ('" + 1 + "','" + preferenciasLista.getHorasPrefUm() + "','" + preferenciasLista.getHorasPrefDois() + "','" + preferenciasLista.getHorasPrefTres() + "','" + preferenciasLista.getHorasPrefQuatro() + "')");
                        }
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
                });*/
                //bancoDados.execSQL("DELETE FROM GPSS");
            } catch (Exception e){
                e.printStackTrace();
            }


        } else {
            Toast.makeText(getApplicationContext(), "Conecte-se a Internet, para atualizar os dados. ", Toast.LENGTH_SHORT).show();
        }

        // VAMOS BUSCAR TODAS AS PERMISSÕES, LOGO QUANDO INICIAR O PROGRAMA PARA NÃO TER PROBLEMAS NO SERVIÇO BACKGROUND E NO ADICIONAR
        if (ContextCompat.checkSelfPermission(getApplicationContext(), CoarseLocation) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 123);
        }

        if (ContextCompat.checkSelfPermission(getApplicationContext(), AccessWifi) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.ACCESS_WIFI_STATE}, 123);
        }

        if (ContextCompat.checkSelfPermission(getApplicationContext(), ChangeWifi) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CHANGE_WIFI_STATE, Manifest.permission.CHANGE_WIFI_STATE}, 123);
        }
        LocationManager lman = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        boolean network_enabled = false;
        try {
            network_enabled = lman.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }
        if (!network_enabled) {
            startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
        }
        // FIM DE PERMISSÕES DE ACESSOS

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        PrincipalFragment principalFragment = new PrincipalFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.containerFrame, principalFragment);
        fragmentTransaction.commit();

        View navView = navigationView.inflateHeaderView(R.layout.nav_header_main);
        profile_image = (CircleImageView) navView.findViewById(R.id.profile_image);
        tvEmailHeader = (TextView) navView.findViewById(R.id.tvEmailHeader);


        UserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    if(dataSnapshot.hasChild("username"))
                    {
                        String username = dataSnapshot.child("username").getValue().toString();
                        tvEmailHeader.setText(username);
                    }

                    if (dataSnapshot.hasChild("profileimage")) {
                        String image = dataSnapshot.child("profileimage").getValue().toString();
                        Picasso.with(MainActivity.this).load(image).placeholder(R.drawable.capaprofile).into(profile_image);

                    } else {
                        Toast.makeText(MainActivity.this, "Nome de Perfil não existe...", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Funcoes.hideFloatingActionButton(fabPrincipal);

        Intent intent = new Intent(this, MyService.class);
        startService(intent);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {
            case R.id.itemSair:
                usuarioAtual.signOut();
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_principal) {

            PrincipalFragment principalFragment = new PrincipalFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.containerFrame, principalFragment);
            fragmentTransaction.commit();

            Funcoes.hideFloatingActionButton(fabPrincipal);

        } else if (id == R.id.nav_local) {

            LocalFragment localFragment = new LocalFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.containerFrame, localFragment);
            fragmentTransaction.commit();
            Funcoes.showFloatingActionButton(fabPrincipal);

            fabPrincipal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), AdicionarLocalActivity.class);
                    startActivity(intent);
                }
            });

        } else if (id == R.id.nav_wifi) {

            WifiFragment wifiFragment = new WifiFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.containerFrame, wifiFragment);
            fragmentTransaction.commit();
            Funcoes.showFloatingActionButton(fabPrincipal);

            fabPrincipal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), AdicionarWifiActivity.class);
                    startActivity(intent);
                }
            });

        } else if (id == R.id.nav_gps) {

            GpsFragment gpsFragment = new GpsFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.containerFrame, gpsFragment);
            fragmentTransaction.commit();
            Funcoes.showFloatingActionButton(fabPrincipal);

            fabPrincipal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), AdicionarGPSActivity.class);
                    startActivity(intent);
                }
            });

        } else if (id == R.id.nav_relatorio) {

            RelatorioFragment relatorioFragment = new RelatorioFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.containerFrame, relatorioFragment);
            fragmentTransaction.commit();

            Funcoes.hideFloatingActionButton(fabPrincipal);

        } else if (id == R.id.nav_conta) {

            //ContaFragment contaFragment = new ContaFragment();
            //FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            //fragmentTransaction.replace(R.id.containerFrame, contaFragment);
            //fragmentTransaction.commit();

            Intent intent = new Intent(getApplicationContext(), ContaActivity.class);
            startActivity( intent );

            Funcoes.hideFloatingActionButton(fabPrincipal);

        }

        else if (id == R.id.nav_foto) {

            //ContaFragment contaFragment = new ContaFragment();
            //FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            //fragmentTransaction.replace(R.id.containerFrame, contaFragment);
            //fragmentTransaction.commit();

            Intent intent = new Intent(getApplicationContext(), EditarImageActivity.class);
            startActivity( intent );

            Funcoes.hideFloatingActionButton(fabPrincipal);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void stopService(View view) {

        Intent intent = new Intent(this, MyService.class);
        stopService(intent);

    }

    public void showSettingsAlert(){
        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(MainActivity.this);

        // Titulo do dialogo
        alertDialog.setTitle("GPS");

        // Mensagem do dialogo
        alertDialog.setMessage("GPS não está habilitado. Deseja configurar?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Configurar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                MainActivity.this.startActivity(intent);
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

        geocoder = new Geocoder(MainActivity.this);

        addresses = geocoder.getFromLocation(latitude,longitude,1);

        if (addresses.size() >0){
            address = addresses.get(0);

        }

        return address;
    }
}



