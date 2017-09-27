package com.example.leonardomoraes.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class VersionamentoActivity extends AppCompatActivity {

    private static final int RC_NICE = 1;
    private static final int RC_PHOTO_PICKER = 2;
    private FloatingActionButton criar;
    private EditText nome, ingrediente, tempo, preparo;
    private int posTipo;
    private Spinner tipo;
    Uri selectedImageUri;

    private String nomeV, ingredienteV, tempoV, preparoV, tipoV, saborV, idPai;

    private ImageButton addImage;
    private RadioButton sal, doce;
    private String sabor;
    private ImageView imageView;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference normalRef = database.getReference("Receita"); //addRecipe normal
    private String idVersionada = normalRef.push().getKey();
    private DatabaseReference userRef = database.getReference("Usuario");

    private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    private StorageReference storageRef = firebaseStorage.getReference().child("recipes_photos");
    private FirebaseAuth auth = FirebaseAuth.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_versionamento);

        //region Spinner Adapter
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.tipos_de_receita, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        ((Spinner) findViewById(R.id.spinner_tipo_Act_versionamento)).setAdapter(adapter);
        //endregion


        final String idDono = auth.getCurrentUser().getUid();


        criar = (FloatingActionButton) findViewById(R.id.fab_versionarReceita_Act_versionamento);
        nome = (EditText) findViewById(R.id.et_nomeReceita_Act_versionamento);
        ingrediente = (EditText) findViewById(R.id.et_ingredientesReceita_Act_versionamento);
        tempo = (EditText) findViewById(R.id.et_tempoReceita_Act_versionamento);
        preparo = (EditText) findViewById(R.id.et_preparoReceita_Act_versionamento);
        tipo = (Spinner) findViewById(R.id.spinner_tipo_Act_versionamento);
        sal = (RadioButton) findViewById(R.id.rb_salgado_v);
        doce = (RadioButton) findViewById(R.id.rb_doce_v);

        Intent intent = getIntent();
        posTipo = intent.getExtras().getInt("tipoID");
        idPai = intent.getExtras().getString("idPai");
        nome.setText(intent.getExtras().getString("nome"));
        ingrediente.setText(intent.getExtras().getString("ingred"));
        tempo.setText(intent.getExtras().getString("tempo"));
        preparo.setText(intent.getExtras().getString("preparo"));
        tipo.setSelection(posTipo);




        criar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nomeV = nome.getText().toString();
                ingredienteV = ingrediente.getText().toString();
                tempoV = tempo.getText().toString();
                preparoV = preparo.getText().toString();
                tipoV = tipo.getSelectedItem().toString();
                saborV = verifySabor();
                addRecipeNormal(idVersionada, nomeV, ingredienteV, tempoV, saborV, tipoV, preparoV, null, idDono, idPai, idVersionada);
                addVersionRef(idVersionada, idPai);
                startActivity(new Intent(VersionamentoActivity.this, MainActivity.class));
            }
        });

    }

    private void addRecipeNormal(String recipeId,
                           String nome,
                           String ingrediente,
                           String tempo,
                           String sabor,
                           String tipo,
                           String preparo,
                           String urlFoto,
                           String idDono,
                           String idPai,
                                 String idProprio){
        Receita receita = new Receita(nome, ingrediente, tempo, sabor, tipo, preparo, urlFoto, idDono, idPai, idProprio);

        normalRef.child(recipeId).setValue(receita);
        userRef.child(idDono).child(recipeId).setValue(nomeV);
    }

    private void addVersionRef(String idVersionada,
                               String idPai){
        normalRef.child(idPai).child("filhas").child(idVersionada).setValue(nomeV);
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
            return "";
        }
    }
}
