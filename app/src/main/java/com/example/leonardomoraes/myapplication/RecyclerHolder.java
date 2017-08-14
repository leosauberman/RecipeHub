package com.example.leonardomoraes.myapplication;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by ADM on 13/08/2017.
 */

public class RecyclerHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    ItemClickListener itemClickListener;
    TextView nome = (TextView) itemView.findViewById(R.id.tv_nomeReceita_Act_telaReceita);
    TextView tempo = (TextView) itemView.findViewById(R.id.tv_tempoReceita_Act_telaReceita);
    TextView tipo = (TextView) itemView.findViewById(R.id.tv_tipoReceita_Act_telaReceita);

    public RecyclerHolder(View itemView) {
        super(itemView);
        itemView.setTag(itemView);
        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener)
    {
        this.itemClickListener=itemClickListener;
    }

    @Override
    public void onClick(View v) {
        this.itemClickListener.onItemClick(this.getLayoutPosition());
    }
}
