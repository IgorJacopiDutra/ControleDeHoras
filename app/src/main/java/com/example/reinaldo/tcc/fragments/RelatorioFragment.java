package com.example.reinaldo.tcc.fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.Toast;

import com.example.reinaldo.tcc.R;
import com.example.reinaldo.tcc.activity.RelatorioActivity;
import com.example.reinaldo.tcc.adapter.RelatoriosAdapter;
import com.example.reinaldo.tcc.model.Funcoes;
import com.example.reinaldo.tcc.model.Horas;
import com.example.reinaldo.tcc.model.ScreenshotType;
import com.example.reinaldo.tcc.model.ScreenshotUtils;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;


/**
 * A simple {@link Fragment} subclass.
 */
public class RelatorioFragment extends Fragment {

    //public Button btnFiltrar;

    public RecyclerView recyclerListaRelatoriosView;
    public RelatoriosAdapter adapter;
    public Button btnExportar;
    List<Horas> listaRelatorio;

    FirebaseDatabase FBD;
    DatabaseReference DBR;

    private ProgressDialog Dialog;

    public RelatorioFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the activity_login for this fragment
        View view = inflater.inflate(R.layout.content_relatorios, container, false);

        Dialog = new ProgressDialog(getContext());
        Dialog.setMessage("Carregando...");
        Dialog.show();

        setHasOptionsMenu(true);

        btnExportar = view.findViewById(R.id.btnExportar);

        recyclerListaRelatoriosView = view.findViewById(R.id.recyclerListaRelatorios);

        recyclerListaRelatoriosView.setHasFixedSize(true);
        RecyclerView.LayoutManager LM = new LinearLayoutManager(getContext());
        recyclerListaRelatoriosView.setLayoutManager(LM);
        recyclerListaRelatoriosView.setItemAnimator(new DefaultItemAnimator());
        recyclerListaRelatoriosView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

        listaRelatorio = new ArrayList<>();

        adapter = new RelatoriosAdapter(listaRelatorio);

        FBD = FirebaseDatabase.getInstance();

        getDataFirebase();
        Dialog.hide();

        btnExportar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                takeScreenshot(ScreenshotType.FULL);

            }
        });


        //btnFiltrar = view.findViewById(R.id.btnFiltrar);

       // btnFiltrar.setOnClickListener(new View.OnClickListener() {
        //    @Override
          //  public void onClick(View v) {
            //    startActivity(new Intent(getActivity().getApplicationContext(), RelatorioActivity.class));
            //}
        //});

        return view;
    }

    /*  Method which will take screenshot on Basis of Screenshot Type ENUM  */
    private void takeScreenshot(ScreenshotType screenshotType) {
        Bitmap b = null;
        switch (screenshotType) {
            case FULL:
                //If Screenshot type is FULL take full page screenshot i.e our root content.
                b = ScreenshotUtils.getRecyclerViewScreenshot(recyclerListaRelatoriosView);
                break;
        }

        if (b != null) {

            File saveFile = ScreenshotUtils.getMainDirectoryName(getContext());//get the path to save screenshot
            File file = ScreenshotUtils.store(b, "screenshot" + screenshotType + ".jpg", saveFile);//save the screenshot to selected path
            shareScreenshot(file);//finally share screenshot
        } else
            //If bitmap is null show toast message
            Toast.makeText(getContext(), R.string.screenshot_take_failed, Toast.LENGTH_SHORT).show();

    }

    private void shareScreenshot(File file) {
        Uri uri = Uri.fromFile(file);//Convert file path into Uri for sharing
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, getString(R.string.sharing_text));
        intent.putExtra(Intent.EXTRA_STREAM, uri);//pass uri here
        startActivity(Intent.createChooser(intent, getString(R.string.share_title)));
    }

    void getDataFirebase(){

        DBR = Funcoes.pegarReferencia("HORAS_TRABALHADAS", getContext());

        DBR.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Horas hora = dataSnapshot.getValue(Horas.class);
                listaRelatorio.add(hora);
                recyclerListaRelatoriosView.setAdapter(adapter);
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {


        super.onCreateOptionsMenu(menu, inflater);
    }


}
