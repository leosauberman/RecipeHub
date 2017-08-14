package com.example.leonardomoraes.myapplication;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
    Adapter do recylcerView
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerHolder> {

    private ArrayList<Receita> receitaArrayList;
    private String recipeNome;
    private String recipeIngredientes;
    private String recipeTempo;
    private String recipePreparo;
    private String recipeTipo;
    private String recipeUrl;
    Context context;


    public RecyclerAdapter(ArrayList<Receita> receitaArrayList, Context c) {
        this.receitaArrayList = receitaArrayList;
        this.context = c;
    }

    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecyclerHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_child, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerHolder holder, int position) {
        holder.nome.setText(receitaArrayList.get(position).getNome());
        holder.tempo.setText(receitaArrayList.get(position).getTempo());
        holder.tipo.setText(receitaArrayList.get(position).getTipo());

        recipeNome = receitaArrayList.get(position).getNome();
        recipeIngredientes = receitaArrayList.get(position).getIngrediente();
        recipeTempo = receitaArrayList.get(position).getTempo();
        recipePreparo = receitaArrayList.get(position).getPreparo();
        recipeTipo = receitaArrayList.get(position).getTipo();
        recipeUrl = receitaArrayList.get(position).getUrlFoto();

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                openActivity();
                Toast.makeText(context, "TA ROLANDO TIOOOW", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return receitaArrayList.size();
    }

    private void openActivity(){
        Intent intent = new Intent(context, OpenReceitaActivity.class);
        intent.putExtra("nome", recipeNome);
        intent.putExtra("ingredientes", recipeIngredientes);
        intent.putExtra("tempo", recipeTempo);
        intent.putExtra("preparo", recipePreparo);
        intent.putExtra("tipo", recipeTipo);
        intent.putExtra("url", recipeUrl);
        context.startActivity(intent);
    }
}
