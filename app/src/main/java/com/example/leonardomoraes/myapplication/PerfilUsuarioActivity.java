package com.example.leonardomoraes.myapplication;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.w3c.dom.Text;

public class PerfilUsuarioActivity extends AppCompatActivity {

    private TextView nome, seguidores, seguindo;
    private ImageView fotoPerfil;
    private FloatingActionButton add;
    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("Receita");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);
    }
}
