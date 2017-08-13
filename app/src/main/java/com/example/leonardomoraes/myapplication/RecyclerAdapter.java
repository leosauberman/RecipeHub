package com.example.leonardomoraes.myapplication;

import android.content.Context;
import android.content.Intent;
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

        recipeNome = receitaArrayList.get(holder.getAdapterPosition()).getNome();
        recipeIngredientes = receitaArrayList.get(holder.getAdapterPosition()).getIngrediente();
        recipeTempo = receitaArrayList.get(holder.getAdapterPosition()).getTempo();
        recipePreparo = receitaArrayList.get(holder.getAdapterPosition()).getPreparo();
        recipeTipo = receitaArrayList.get(holder.getAdapterPosition()).getTipo();

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                openActivity();
                Toast.makeText(context, "BKJVKGC", Toast.LENGTH_SHORT).show();
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
        context.startActivity(intent);
    }
}
