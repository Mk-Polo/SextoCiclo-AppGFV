package com.idat.appgfv.UI;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.idat.appgfv.R;

public class Prueba_fragment extends Fragment {


    public Prueba_fragment() {

    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_prueba_fragment, container, false);
    }
}