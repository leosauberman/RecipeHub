package com.example.leonardomoraes.myapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SalvarActivity extends MainActivity {

    private ArrayList<Receita> receitaArrayList;
    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference userRef = database.getReference("Usuario");
    private DatabaseReference myRef = database.getReference("Receita");
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private ActionBarDrawerToggle mDrawerToggle;
    protected DrawerLayout mDrawerLayout;
    private TextView user;
    private TextView profileTV;
    private CircleImageView avatar;
    private ArrayList<String> receitaIdArray;
    private String salvas = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salvar);

        //region MENU
        NavigationView navView = (NavigationView) findViewById(R.id.navView);
        View headerView = navView.getHeaderView(0);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        profileTV = (TextView) headerView.findViewById(R.id.profileTV);
        profileTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SalvarActivity.this, PerfilUsuarioActivity.class));

            }
        });

        avatar = (CircleImageView) headerView.findViewById(R.id.avatar);
        user = (TextView) headerView.findViewById(R.id.userName);

        for(UserInfo profile: auth.getCurrentUser().getProviderData()){
            if(profile.getProviderId().equals("facebook.com")) {
                Glide.with(this).load(auth.getCurrentUser().getPhotoUrl()).into(avatar);
                user.setText(auth.getCurrentUser().getDisplayName());
            }
            else {
                Glide.with(this).load(R.drawable.profilepic).into(avatar);
                user.setText(auth.getCurrentUser().getEmail());
            }
        }

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int pos = item.getItemId();
                if(pos == R.id.feed){
                    startActivity(new Intent(SalvarActivity.this, MainActivity.class));
                }
                else if(pos == R.id.preferences){
                    //startActivity(new Intent(MainActivity.this, SalvarActivity.class));
                }
                else if(pos == R.id.salvas){
                    return true;
                }
                else if(pos == R.id.sair){
                    auth.signOut();
                    LoginManager.getInstance().logOut();
                    startActivity(new Intent(SalvarActivity.this, LoginActivity.class));
                }
                return false;
            }
        });

        mDrawerLayout.addDrawerListener(mDrawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //endregion

        String userId = auth.getCurrentUser().getUid();

        receitaArrayList = new ArrayList<>();
        receitaIdArray = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);


        // Read from the database

        DatabaseReference a = userRef.child(userId).child("salvas");


        a.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot salvasSnapshot : dataSnapshot.getChildren()) {
                    salvas = salvasSnapshot.getValue(String.class);
                    receitaIdArray.add(salvas);
                }
                receitaArrayList.clear();
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot receitaSnapshot : dataSnapshot.getChildren()) {
                            if(receitaIdArray.contains(receitaSnapshot.getKey())) {
                                Receita receita = receitaSnapshot.getValue(Receita.class);
                                receitaArrayList.add(receita);
                            }
                        }
                        adapter = new RecyclerAdapter(receitaArrayList, SalvarActivity.this);
                        recyclerView.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                    }
                });
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        recyclerView.setHasFixedSize(false);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);

        recyclerView.setLayoutManager(gridLayoutManager);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle
        // If it returns true, then it has handled
        // the nav drawer indicator touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    //@Override
    public boolean onQueryTextSubmit(String query) {

        Intent intent = new Intent (this, SearchActivity.class);
        intent.putExtra("query", query);
        startActivity(intent);

        return false;

    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(SalvarActivity.this, MainActivity.class));
    }
}
