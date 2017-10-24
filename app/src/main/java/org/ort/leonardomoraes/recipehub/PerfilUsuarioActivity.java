package org.ort.leonardomoraes.recipehub;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

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
    private TextView profileTV;
    private CircleImageView avatar;
    private TextView user;
    private static String TAG = MainActivity.class.getSimpleName();

    ArrayList<NavItem> mNavItems = new ArrayList<NavItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);

        NavigationView navView = (NavigationView) findViewById(R.id.navView);
        View headerView = navView.getHeaderView(0);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nome = (TextView) findViewById(R.id.tv_nome_act_perfil_usuario);
        seguidores = (TextView) findViewById((R.id.tv_seguidores_act_perfil_usuario));
        seguindo = (TextView) findViewById((R.id.tv_seguindo_act_perfil_usuario));

        CircleImageView profileImage = (CircleImageView) findViewById(R.id.profileImage);
        for(UserInfo profile: auth.getCurrentUser().getProviderData()){
            if(profile.getProviderId().equals("facebook.com")) {
                Glide.with(PerfilUsuarioActivity.this).load(auth.getCurrentUser().getPhotoUrl()).into(profileImage);
                nome.setText(auth.getCurrentUser().getDisplayName());
            }
            else {
                Glide.with(this).load(R.drawable.profilepic).into(profileImage);
                nome.setText(auth.getCurrentUser().getEmail());
            }
        }

        recyclerView1 = (RecyclerView) findViewById(R.id.recycler_view_usuario);
        receitaArrayList1 = new ArrayList<>();

        add = (FloatingActionButton) findViewById(R.id.fab_adicionarReceita_Act_perfil);
        add.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v){
                startActivity(new Intent(PerfilUsuarioActivity.this, TelaReceita.class));
            }
        });

        //FAB hiding
        recyclerView1.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
                if (dy > 0 && add.isShown()){
                    add.hide();
                }
                else{
                    add.show();
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

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

        recyclerView1.setHasFixedSize(false);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);

        recyclerView1.setLayoutManager(gridLayoutManager);

        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        profileTV = (TextView) headerView.findViewById(R.id.profileTV);
        profileTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                return;

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
                    startActivity(new Intent(PerfilUsuarioActivity.this, MainActivity.class));
                }
                else if(pos == R.id.preferences){
                    startActivity(new Intent(PerfilUsuarioActivity.this, SalvarActivity.class));
                }
                else if(pos == R.id.salvas){
                    //startActivity(new Intent(MainActivity.this, SalvarActivity.class));
                }
                else if(pos == R.id.sair){
                    auth.signOut();
                    startActivity(new Intent(PerfilUsuarioActivity.this, LoginActivity.class));
                }
                return false;
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
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



}