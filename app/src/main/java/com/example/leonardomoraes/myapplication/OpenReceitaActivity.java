package com.example.leonardomoraes.myapplication;



import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.leonardomoraes.myapplication.R.id.parent;

public class OpenReceitaActivity extends MainActivity {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;


    private ScrollView scroll;
    private TextView nome, ingredientes, tempo, tipo, preparo;
    private int posTipo;
    private String idProprio;
    private Uri imgUri;
    private ProgressDialog progress;
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_receita);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_open_receita, null, false);
        mDrawerLayout.addView(contentView, 0);

        Button version = (Button) findViewById(R.id.version);

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

                nome = (TextView) findViewById(R.id.tv_nomeReceita_Act_openReceita);
                ingredientes = (TextView) findViewById(R.id.tv_ingredientesReceita_Act_openReceita);
                tempo = (TextView) findViewById(R.id.tv_tempoReceita_Act_openReceita);
                tipo = (TextView) findViewById(R.id.tv_tipoReceita_Act_openReceita);
                preparo = (TextView) findViewById(R.id.tv_preparoReceita_Act_openReceita);
                img = (ImageView) findViewById(R.id.imageView_Act_openReceita);
                progress = new ProgressDialog(OpenReceitaActivity.this);


                Intent i = getIntent();
                String nome_string = i.getExtras().getString("nome");
                String ingredientes_string = i.getExtras().getString("ingredientes");
                String tempo_string = i.getExtras().getString("tempo");
                String tipo_string = i.getExtras().getString("tipo");
                String preparo_string = i.getExtras().getString("preparo");

                idProprio = i.getExtras().getString("idProprio");
                posTipo = i.getExtras().getInt("tipoID");

                String image = i.getExtras().getString("uri");
                if (image != null) {
                    imgUri = Uri.parse(image);
                } else {
                    Toast.makeText(this, "Receita sem imagem", Toast.LENGTH_SHORT).show();
                }

                nome.setText(nome_string);
                ingredientes.setText(ingredientes_string);
                tempo.setText(tempo_string);
                tipo.setText(tipo_string);
                preparo.setText(preparo_string);

                Glide.with(OpenReceitaActivity.this).load(imgUri).placeholder(R.drawable.ic_file_download_black_24dp).into(img);


            }

            //pegando a list view
            expListView = (ExpandableListView) findViewById(R.id.expand_lv);

            prepareListData();


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
            });


        }

        private void prepareListData() {
            listDataHeader = new ArrayList<String>();
            listDataChild = new HashMap<String, List<String>>();

                //valor do pai

            listDataHeader.add("numeros");
            listDataHeader.add("letras");

                //valor das filhas

            List<String> numeros = new ArrayList<String>();
            numeros.add("1");
            numeros.add("2");
            numeros.add("3");
            numeros.add("4");
            numeros.add("5");
            numeros.add("6");

            List<String> letras = new ArrayList<String>();
            letras.add("a");
            letras.add("b");
            letras.add("c");
            letras.add("d");
            letras.add("e");

            listDataChild.put(listDataHeader.get(0), numeros);
            listDataChild.put(listDataHeader.get(1), letras);

        }

        @Override
            public void onBackPressed() {
                super.onBackPressed();
                startActivity(new Intent(this, MainActivity.class));
            }
    }
