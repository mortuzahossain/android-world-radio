package com.wordpress.mortuza99.worldradio;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class Settings extends AppCompatActivity {

    Spinner countrySelector;
    ArrayList<String> COUNTRY_NAMES;

    // For Shared Preference
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public static final String SHARED_NAME = "RadioStationsData";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        countrySelector = findViewById(R.id.countrySelector);
        COUNTRY_NAMES = getIntent().getStringArrayListExtra("COUNTRY_NAMES");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, COUNTRY_NAMES);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countrySelector.setAdapter(dataAdapter);

        Button save = findViewById(R.id.btnSave);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreferences = getSharedPreferences(SHARED_NAME, Context.MODE_PRIVATE);
                editor = sharedPreferences.edit();
                editor.putString("DEFAULTNAME", COUNTRY_NAMES.get(countrySelector.getSelectedItemPosition()));
                editor.commit();
                Snackbar.make(view, "Setting is Saved.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


    }
}
