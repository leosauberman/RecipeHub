package com.example.leonardomoraes.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.sax.StartElementListener;
import android.support.annotation.NonNull;
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
import com.facebook.login.LoginManager;
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

import de.hdodenhof.circleimageview.CircleImageView;

/*
import static com.example.leonardomoraes.myapplication.R.id.parent;
import static com.example.leonardomoraes.myapplication.R.id.version;
*/

public class OpenReceitaActivity extends MainActivity implements View.OnClickListener {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    List<String> Versoes = new ArrayList<String>();

    private ScrollView scroll;
    private TextView nome, ingredientes, tempo, tipo, preparo, autor, receitaOriginal, receitasFilhas;
    private int posTipo;
    private String idProprio, idDono, idPai;
    private Uri imgUri;
    private ProgressDialog progress;
    private ImageView img;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference receitasRef = database.getReference("Receita");

    private ExpandableListView expandableListView;

    private DatabaseReference receitaRef, usuarioRef, paiRef;

    private DatabaseReference filhaRef = database.getReference("Receita");
    private RecyclerView recyclerViewFilhas, recyclerViewPai;
    private RecyclerAdapter adapterFilhas, adapterPai;

    private CircleImageView avatar;

    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef;

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    ListView mDrawerList;
    RelativeLayout mDrawerPane;
    private ActionBarDrawerToggle mDrawerToggle;
    protected DrawerLayout mDrawerLayout;
    private TextView profileTV;
    private TextView user;
    private static String TAG = MainActivity.class.getSimpleName();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_receita);

        NavigationView navView = (NavigationView) findViewById(R.id.navView);
        View headerView = navView.getHeaderView(0);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mDrawerToggle = new ActionBarDrawerToggle(OpenReceitaActivity.this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        expandableListView = (ExpandableListView) findViewById(R.id.expList);

        //recyclerViewFilhas = (RecyclerView) findViewById(R.id.recycler_view_filhas);
        //GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        //recyclerViewFilhas.setLayoutManager(gridLayoutManager);

        //LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //inflate your activity layout here!
        //View contentView = inflater.inflate(R.layout.activity_open_receita, null, false);
        //mDrawerLayout.addView(contentView, 0);

        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


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
        //receitaOriginal = (TextView) findViewById(R.id.receita_original);
        //receitasFilhas = (TextView) findViewById(R.id.receitas_filhas);
        //recyclerViewPai = (RecyclerView) findViewById(R.id.recycler_view_pai);
        //recyclerViewFilhas = (RecyclerView) findViewById(R.id.recycler_view_filhas);


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



        prepareListData();


        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        expandableListView.setAdapter(listAdapter);


        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                Toast.makeText(OpenReceitaActivity.this, "clicou" + listDataHeader.get(groupPosition),
                        Toast.LENGTH_SHORT).show();
                return false;

            }
        });

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Expanded",
                        Toast.LENGTH_SHORT).show();
            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Collapsed",
                        Toast.LENGTH_SHORT).show();

            }
        });


        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition)
                                + ":"
                                + listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition),
                        Toast.LENGTH_SHORT).show();
                Intent i = new Intent(OpenReceitaActivity.this, FilhasActivity.class);
                String tipoFilha = listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition);
                i.putExtra("tipo", tipoFilha);
                i.putExtra("idProprio", idProprio);

                startActivity(i);
                return false;
            }
        });


        profileTV = (TextView) headerView.findViewById(R.id.profileTV);
        profileTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OpenReceitaActivity.this, PerfilUsuarioActivity.class));

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
                    //startActivity(new Intent(OpenReceitaActivity.this, SalvarActivity.class));
                }
                else if(pos == R.id.salvas){
                    startActivity(new Intent(OpenReceitaActivity.this, SalvarActivity.class));
                }
                else if(pos == R.id.sair){
                    auth.signOut();
                    LoginManager.getInstance().logOut();
                    startActivity(new Intent(OpenReceitaActivity.this, LoginActivity.class));
                }
                return false;
            }
        });

        mDrawerLayout.addDrawerListener(mDrawerToggle);

//        Query buscaPai = receitasRef.orderByChild("idProprio").equalTo(idPai);
//        buscaPai.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                ArrayList<Receita> newList = new ArrayList<>();
//                for(DataSnapshot receitaSnapshot : dataSnapshot.getChildren()) {
//                    Receita receita = receitaSnapshot.getValue(Receita.class);
//                    newList.add(receita);
//                    receitaOriginal.setText("Receita Original:");
//
//                }
//                adapterPai = new RecyclerAdapter(newList, OpenReceitaActivity.this);
//                recyclerViewPai.setAdapter(adapterPai);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
//        Query buscaFilhas = receitasRef.orderByChild("idPai").equalTo(idProprio);
//        buscaFilhas.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                ArrayList<Receita> newList = new ArrayList<>();
//                for(DataSnapshot receitaSnapshot : dataSnapshot.getChildren()) {
//                    Receita receita = receitaSnapshot.getValue(Receita.class);
//                    newList.add(receita);
//                    receitasFilhas.setText("Receitas Filhas:");
//
//                }
//                adapterFilhas = new RecyclerAdapter(newList, OpenReceitaActivity.this);
//                recyclerViewFilhas.setAdapter(adapterFilhas);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
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


    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        //valor do pai

        listDataHeader.add("versoes");

        //valor das filhas

        List<String> versoes = new ArrayList<String>();
        versoes.add("Normal");
        versoes.add("Vegetariano");
        versoes.add("Vegano");
        versoes.add("Sem lactose");
        versoes.add("Sem glúten");
        versoes.add("Sem açúcar");

        listDataChild.put(listDataHeader.get(0), versoes);


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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
    }

    /*private void selectItemFromDrawer(int position) {

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
        //setTitle(mNavItems.get(position).mTitle);

        // Close the drawer
        mDrawerLayout.closeDrawer(mDrawerPane);
    }*/

    @Override
    public void onClick(View view) {

    }

}
