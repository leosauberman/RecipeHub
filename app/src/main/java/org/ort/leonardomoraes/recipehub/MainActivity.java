package org.ort.leonardomoraes.recipehub;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.SearchView;


import com.bumptech.glide.Glide;
import com.facebook.CallbackManager;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

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
    private TextView user;
    private TextView profileTV;
    private CircleImageView avatar;

    private CallbackManager callbackManager;

    ArrayList<NavItem> mNavItems = new ArrayList<NavItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        NavigationView navView = (NavigationView) findViewById(R.id.navView);
        View headerView = navView.getHeaderView(0);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setTitle("Pesquisar");

        receitaArrayList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        add1 = (FloatingActionButton) findViewById(R.id.fab_adicionarReceita_Act_main);
        add1.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v){
                startActivity(new Intent(MainActivity.this, TelaReceita.class));
            }
        });

        //FAB hiding
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
                if (dy > 0 && add1.isShown()){
                    add1.hide();
                }
                else{
                    add1.show();
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

            }
        });

        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        profileTV = (TextView) headerView.findViewById(R.id.profileTV);
        profileTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PerfilUsuarioActivity.class));

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
                    //startActivity(new Intent(MainActivity.this, MainActivity.class));
                    return true;
                }
                else if(pos == R.id.preferences){
                    //startActivity(new Intent(MainActivity.this, SalvarActivity.class));
                }
                else if(pos == R.id.salvas){
                    startActivity(new Intent(MainActivity.this, SalvarActivity.class));
                }
                else if(pos == R.id.sair){
                    auth.signOut();
                    LoginManager.getInstance().logOut();
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }
                return false;
            }
        });

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

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                receitaArrayList.clear();
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

/*
    private void selectItemFromDrawer(int position) {

        if(position == 0){
            startActivity(new Intent(this, MainActivity.class));
        }
        else if(position == 2){
            startActivity(new Intent(this, SalvarActivity.class));
        }
        else if(position == 3){
            auth.signOut();
            startActivity(new Intent(this, LoginActivity.class));
        }

        mDrawerList.setItemChecked(position, true);
        setTitle(mNavItems.get(position).mTitle);


        // Close the drawer
        mDrawerLayout.closeDrawer(mDrawerPane);
    }*/
}