package org.ort.leonardomoraes.recipehub;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    private String recipeAutor;
    private String recipeIdPai;
    private String recipeIdDono;
    private int posTipo;
    private String recipeId;
    private Context c;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private DatabaseReference myRef = database.getReference("Usuario");
    private DatabaseReference myRef2;
    private Uri downloadUrl;
    private ArrayList<String> receitaIdArray;

    class RecyclerHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //ItemClickListener itemClickListener;
        TextView nome = (TextView) itemView.findViewById(R.id.tv_nomeReceita_Act_recyclerChild);
        TextView tempo = (TextView) itemView.findViewById(R.id.tv_tempoReceita_Act_recyclerChild);
        TextView tipo = (TextView) itemView.findViewById(R.id.tv_tipoReceita_Act_recyclerChild);
        ImageView foto = (ImageView) itemView.findViewById(R.id.imageView_Act_recyclerChild);
        TextView autor = (TextView) itemView.findViewById(R.id.tv_nomeAutor_Act_recyclerChild);
        ImageButton salvar = (ImageButton) itemView.findViewById(R.id.empty_star);
        ImageButton desalvar = (ImageButton) itemView.findViewById(R.id.full_star);


        RecyclerHolder(View itemView) {
            super(itemView);
            itemView.setTag(itemView);
            itemView.setOnClickListener(this);

            receitaIdArray = new ArrayList<>();

            myRef2 = myRef.child(auth.getCurrentUser().getUid());

            myRef2.child("salvas").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot salvasSnapshot : dataSnapshot.getChildren()) {
                        String salvas = salvasSnapshot.getValue(String.class);

                        receitaIdArray.add(salvas);
                        final int pos = getAdapterPosition();
                        try {
                            recipeId = receitaArrayList.get(pos).getIdProprio();
                            recipeNome = receitaArrayList.get(pos).getNome();

                            if (receitaIdArray.contains(recipeId)) {
                                salvar.setVisibility(View.GONE);
                                desalvar.setVisibility(View.VISIBLE);
                            } else {
                                salvar.setVisibility(View.VISIBLE);
                                desalvar.setVisibility(View.GONE);
                            }

                            salvar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    recipeId = receitaArrayList.get(pos).getIdProprio();
                                    recipeNome = receitaArrayList.get(pos).getNome();
                                    myRef2.child("salvas").child(recipeNome).setValue(recipeId);
                                    salvar.setVisibility(View.GONE);
                                    desalvar.setVisibility(View.VISIBLE);
                                }
                            });

                            desalvar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    recipeId = receitaArrayList.get(pos).getIdProprio();
                                    recipeNome = receitaArrayList.get(pos).getNome();
                                    myRef2.child("salvas").child(recipeNome).removeValue();
                                    desalvar.setVisibility(View.GONE);
                                    salvar.setVisibility(View.VISIBLE);
                                }
                            });
                        }catch (Exception e){
                            System.out.println("Erro:" + e);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            myRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(!dataSnapshot.hasChild("salvas")) {
                        salvar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int pos = getAdapterPosition();
                                recipeId = receitaArrayList.get(pos).getIdProprio();
                                recipeNome = receitaArrayList.get(pos).getNome();
                                myRef2.child("salvas").child(recipeNome).setValue(recipeId);
                                salvar.setVisibility(View.GONE);
                                desalvar.setVisibility(View.VISIBLE);
                            }
                        });

                        desalvar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int pos = getAdapterPosition();
                                recipeId = receitaArrayList.get(pos).getIdProprio();
                                recipeNome = receitaArrayList.get(pos).getNome();
                                myRef2.child("salvas").child(recipeNome).removeValue();
                                desalvar.setVisibility(View.GONE);
                                salvar.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                    else{
                        return;
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

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
            //intent.putExtra("autor", recipeAutor);
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
        holder.tempo.setText(receitaArrayList.get(position).getTempo() + " min");
        holder.tipo.setText(receitaArrayList.get(position).getTipo());
        recipeUri = receitaArrayList.get(position).getUrlFoto();

        recipeIdDono = receitaArrayList.get(position).getIdDono();

        myRef.child(recipeIdDono).child("nome").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                recipeAutor = dataSnapshot.getValue(String.class);
                holder.autor.setText(recipeAutor);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


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