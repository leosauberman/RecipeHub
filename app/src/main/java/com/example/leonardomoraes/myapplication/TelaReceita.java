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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class TelaReceita extends AppCompatActivity {

    private Button criar;
    private EditText nome, ingrediente, tempo, preparo;
    private Spinner tipo;
    private ImageButton addImage;
    private Receita receita;
    private RadioButton sal, doce;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_receita);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.tipos_de_receita, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        ((Spinner) findViewById(R.id.tipo)).setAdapter(adapter);

        addImage = (ImageButton) findViewById(R.id.imageButton);

        nome = (EditText) findViewById(R.id.nomeEdit);
        ingrediente = (EditText) findViewById(R.id.ingredientesEdit);
        tempo = (EditText) findViewById(R.id.tempoEdit);
        sal = (RadioButton) findViewById(R.id.salgado);
        doce = (RadioButton) findViewById(R.id.doce);
        tipo = (Spinner) findViewById(R.id.tipo);
        preparo = (EditText) findViewById(R.id.preparoEdit);

        /*if(sal.isChecked()) {
            receita = new Receita(nome.getText().toString(), ingrediente.getText().toString(), tempo.getText().toString(), sal.getText().toString(), tipo.getSelectedItem().toString(), preparo.getText().toString());
        }
        else if(doce.isChecked()){
            receita = new Receita(nome.getText().toString(), ingrediente.getText().toString(), tempo.getText().toString(), doce.getText().toString(), tipo.getSelectedItem().toString(), preparo.getText().toString());
        }
        else{
            Toast.makeText(this, "Selecione doce ou salgado", Toast.LENGTH_SHORT).show();
        }*/

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Receita");
        final String id = myRef.push().getKey();

        criar = (Button) findViewById(R.id.criarReceita);
        criar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TelaReceita.this, MainActivity.class));
                receita = new Receita(nome.getText().toString(), ingrediente.getText().toString(), tempo.getText().toString(), sal.getText().toString(), tipo.getSelectedItem().toString(), preparo.getText().toString());
                myRef.child(id).setValue(receita);
            }
        });
    }
}

