package com.example.leonardomoraes.myapplication;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PerfilUsuarioActivity extends MainActivity {

    private TextView nome, seguidores, seguindo, editar;
    private ImageView fotoPerfil;
    private FloatingActionButton add;
    private ArrayList<Receita> receitaArrayList1;
    private RecyclerView recyclerView1;
    private RecyclerAdapter adapter;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("Receita");


    ListView mDrawerList;
    RelativeLayout mDrawerPane;
    private ActionBarDrawerToggle mDrawerToggle;
    protected DrawerLayout mDrawerLayout;
    private TextView profile;
    private TextView user;
    private static String TAG = MainActivity.class.getSimpleName();

    ArrayList<NavItem> mNavItems = new ArrayList<NavItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setTitle("Pesquisar");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nome = (TextView) findViewById(R.id.tv_nome_act_perfil_usuario);
        seguidores = (TextView) findViewById((R.id.tv_seguidores_act_perfil_usuario));
        seguindo = (TextView) findViewById((R.id.tv_seguindo_act_perfil_usuario));
        editar = (TextView) findViewById(R.id.editar);

        ImageView profileImage = (ImageView) findViewById(R.id.profileImage);
        Glide.with(this).load(auth.getCurrentUser().getPhotoUrl()).into(profileImage);

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

                    if(receita.getIdDono().equals(auth.getCurrentUser().getUid())){
                        receitaArrayList1.add(receita);
                    }
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

        nome.setText(auth.getCurrentUser().getDisplayName());


        //MENU
        mNavItems.add(new NavItem("Feed de receitas", "Onde estão toas as receitas", R.drawable.ic_home));
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

        profile = (TextView) findViewById(R.id.desc);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(PerfilUsuarioActivity.this, PerfilUsuarioActivity.class));
                return;

            }
        });

        ImageView avatar = (ImageView) findViewById(R.id.avatar);
        Glide.with(this).load(auth.getCurrentUser().getPhotoUrl()).into(avatar);

        user = (TextView) findViewById(R.id.userName);
        user.setText(auth.getCurrentUser().getDisplayName());

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



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
    }

    /*private void selectItemFromDrawer(int position) {
        Fragment fragment = new PreferencesFragment();

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.openContent, fragment)
                .commit();

        if(position == 3){
            auth.signOut();
            startActivity(new Intent(this, LoginActivity.class));
        }

        mDrawerList.setItemChecked(position, true);
        setTitle(mNavItems.get(position).mTitle);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("position", position);
        editor.commit();

        // Close the drawer
        mDrawerLayout.closeDrawer(mDrawerPane);
    }*/

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
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener((SearchView.OnQueryTextListener) this);
        return true;
    }


    public boolean onQueryTextSubmit(String query) {

        Intent intent = new Intent (this, SearchActivity.class);
        intent.putExtra("query", query);
        startActivity(intent);

        return false;
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
        //setTitle(mNavItems.get(position).mTitle);

        // Close the drawer
        mDrawerLayout.closeDrawer(mDrawerPane);
    }


}
