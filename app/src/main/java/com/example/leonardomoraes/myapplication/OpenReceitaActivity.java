package com.example.leonardomoraes.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class OpenReceitaActivity extends MainActivity {

    private TextView nome, ingredientes, tempo, tipo, preparo, autor;
    private int posTipo;
    private String idProprio, idDono, idPai;
    private Uri imgUri;
    private ProgressDialog progress;
    private ImageView img;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference receitaRef, usuarioRef, paiRef;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_receita);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_open_receita, null, false);
        mDrawerLayout.addView(contentView, 0);


        Button version = (Button) findViewById(R.id.version);
        Button delete = (Button) findViewById(R.id.delete);

        version.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OpenReceitaActivity.this, VersionamentoActivity.class);
                intent.putExtra("nome", nome.getText());
                intent.putExtra("ingred", ingredientes.getText());
                intent.putExtra("tempo", tempo.getText());
                intent.putExtra("tipo", tipo.getText());
                intent.putExtra("tipoID", posTipo);
                intent.putExtra("preparo", preparo.getText());
                intent.putExtra("idPai", idProprio);
                intent.putExtra("img", imgUri);
                startActivity(intent);
            }
        });

        nome = (TextView) findViewById(R.id.tv_nomeReceita_Act_openReceita);
        ingredientes = (TextView) findViewById(R.id.tv_ingredientesReceita_Act_openReceita);
        tempo = (TextView) findViewById(R.id.tv_tempoReceita_Act_openReceita);
        tipo = (TextView) findViewById(R.id.tv_tipoReceita_Act_openReceita);
        preparo = (TextView) findViewById(R.id.tv_preparoReceita_Act_openReceita);
        img = (ImageView) findViewById(R.id.imageView_Act_openReceita);
        autor = (TextView) findViewById(R.id.tv_autor_Act_openReceita);


        Intent i = getIntent();
        String nome_string = i.getExtras().getString("nome");
        String ingredientes_string = i.getExtras().getString("ingredientes");
        String tempo_string = i.getExtras().getString("tempo");
        String tipo_string = i.getExtras().getString("tipo");
        String preparo_string = i.getExtras().getString("preparo");

        idProprio = i.getExtras().getString("idProprio");
        posTipo = i.getExtras().getInt("tipoID");
        idDono = i.getExtras().getString("idDono");
        idPai = i.getExtras().getString("idPai");
        String image = i.getExtras().getString("uri");
//        String nomeUsuario = i.getExtras().getString("nomeUsuario");


        if(image!= null) {
            imgUri = Uri.parse(image);
            storageRef = storage.getReferenceFromUrl(imgUri.toString());
        }
        else{
            Toast.makeText(this, "Receita sem imagem", Toast.LENGTH_SHORT).show();
        }

        receitaRef = database.getReference("Receita").child(idProprio);
        usuarioRef = database.getReference("Usuario").child(idDono).child(idProprio);

        if(idPai == null) {
            if(imgUri != null) {
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(OpenReceitaActivity.this, MainActivity.class));
                        receitaRef.removeValue();
                        usuarioRef.removeValue();
                        storageRef.delete();
                    }
                });
            }
            else{
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(OpenReceitaActivity.this, MainActivity.class));
                        receitaRef.removeValue();
                        usuarioRef.removeValue();
                    }
                });
            }
        }
        else{
            paiRef = database.getReference("Receita").child(idPai).child("filhas").child(idProprio);
            if(imgUri != null) {
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(OpenReceitaActivity.this, MainActivity.class));
                        receitaRef.removeValue();
                        usuarioRef.removeValue();
                        paiRef.removeValue();
                        storageRef.delete();
                    }
                });
            }
            else{
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(OpenReceitaActivity.this, MainActivity.class));
                        receitaRef.removeValue();
                        usuarioRef.removeValue();
                        paiRef.removeValue();
                    }
                });
            }
        }

        nome.setText(nome_string);
        ingredientes.setText(ingredientes_string);
        tempo.setText(tempo_string);
        tipo.setText(tipo_string);
        preparo.setText(preparo_string);
//        autor.setText(nomeUsuario);

        Glide.with(OpenReceitaActivity.this).load(imgUri).placeholder(R.drawable.ic_file_download_black_24dp).into(img);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
    }


}

