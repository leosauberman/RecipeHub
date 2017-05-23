package com.example.leonardomoraes.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class TelaReceita extends AppCompatActivity {

    private Button criar;
    private ImageButton addImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_receita);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.tipos_de_receita, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        ((Spinner) findViewById(R.id.tipo)).setAdapter(adapter);

        addImage = (ImageButton) findViewById(R.id.imageButton);
        final EditText nome = (EditText) findViewById(R.id.nomeEdit);
        final Spinner tipo = (Spinner) findViewById(R.id.tipo);
        final RadioGroup sabor = (RadioGroup) findViewById(R.id.radioGroup);
        final EditText ingrediente = (EditText) findViewById(R.id.ingredientesEdit);
        final EditText preparo = (EditText) findViewById(R.id.preparoEdit);
        final EditText tempo = (EditText) findViewById(R.id.tempoEdit);
        final int tempoInt = Integer.parseInt(tempo.getText().toString());
        final DataBaseHelper myDB = new DataBaseHelper(this);

        criar = (Button) findViewById(R.id.criarReceita);
        criar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TelaReceita.this, MainActivity.class));
                myDB.insertData(nome.toString(), ingrediente.toString(), tempoInt, sabor.toString(), tipo.toString(), preparo.toString());
            }
        });
    }
}

