package com.example.leonardomoraes.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class TelaReceita extends AppCompatActivity {

    private static final int RC_NICE = 1;
    private static final int RC_PHOTO_PICKER = 2;
    private FloatingActionButton criar;
    private EditText nome, ingrediente, tempo, preparo;
    private Spinner tipo;
    Uri selectedImageUri;

    private ImageButton addImage;
    private RadioButton sal, doce;
    private String sabor;
    private Uri downloadUrl;

    private ImageView imageView;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("Receita");
    private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    private StorageReference storageRef = firebaseStorage.getReference().child("recipes_photos");
    private String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_receita);
        //region Spinner Adapter
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.tipos_de_receita, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        ((Spinner) findViewById(R.id.spinner_tipo_Act_telaReceita)).setAdapter(adapter);
        //endregion

        addImage = (ImageButton) findViewById(R.id.imageButton_Act_telaReceita);

        nome = (EditText) findViewById(R.id.et_nomeReceita_Act_telaReceita);
        ingrediente = (EditText) findViewById(R.id.et_ingredientesReceita_Act_telaReceita);
        tempo = (EditText) findViewById(R.id.et_tempoReceita_Act_telaReceita);
        sal = (RadioButton) findViewById(R.id.rb_salgado);
        doce = (RadioButton) findViewById(R.id.rb_doce);
        tipo = (Spinner) findViewById(R.id.spinner_tipo_Act_telaReceita);
        preparo = (EditText) findViewById(R.id.et_preparoReceita_Act_telaReceita);

        imageView = (ImageView) findViewById(R.id.imageView_Act_telaReceita);

        id = myRef.push().getKey();

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete act using"), RC_PHOTO_PICKER);
            }
        });

        criar = (FloatingActionButton) findViewById(R.id.fab_criarReceita_Act_telaReceita);
        criar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                if(verifyNome() && verifyIngred() && verifyTempo() && !verifySabor().isEmpty() && verifyTipo() && verifyPreparo())
                {
                    startActivity(new Intent(TelaReceita.this, MainActivity.class));
                    addRecipe(id, nome.getText().toString(), ingrediente.getText().toString(), tempo.getText().toString(), sabor, tipo.getSelectedItem().toString(), preparo.getText().toString(), downloadUrl.toString());
                }
                //startActivity(new Intent(TelaReceita.this, MainActivity.class));
            }
        });
    }

    /*@Override
    public void onBackPressed() {
        super.onBackPressed();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Voltar?");
        builder.setMessage("Deseja abandonar sua receita?");
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(TelaReceita.this, MainActivity.class));
            }
        });
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog;
        alertDialog = builder.create();
        alertDialog.show();
    }*/

    private void addRecipe(String recipeId,
                           String nome,
                           String ingrediente,
                           String tempo,
                           String sabor,
                           String tipo,
                           String preparo,
                           String urlFoto){
        Receita receita = new Receita(nome, ingrediente, tempo, sabor, tipo, preparo, urlFoto);

        myRef.child(recipeId).setValue(receita);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_NICE){
            if(resultCode == RESULT_OK){
                Toast.makeText(this, "Show", Toast.LENGTH_SHORT).show();
            }
            else if(resultCode == RESULT_CANCELED){
                Toast.makeText(this, "Cancelado", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
        else if(resultCode == RESULT_OK && requestCode == RC_PHOTO_PICKER) {
            selectedImageUri = data.getData();
            // Get a reference to store file at chat_photos/<FILENAME>
            StorageReference photoRef = storageRef.child(selectedImageUri.getLastPathSegment());

            // Upload file to Firebase Storage
            photoRef.putFile(selectedImageUri)
                    .addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // When the image has successfully uploaded, we get its download URL
                            downloadUrl = taskSnapshot.getDownloadUrl();
                            Glide.with(TelaReceita.this).load(downloadUrl).into(imageView);
                        }
                    });
            changeImageStatus();
        }
    }

    private void changeImageStatus(){
        if(downloadUrl == null){
            addImage.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
            Glide.with(imageView.getContext()).load(downloadUrl).into(imageView);
        }
        else{
            Toast.makeText(this, "Espere a imagem ser carregada", Toast.LENGTH_LONG).show();
            changeImageStatus();
            addImage.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.GONE);
        }
    }
    //region Verifying
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
    //endregion
}

