package com.example.reinaldo.tcc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.example.reinaldo.tcc.R;
import com.example.reinaldo.tcc.adapter.GPSAdapter;
import com.example.reinaldo.tcc.helper.RecyclerItemClickListener;
import com.example.reinaldo.tcc.model.GPS;

import java.util.ArrayList;
import java.util.List;

public class GPSActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fabGps = (FloatingActionButton) findViewById(R.id.fabGps);

        fabGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AdicionarGPSActivity.class);
                startActivity( intent );
            }
        });

    }

}
