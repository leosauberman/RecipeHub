package com.example.leonardomoraes.myapplication;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
    Adapter do recylcerView
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private ArrayList<Receita> receitaArrayList;

    public RecyclerAdapter(ArrayList<Receita> receitaArrayList) {
        this.receitaArrayList = receitaArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_child, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.nome.setText(receitaArrayList.get(position).getNome());
        holder.tempo.setText(receitaArrayList.get(position).getTempo());
        holder.tipo.setText(receitaArrayList.get(position).getTipo());
    }

    @Override
    public int getItemCount() {
        return receitaArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView nome = (TextView) itemView.findViewById(R.id.tv_nomeReceita_Act_telaReceita);
        TextView tempo = (TextView) itemView.findViewById(R.id.tv_tempoReceita_Act_telaReceita);
        TextView tipo = (TextView) itemView.findViewById(R.id.tv_tipoReceita_Act_telaReceita);
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
