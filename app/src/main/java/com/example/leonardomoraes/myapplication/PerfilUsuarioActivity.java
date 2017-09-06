package com.example.leonardomoraes.myapplication;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Set;

public class PerfilUsuarioActivity extends AppCompatActivity {

    private TextView nome, seguidores, seguindo;
    private ImageView fotoPerfil;
    private FloatingActionButton add;
    private ArrayList<Receita> receitaArrayList;
    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("Receita");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);

        receitaArrayList = new ArrayList<>();

        add = (FloatingActionButton) findViewById(R.id.fab_adicionarReceita_Act_perfil);
        add.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v){
                startActivity(new Intent(PerfilUsuarioActivity.this, TelaReceita.class));
            }
        });

    }
}
