package com.example.leonardomoraes.myapplication;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{

    private ArrayList<Receita> receitaArrayList;
    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("Receita");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        /*query = query.toLowerCase();
        ArrayList<Receita> newList = new ArrayList<>();
        for (Receita receita : receitaArrayList){
            String nome = receita.getNome().toLowerCase();
            if (nome.contains(query)){
                newList.add(receita);
            }
        }
        adapter = new RecyclerAdapter(newList, MainActivity.this);
        recyclerView.setAdapter(adapter);
        return false; */

        Query busca = myRef.orderByChild("nome").equalTo(query);
        busca.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Receita> newList = new ArrayList<>();
                for(DataSnapshot receitaSnapshot : dataSnapshot.getChildren()) {
                    Receita receita = receitaSnapshot.getValue(Receita.class);

                    newList.add(receita);

                }
                adapter = new RecyclerAdapter(newList, SearchActivity.this);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return false;

    }

    @Override
    public boolean onQueryTextChange(String newText) {
        /*newText = newText.toLowerCase();
        ArrayList<Receita> newList = new ArrayList<>();
        for (Receita receita : receitaArrayList){
            String nome = receita.getNome().toLowerCase();
            if (nome.contains(newText)){
                newList.add(receita);
            }
        }
        adapter = new RecyclerAdapter(newList, MainActivity.this);
        recyclerView.setAdapter(adapter);

        Query query = myRef.orderByChild("nome").equalTo(newText);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot receitaSnapshot : dataSnapshot.getChildren()) {
                    Receita receita = receitaSnapshot.getValue(Receita.class);

                    if(!searchArrayList.contains(receita)){
                        searchArrayList.add(receita);
                    }
                }
                adapter = new RecyclerAdapter(searchArrayList, MainActivity.this);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

        return false;
    }
}
