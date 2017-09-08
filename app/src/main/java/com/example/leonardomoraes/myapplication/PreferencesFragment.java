package com.example.leonardomoraes.myapplication;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by leonardo.moraes on 02/09/2017.
 */

public class PreferencesFragment extends Fragment {


    public PreferencesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        int data = sharedPreferences.getInt("position", 0);
        if(data == 0){
            return inflater.inflate(R.layout.main, container, false);
        }
        else if(data == 1){
            return inflater.inflate(R.layout.fragment_preferences, container, false);
        }
        return inflater.inflate(R.layout.fragment_preferences, container, false);
    }


}