package com.example.leonardomoraes.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
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

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class VersionamentoActivity extends AppCompatActivity {

    private static final int RC_NICE = 1;
    private static final int RC_PHOTO_PICKER = 2;
    private FloatingActionButton criar;
    private EditText nome, ingrediente, tempo, preparo;
    private int posTipo;
    private Spinner tipo;
    Uri selectedImageUri;

    private Uri downloadUrl;
    private String url;
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
    private int boleana;


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

        imageView = (ImageView) findViewById(R.id.imageView_Act_versionamento);
        addImage = (ImageButton) findViewById(R.id.imageButton_Act_versionamento); 
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

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete act using"), RC_PHOTO_PICKER);
            }
        });

        criar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nomeV = nome.getText().toString();
                ingredienteV = ingrediente.getText().toString();
                tempoV = tempo.getText().toString();
                preparoV = preparo.getText().toString();
                tipoV = tipo.getSelectedItem().toString();
                saborV = verifySabor();
                addRecipeNormal(idVersionada, nomeV, ingredienteV, tempoV, saborV, tipoV, preparoV, url, idDono, idPai, idVersionada);
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
        normalRef.child(idPai).child("filhas").child(idVersionada).child("idFilha").setValue(idVersionada);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_NICE) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Show", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Cancelado", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else if (resultCode == RESULT_OK && requestCode == RC_PHOTO_PICKER) {
            selectedImageUri = data.getData();
            // Get a reference to store file at chat_photos/<FILENAME>
            StorageReference photoRef = storageRef.child(selectedImageUri.getLastPathSegment());

            // Upload file to Firebase Storage
            photoRef.putFile(selectedImageUri)
                    .addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // When the image has successfully uploaded, we get its download URL
                            downloadUrl = taskSnapshot.getDownloadUrl();
                            Glide.with(VersionamentoActivity.this).load(downloadUrl).into(imageView);
                        }
                    });
            changeImageStatus();
        }
    }

    private void changeImageStatus() {
        if (downloadUrl == null) {
            addImage.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
            Toast.makeText(this, "Espere a imagem ser carregada", Toast.LENGTH_SHORT).show();
            Glide.with(imageView.getContext()).load(downloadUrl).into(imageView);
        } else {
            Toast.makeText(this, "Sua receita não vai ter uma imagem??", Toast.LENGTH_LONG).show();
            //changeImageStatus();
            addImage.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.GONE);
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
            return "";
        }
    }

    private boolean verifyPhotoURL() {
        if (downloadUrl != null) {
            url = downloadUrl.toString();
            return true;
        }
        else if(url != null){
            return true;
        }
        else {
            final AlertDialog alertDialog = new AlertDialog.Builder(VersionamentoActivity.this, R.style.alertDialog).create();

            // Setting Dialog Title
            alertDialog.setTitle("E a foto?");

            // Setting Dialog Message
            alertDialog.setMessage("Sua receita vai ter uma foto??");

            // Setting OK Button
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Sim", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                    boleana = 0;
                    Toast.makeText(getApplicationContext(), "Então clique no camera para adicioná-la!!", Toast.LENGTH_LONG).show();
                }
            });

            alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Não", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    addImage.setVisibility(View.GONE);
                    Glide.with(VersionamentoActivity.this).load(R.drawable.noimage).into(imageView);
                    url = "no_image";
                    boleana = 1;
                }
            });
            // Showing Alert Message
            alertDialog.show();
            return boleana == 1;
        }
    }
    private boolean verifyNome() {
        if (nome.getText().toString().isEmpty()) {
            Toast.makeText(VersionamentoActivity.this, "Dê um nome à sua receita", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private boolean verifyIngred() {
        if (ingrediente.getText().toString().isEmpty()) {
            Toast.makeText(VersionamentoActivity.this, "Quais são os ingredientes da sua receita?", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private boolean verifyTempo() {
        if (tempo.getText().toString().isEmpty()) {
            Toast.makeText(VersionamentoActivity.this, "Quanto tempo demora para fazer esta receita?", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private boolean verifyTipo() {
        if (tipo.getSelectedItem().toString().equalsIgnoreCase("Tipo")) {
            Toast.makeText(VersionamentoActivity.this, "Sua receita tem alguma especificidade?", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private boolean verifyPreparo() {
        if (preparo.getText().toString().isEmpty()) {
            Toast.makeText(VersionamentoActivity.this, "Como se faz esta receita?", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }
}
