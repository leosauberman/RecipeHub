package com.example.leonardomoraes.myapplication;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
        TextView nome = (TextView) itemView.findViewById(R.id.nomeTV);
        TextView tempo = (TextView) itemView.findViewById(R.id.tempoTV);
        TextView tipo = (TextView) itemView.findViewById(R.id.tipoTV);
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
