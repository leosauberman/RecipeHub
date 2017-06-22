package com.example.leonardomoraes.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
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

    private FloatingActionButton criar;
    private EditText nome, ingrediente, tempo, preparo;
    private Spinner tipo;
    private ImageButton addImage;
    private RadioButton sal, doce;
    private String sabor;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("Receita");
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_receita);
        //region Spinner Adapter
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.tipos_de_receita, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        ((Spinner) findViewById(R.id.tipo)).setAdapter(adapter);
        //endregion

        addImage = (ImageButton) findViewById(R.id.imageButton);

        nome = (EditText) findViewById(R.id.nomeEdit);
        ingrediente = (EditText) findViewById(R.id.ingredientesEdit);
        tempo = (EditText) findViewById(R.id.tempoEdit);
        sal = (RadioButton) findViewById(R.id.salgado);
        doce = (RadioButton) findViewById(R.id.doce);
        tipo = (Spinner) findViewById(R.id.tipo);
        preparo = (EditText) findViewById(R.id.preparoEdit);


        id = myRef.push().getKey();


        criar = (FloatingActionButton) findViewById(R.id.criarReceita);
        criar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                if(verifyNome() && verifyIngred() && verifyTempo() && !verifySabor().isEmpty() && verifyTipo() && verifyPreparo())
                {
                    startActivity(new Intent(TelaReceita.this, MainActivity.class));
                    addRecipe(id, nome.getText().toString(), ingrediente.getText().toString(), tempo.getText().toString(), sabor, tipo.getSelectedItem().toString(), preparo.getText().toString());
                }
                //startActivity(new Intent(TelaReceita.this, MainActivity.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(TelaReceita.this, MainActivity.class));
    }

    private void addRecipe(String recipeId,
                           String nome,
                           String ingrediente,
                           String tempo,
                           String sabor,
                           String tipo,
                           String preparo){
        Receita receita = new Receita(nome, ingrediente, tempo, sabor, tipo, preparo);

        myRef.child(recipeId).setValue(receita);
    }
    private boolean verifyNome(){
        if(nome.getText().toString().isEmpty()){
            Toast.makeText(TelaReceita.this, "Dê um nome à sua receita", Toast.LENGTH_SHORT).show();
            return false;
        }
        else{
            return true;
        }
    }
    private boolean verifyIngred(){
        if(ingrediente.getText().toString().isEmpty()){
            Toast.makeText(TelaReceita.this, "Quais são os ingredientes da sua receita?", Toast.LENGTH_SHORT).show();
            return false;
        }
        else{
            return true;
        }
    }
    private boolean verifyTempo(){
        if(tempo.getText().toString().isEmpty()){
            Toast.makeText(TelaReceita.this, "Quanto tempo demora para fazer esta receita?", Toast.LENGTH_SHORT).show();
            return false;
        }
        else{
            return true;
        }
    }
    private boolean verifyTipo(){
        if(tipo.getSelectedItem().toString().equalsIgnoreCase("Tipo")){
            Toast.makeText(TelaReceita.this, "Sua receita tem alguma especificidade?", Toast.LENGTH_SHORT).show();
            return false;
        }
        else{
            return true;
        }
    }
    private boolean verifyPreparo(){
        if(preparo.getText().toString().isEmpty()){
            Toast.makeText(TelaReceita.this, "Como se faz esta receita?", Toast.LENGTH_SHORT).show();
            return false;
        }
        else{
            return true;
        }
    }
    private String verifySabor(){
        if(sal.isChecked()) {
            sabor = sal.getText().toString();
            return sabor;
        }
        else if(doce.isChecked()){
            sabor = doce.getText().toString();
            return sabor;
        }
        else{
            Toast.makeText(TelaReceita.this, "Selecione doce ou salgado", Toast.LENGTH_SHORT).show();
            return "";
        }
    }
}

