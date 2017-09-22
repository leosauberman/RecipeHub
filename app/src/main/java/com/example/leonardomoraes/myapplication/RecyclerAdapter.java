package com.example.leonardomoraes.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

/**
    Adapter do recylcerView
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder> {

    private ArrayList<Receita> receitaArrayList;
    private String recipeNome;
    private String recipeIngredientes;
    private String recipeTempo;
    private String recipePreparo;
    private String recipeTipo;
    private String recipeUri;
    //private ArrayList<String> recipeNomeUsuario = new ArrayList<>();
    private String recipeIdPai;
    private String recipeIdDono;
    private int posTipo;
    private String recipeId;
    private Context c;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private DatabaseReference myRef = database.getReference("Usuario");

    private Uri downloadUrl;

    class RecyclerHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //ItemClickListener itemClickListener;
        TextView nome = (TextView) itemView.findViewById(R.id.tv_nomeReceita_Act_recyclerChild);
        TextView tempo = (TextView) itemView.findViewById(R.id.tv_tempoReceita_Act_recyclerChild);
        TextView tipo = (TextView) itemView.findViewById(R.id.tv_tipoReceita_Act_recyclerChild);
        ImageView foto = (ImageView) itemView.findViewById(R.id.imageView_Act_recyclerChild);
        TextView autor = (TextView) itemView.findViewById(R.id.tv_nomeAutor_Act_recyclerChild);


        RecyclerHolder(View itemView) {
            super(itemView);

            itemView.setTag(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();

            recipeNome = receitaArrayList.get(pos).getNome();
            recipeIngredientes = receitaArrayList.get(pos).getIngrediente();
            recipeTempo = receitaArrayList.get(pos).getTempo();
            recipePreparo = receitaArrayList.get(pos).getPreparo();
            recipeTipo = receitaArrayList.get(pos).getTipo();
            posTipo = receitaArrayList.get(pos).getTipo().indexOf(recipeTipo);
            recipeUri = receitaArrayList.get(pos).getUrlFoto();
            recipeId = receitaArrayList.get(pos).getIdProprio();
            recipeIdDono = receitaArrayList.get(pos).getIdDono();
            recipeIdPai = receitaArrayList.get(pos).getIdPai();

            Context context = itemView.getContext();
            Intent intent = new Intent(context, OpenReceitaActivity.class);
            intent.putExtra("nome", recipeNome);
            intent.putExtra("ingredientes", recipeIngredientes);
            intent.putExtra("tempo", recipeTempo);
            intent.putExtra("preparo", recipePreparo);
            intent.putExtra("tipo", recipeTipo);
            intent.putExtra("tipoID", posTipo);
            intent.putExtra("idProprio", recipeId);
            intent.putExtra("uri", recipeUri);
            intent.putExtra("idDono", recipeIdDono);
            intent.putExtra("idPai", recipeIdPai);
            context.startActivity(intent);
        }
    }

    public RecyclerAdapter(ArrayList<Receita> receitaArrayList, Context c) {
        this.receitaArrayList = receitaArrayList;
        this.c = c;
    }

    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecyclerHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_child, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerHolder holder, final int position) {
        holder.nome.setText(receitaArrayList.get(position).getNome());
        holder.tempo.setText(receitaArrayList.get(position).getTempo());
        holder.tipo.setText(receitaArrayList.get(position).getTipo());
        recipeUri = receitaArrayList.get(position).getUrlFoto();

        recipeIdDono = receitaArrayList.get(position).getIdDono();

//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(c);
//        recipeNomeUsuario = sharedPreferences.getString("nomeUsuario","default_string");
        //recipeNomeUsuario = myRef.child(recipeIdDono).child();
        //holder.autor.setText(recipeNomeUsuario);

        if (recipeUri != null) {
            downloadUrl = Uri.parse(recipeUri);
        }
        else{
            holder.foto.setImageResource(R.drawable.no_image);
        }
        Glide.with(c).load(downloadUrl).placeholder(R.drawable.no_image).into(holder.foto);
    }
    @Override
    public int getItemCount() {
        return receitaArrayList.size();
    }

}
