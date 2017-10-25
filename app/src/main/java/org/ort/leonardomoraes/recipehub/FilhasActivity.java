package org.ort.leonardomoraes.recipehub;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FilhasActivity extends AppCompatActivity {

    private ArrayList<Receita> tempArrayList;
    private ArrayList<Receita> receitaArrayList;
    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("Receita");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filhas);

        tempArrayList = new ArrayList<>();
        receitaArrayList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerFilhas);
        final TextView textView = (TextView) findViewById(R.id.text);

        Intent i = getIntent();
        String idProprio = i.getExtras().getString("idProprio");
        final String tipo = i.getExtras().getString("tipo");

        Query buscaFilhas = myRef.orderByChild("idPai").equalTo(idProprio);
        buscaFilhas.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot receitaSnapshot : dataSnapshot.getChildren()) {
                    Receita receita = receitaSnapshot.getValue(Receita.class);
                    tempArrayList.add(receita);
                }
                for(int j = 0; j < tempArrayList.size(); j++){
                    if(tempArrayList.get(j).getTipo().equals(tipo)) {
                        receitaArrayList.add(tempArrayList.get(j));
                    }
                }
                adapter = new RecyclerAdapter(receitaArrayList, FilhasActivity.this);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if(receitaArrayList.isEmpty()){
            textView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }

        recyclerView.setHasFixedSize(false);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);

        recyclerView.setLayoutManager(gridLayoutManager);
    }
}
    /*@Override
    protected void onStart() {
        super.onStart();
        receitaArrayList.clear();
        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot receitaSnapshot : dataSnapshot.getChildren()) {
                    Receita receita = receitaSnapshot.getValue(Receita.class);

                    receitaArrayList.add(receita);
                }
                adapter = new RecyclerAdapter(receitaArrayList, MainActivity.this);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
        recyclerView.setHasFixedSize(false);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);

        recyclerView.setLayoutManager(gridLayoutManager);
    }*/

