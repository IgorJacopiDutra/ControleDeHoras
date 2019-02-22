package com.example.reinaldo.tcc.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.reinaldo.tcc.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SairFragment extends Fragment {


    public SairFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the activity_login for this fragment
        return inflater.inflate(R.layout.activity_local, container, false);
    }

}
