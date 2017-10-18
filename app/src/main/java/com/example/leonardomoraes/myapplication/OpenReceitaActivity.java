package com.example.leonardomoraes.myapplication;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

//import static com.example.leonardomoraes.myapplication.R.id.parent;
import static com.example.leonardomoraes.myapplication.R.id.version;

public class OpenReceitaActivity extends MainActivity implements View.OnClickListener {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    List<String> Versoes = new ArrayList<String>();

    private ScrollView scroll;
    private TextView nome, ingredientes, tempo, tipo, preparo, autor;
    private int posTipo;
    private String idProprio, idDono, idPai;
    private Uri imgUri;
    private ProgressDialog progress;
    private ImageView img;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("Receita");

    private DatabaseReference receitaRef, usuarioRef, paiRef;

    private DatabaseReference filhaRef = database.getReference("Receita");
    private RecyclerView recyclerViewFilhas;
    private RecyclerAdapter adapterFilhas;


    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef;

    private FirebaseAuth auth = FirebaseAuth.getInstance();
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
        setContentView(R.layout.activity_open_receita);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setTitle("Pesquisar");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerViewFilhas = (RecyclerView) findViewById(R.id.recycler_view_filhas);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        recyclerViewFilhas.setLayoutManager(gridLayoutManager);


        //LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //inflate your activity layout here!
        //View contentView = inflater.inflate(R.layout.activity_open_receita, null, false);
        //mDrawerLayout.addView(contentView, 0);


        Button version = (Button) findViewById(R.id.version);
        Button delete = (Button) findViewById(R.id.delete);

        version.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OpenReceitaActivity.this, VersionamentoActivity.class);
                intent.putExtra("nome", nome.getText());
                intent.putExtra("ingred", ingredientes.getText());
                intent.putExtra("tempo", tempo.getText());
                intent.putExtra("tipo", tipo.getText());
                intent.putExtra("tipoID", posTipo);
                intent.putExtra("preparo", preparo.getText());
                intent.putExtra("idPai", idProprio);
                intent.putExtra("img", imgUri);
                startActivity(intent);
            }
        });

        nome = (TextView) findViewById(R.id.tv_nomeReceita_Act_openReceita);
        ingredientes = (TextView) findViewById(R.id.tv_ingredientesReceita_Act_openReceita);
        tempo = (TextView) findViewById(R.id.tv_tempoReceita_Act_openReceita);
        tipo = (TextView) findViewById(R.id.tv_tipoReceita_Act_openReceita);
        preparo = (TextView) findViewById(R.id.tv_preparoReceita_Act_openReceita);
        img = (ImageView) findViewById(R.id.imageView_Act_openReceita);
        autor = (TextView) findViewById(R.id.tv_autor_Act_openReceita);


        progress = new ProgressDialog(OpenReceitaActivity.this);

        Intent i = getIntent();
        String nome_string = i.getExtras().getString("nome");
        String ingredientes_string = i.getExtras().getString("ingredientes");
        String tempo_string = i.getExtras().getString("tempo");
        String tipo_string = i.getExtras().getString("tipo");
        String preparo_string = i.getExtras().getString("preparo");

        idProprio = i.getExtras().getString("idProprio");
        posTipo = i.getExtras().getInt("tipoID");
        idDono = i.getExtras().getString("idDono");
        idPai = i.getExtras().getString("idPai");
        String image = i.getExtras().getString("uri");
        String nomeUsuario = i.getExtras().getString("autor");


        if(image != null && !image.equals("no_image")) {
            imgUri = Uri.parse(image);
            storageRef = storage.getReferenceFromUrl(imgUri.toString());
        }
        else{
            Toast.makeText(this, "Receita sem imagem", Toast.LENGTH_SHORT).show();
        }

        receitaRef = database.getReference("Receita").child(idProprio);
        usuarioRef = database.getReference("Usuario").child(idDono).child(idProprio);

        if(idPai == null) {
            if(imgUri != null) {
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(OpenReceitaActivity.this, MainActivity.class));
                        receitaRef.removeValue();
                        usuarioRef.removeValue();
                        storageRef.delete();
                    }
                });
            }
            else{
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(OpenReceitaActivity.this, MainActivity.class));
                        receitaRef.removeValue();
                        usuarioRef.removeValue();
                    }
                });
            }
        }
        else{
            paiRef = database.getReference("Receita").child(idPai).child("filhas").child(idProprio);
            if(imgUri != null) {
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(OpenReceitaActivity.this, MainActivity.class));
                        receitaRef.removeValue();
                        usuarioRef.removeValue();
                        paiRef.removeValue();
                        storageRef.delete();
                    }
                });
            }
            else{
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(OpenReceitaActivity.this, MainActivity.class));
                        receitaRef.removeValue();
                        usuarioRef.removeValue();
                        paiRef.removeValue();
                    }
                });
            }
        }

        nome.setText(nome_string);
        ingredientes.setText(ingredientes_string);
        tempo.setText(tempo_string);
        tipo.setText(tipo_string);
        preparo.setText(preparo_string);
        autor.setText(nomeUsuario);

        Glide.with(OpenReceitaActivity.this).load(imgUri).placeholder(R.drawable.ic_file_download_black_24dp).into(img);

        /*listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        //pegando a list view
        expListView = (ExpandableListView) findViewById(R.id.expand_lv);

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        expListView.setAdapter(listAdapter);

        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                Toast.makeText(OpenReceitaActivity.this, "clicou" + listDataHeader.get(groupPosition),
                        Toast.LENGTH_SHORT).show();
                return false;

            }
        });

        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Expanded",
                        Toast.LENGTH_SHORT).show();
            }
        });

        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Collapsed",
                        Toast.LENGTH_SHORT).show();

            }
        });


        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition)
                                + ":"
                                + listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition),
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        });*/


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
                startActivity(new Intent(OpenReceitaActivity.this, PerfilUsuarioActivity.class));

            }
        });

        ImageView avatar = (ImageView) findViewById(R.id.avatar);
        user = (TextView) findViewById(R.id.userName);

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

        Query busca = filhaRef.orderByChild("idPai").equalTo(idProprio);
        busca.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Receita> newList = new ArrayList<>();
                for(DataSnapshot receitaSnapshot : dataSnapshot.getChildren()) {
                    Receita receita = receitaSnapshot.getValue(Receita.class);

                    newList.add(receita);

                }
                adapterFilhas = new RecyclerAdapter(newList, OpenReceitaActivity.this);
                recyclerViewFilhas.setAdapter(adapterFilhas);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
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



    /*@Override
    protected void onStart() {
        super.onStart();

        /*listDataHeader.add("Versões");
        Versoes.clear();*//*
        // Read from the database
        Query busca = filhaRef.orderByChild("idProprio").equalTo(idProprio);
        busca.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Receita> newList = new ArrayList<>();
                for(DataSnapshot receitaSnapshot : dataSnapshot.getChildren()) {
                    Receita receita = receitaSnapshot.getValue(Receita.class);

                    newList.add(receita);

                }
                adapterFilhas = new RecyclerAdapter(newList, OpenReceitaActivity.this);
                recyclerViewFilhas.setAdapter(adapterFilhas);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }*/
    @Override
    public void onClick(View view) {

    }

}
