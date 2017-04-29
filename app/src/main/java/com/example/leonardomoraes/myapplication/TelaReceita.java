package com.example.leonardomoraes.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class TelaReceita extends AppCompatActivity {

    private Button criar;
    private EditText nomeReceita, ingredientes, preparo, tempo;
    private TextView receitaFeed, tipoTextView, sdTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_receita);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.tipos_de_receita, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        ((Spinner) findViewById(R.id.tipo)).setAdapter(adapter);

        criar = (Button) findViewById(R.id.criarReceita);
    }
    public void addReceita(View view) {
        Bundle params = new Bundle();
        //Map<String, String> campos = new HashMap<>();
        EditText nome = (EditText) findViewById(R.id.nomeEdit);
        Spinner tipo = (Spinner) findViewById(R.id.tipo);
        RadioButton sal = (RadioButton) findViewById(R.id.salgado);
        RadioButton doce = (RadioButton) findViewById(R.id.doce);
        EditText ingredientes = (EditText) findViewById(R.id.ingredientesEdit);
        EditText preparo = (EditText) findViewById(R.id.preparoEdit);
        EditText tempo = (EditText) findViewById(R.id.tempoEdit);

        if(!nome.toString().isEmpty() && !tipo.toString().isEmpty() && !ingredientes.toString().isEmpty() && !preparo.toString().isEmpty()){
            params.putString("nome", nome.toString());
            params.putString("tipo", nome.toString());
        }

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtras(params);

        startActivityForResult(intent, 1);
    }
    public void aceitou(View view){
            Intent intent = new Intent();
            intent.putExtra("msg", "Receita adicionada");
            setResult(1, intent);
            finish();
    }
    /*Intent intent = new Intent(this, MainActivity.class);
                                intent.putExtras(params);

    startActivityForResult(intent, 1);*/


        /*

        public void aceitou(View view){
            Intent intent = new Intent();
            intent.putExtra("msg", "Aceitou");

            setResult(1, intent);
            finish();
        }*/
    }

