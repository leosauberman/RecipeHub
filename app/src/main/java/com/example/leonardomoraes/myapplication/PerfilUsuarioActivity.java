package com.example.leonardomoraes.myapplication;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Set;

public class PerfilUsuarioActivity extends AppCompatActivity {

    private TextView nome, seguidores, seguindo, editar;
    private ImageView fotoPerfil;
    private FloatingActionButton add;
    private ArrayList<Receita> receitaArrayList1;
    private RecyclerView recyclerView1;
    private RecyclerAdapter adapter;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("Receita");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);

        nome = (TextView) findViewById(R.id.tv_nome_act_perfil_usuario);
        seguidores = (TextView) findViewById((R.id.tv_seguidores_act_perfil_usuario));
        seguindo = (TextView) findViewById((R.id.tv_seguindo_act_perfil_usuario));
        editar = (TextView) findViewById(R.id.editar);

        receitaArrayList1 = new ArrayList<>();

        add = (FloatingActionButton) findViewById(R.id.fab_adicionarReceita_Act_perfil);
        add.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v){
                startActivity(new Intent(PerfilUsuarioActivity.this, TelaReceita.class));
            }
        });

        receitaArrayList1.clear();
        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot receitaSnapshot : dataSnapshot.getChildren()) {
                    Receita receita = receitaSnapshot.getValue(Receita.class);

                    receitaArrayList1.add(receita);
                }
                adapter = new RecyclerAdapter(receitaArrayList1, PerfilUsuarioActivity.this);
                recyclerView1.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
        recyclerView1 = (RecyclerView) findViewById(R.id.recycler_view_usuario);
        recyclerView1.setHasFixedSize(false);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);

        recyclerView1.setLayoutManager(gridLayoutManager);

        if(!auth.getCurrentUser().getEmail().isEmpty()){
            nome.setText(auth.getCurrentUser().getEmail());
        }
        else {
            nome.setText("user");
        }

    }
}
