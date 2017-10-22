package com.example.leonardomoraes.myapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{

    private RecyclerView recyclerViewSearch;
    private RecyclerAdapter adapterSearch;
    private UserAdapter adapterUserSearch;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference searchRef = database.getReference("Receita");
    private DatabaseReference userRef = database.getReference("Usuario");

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    ListView mDrawerList;
    RelativeLayout mDrawerPane;
    private ActionBarDrawerToggle mDrawerToggle;
    protected DrawerLayout mDrawerLayout;
    private TextView profileTV, user;
    private static String TAG = MainActivity.class.getSimpleName();

    ArrayList<NavItem> mNavItems = new ArrayList<NavItem>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        recyclerViewSearch = (RecyclerView) findViewById(R.id.recycler_view_search);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        recyclerViewSearch.setLayoutManager(gridLayoutManager);

        NavigationView navView = (NavigationView) findViewById(R.id.navView);
        View headerView = navView.getHeaderView(0);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Pesquisar");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        String query = intent.getExtras().getString("query");

        Query busca = searchRef.orderByChild("nome").startAt(query).endAt(query + "\uf8ff");
        //Query busca = searchRef.orderByChild("nome").equalTo(query);
        System.out.println(query);
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

        /*Query buscanome = userRef.orderByChild("nome").equalTo(query);
        buscanome.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<User> userList = new ArrayList();
                for(DataSnapshot userSnapshot : dataSnapshot.getChildren()){
                    User user = userSnapshot.getValue(User.class);

                    userList.add(user);
                }
                adapterUserSearch = new UserAdapter(userList, SearchActivity.this);
                recyclerViewSearch.setAdapter(adapterUserSearch);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/


        //MENU
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        profileTV = (TextView) headerView.findViewById(R.id.profileTV);
        profileTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SearchActivity.this, PerfilUsuarioActivity.class));

            }
        });

        CircleImageView avatar = (CircleImageView) headerView.findViewById(R.id.avatar);
        user = (TextView) headerView.findViewById(R.id.userName);

        for(UserInfo profile: auth.getCurrentUser().getProviderData()){
            if(profile.getProviderId().equals("facebook.com")) {
                Glide.with(SearchActivity.this).load(auth.getCurrentUser().getPhotoUrl()).into(avatar);
                user.setText(auth.getCurrentUser().getDisplayName());
            }
            else {
                Glide.with(SearchActivity.this).load(R.drawable.profilepic).into(avatar);
                user.setText(auth.getCurrentUser().getEmail());
            }
        }

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int pos = item.getItemId();
                if(pos == R.id.feed){
                    startActivity(new Intent(SearchActivity.this, MainActivity.class));

                }
                else if(pos == R.id.preferences){
                    //startActivity(new Intent(SearchActivity.this, SalvarActivity.class));
                }
                else if(pos == R.id.salvas){
                    startActivity(new Intent(SearchActivity.this, SalvarActivity.class));
                }
                else if(pos == R.id.sair){
                    auth.signOut();
                    LoginManager.getInstance().logOut();
                    startActivity(new Intent(SearchActivity.this, LoginActivity.class));
                }
                return false;
            }
        });
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

}