package com.example.leonardomoraes.myapplication;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
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

public class SearchActivity extends AppCompatActivity{

    private RecyclerView recyclerViewSearch;
    private RecyclerAdapter adapterSearch;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference searchRef = database.getReference("Receita");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        recyclerViewSearch = (RecyclerView) findViewById(R.id.recycler_view_search);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        recyclerViewSearch.setLayoutManager(gridLayoutManager);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setTitle("Pesquisar");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent intent = getIntent();
        String query = intent.getExtras().getString("query");

        Query busca = searchRef.orderByChild("nome").equalTo(query);
        busca.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Receita> newList = new ArrayList<>();
                for(DataSnapshot receitaSnapshot : dataSnapshot.getChildren()) {
                    Receita receita = receitaSnapshot.getValue(Receita.class);

                    newList.add(receita);

                }
                adapterSearch = new RecyclerAdapter(newList, SearchActivity.this);
                recyclerViewSearch.setAdapter(adapterSearch);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
