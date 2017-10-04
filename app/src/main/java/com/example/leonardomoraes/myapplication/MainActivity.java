package com.example.leonardomoraes.myapplication;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.SearchView;


import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{

    private ArrayList<Receita> receitaArrayList;
    private FloatingActionButton add1;
    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("Receita");
    private DatabaseReference userRef = database.getReference("Usuario");
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private static String TAG = MainActivity.class.getSimpleName();
    ListView mDrawerList;
    RelativeLayout mDrawerPane;
    private ActionBarDrawerToggle mDrawerToggle;
    protected DrawerLayout mDrawerLayout;
    private TextView profile;
    private TextView user;
    private ImageView avatar;

    ArrayList<NavItem> mNavItems = new ArrayList<NavItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setTitle("Pesquisar");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        receitaArrayList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        add1 = (FloatingActionButton) findViewById(R.id.fab_adicionarReceita_Act_main);
        add1.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v){
                startActivity(new Intent(MainActivity.this, TelaReceita.class));
            }
        });

        profile = (TextView) findViewById(R.id.desc);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PerfilUsuarioActivity.class));

            }
        });

        avatar = (ImageView) findViewById(R.id.avatar);
        Glide.with(this).load(auth.getCurrentUser().getPhotoUrl()).into(avatar);

        user = (TextView) findViewById(R.id.userName);
        user.setText(auth.getCurrentUser().getDisplayName());

        //MENU
        //user.setText(database.getReference("Usuario").child(auth.getCurrentUser().getUid()).child("nomeUsuario").toString());
        mNavItems.add(new NavItem("Feed de receitas", "Onde estão todas as receitas", R.drawable.ic_home));
        mNavItems.add(new NavItem("Preferências", "Altere suas preferências", R.drawable.ic_action_settings));
        mNavItems.add(new NavItem("Sobre", "Conheça os desenvolvedores", R.drawable.ic_action_about));
        mNavItems.add(new NavItem("Sair", "Sair do seu perfil", R.drawable.ic_close));

        // DrawerLayout
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        // Populate the Navigtion Drawer with options
        mDrawerPane = (RelativeLayout) findViewById(R.id.drawerPane);
        mDrawerList = (ListView) findViewById(R.id.navList);
        DrawerListAdapter adapter = new DrawerListAdapter(this, mNavItems);
        mDrawerList.setAdapter(adapter);

        // Drawer Item click listeners
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItemFromDrawer(position);
            }
        });

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                Log.d(TAG, "onDrawerClosed: " + getTitle());

                invalidateOptionsMenu();
            }
        };

        mDrawerLayout.addDrawerListener(mDrawerToggle);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

    @Override
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

        Intent intent = new Intent (this, SearchActivity.class);
        intent.putExtra("query", query);
        startActivity(intent);

        return false;

    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    public void onBackPressed() {
    }


    private void selectItemFromDrawer(int position) {

        if(position == 0){
            startActivity(new Intent(this, MainActivity.class));
        }
        else if(position == 2){
            startActivity(new Intent(this, SobreActivity.class));
        }
        else if(position == 3){
            auth.signOut();
            startActivity(new Intent(this, LoginActivity.class));
        }

        mDrawerList.setItemChecked(position, true);
        setTitle(mNavItems.get(position).mTitle);


        // Close the drawer
        mDrawerLayout.closeDrawer(mDrawerPane);
    }
}