package com.example.leonardomoraes.myapplication;

/**
 * Created by aiko.oliveira on 18/10/2017.
 */


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
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

import com.example.leonardomoraes.myapplication.*;
import com.example.leonardomoraes.myapplication.PerfilUsuarioActivity;
import com.example.leonardomoraes.myapplication.User;

public abstract class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private ArrayList<User> list;
    Context context;
    DatabaseReference mref;
    String user;

    public UserAdapter(ArrayList<User> users, Context context) {
        list = users;
        this.context = context;
    }

    //@Override
    //public UserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    //    return 0; // new RecyclerView.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_perfil_usuario, parent, false));
    //}

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.userName.setText(list.get(position).getName());
        if(!list.get(position).getImageUrl().isEmpty()){
            Glide.with(context).load(Uri.parse(list.get(position).getImageUrl())).into(holder.userPhoto);
        }
        if(list.get(position).getId().equals(user)){
            holder.followButton.setVisibility(View.GONE);
        }
        else{
            holder.followButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mref.child(list.get(position).getId()).setValue(list.get(position));
                    holder.followButton.setText("Seguindo");
                    //holder.followButton.setBackgroundColor(ContextCompat.getColor(context, R.color.buttonPressed));
                    //holder.followButton.setTextColor(Color.WHITE);
                    notifyDataSetChanged();
                }
            });
            mref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.hasChild(list.get(position).getId())){
                        holder.followButton.setText("seguindo");
                        //holder.followButton.setBackgroundColor(ContextCompat.getColor(context, R.color.buttonPressed));
                        //holder.followButton.setTextColor(Color.WHITE);
                        holder.followButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mref.child(list.get(position).getId()).setValue(list.get(position));
                                holder.followButton.setText("seguir");
                                //holder.followButton.setTextColor(Color.BLACK);
                                //holder.followButton.setBackgroundColor(ContextCompat.getColor(context, R.color.button));
                                holder.followButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mref.child(list.get(position).getId()).setValue(list.get(position));
                                        holder.followButton.setText("seguindo");
                                        //holder.followButton.setTextColor(Color.WHITE);
                                        //holder.followButton.setBackgroundColor(ContextCompat.getColor(context, R.color.buttonPressed));
                                        notifyDataSetChanged();
                                    }
                                });
                            }
                        });

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView userName;
        Button followButton;
        ImageView userPhoto;
        public ViewHolder(View itemView){
            super(itemView);
            mref = FirebaseDatabase.getInstance().getReference("Followings").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            userName = (TextView)itemView.findViewById(R.id.tv_nome_act_perfil_usuario);
            followButton = (Button) itemView.findViewById(R.id.tv_botao_seguir_act_perfil_usuario);
            userPhoto = (ImageView) itemView.findViewById(R.id.profileImage);
            user = FirebaseAuth.getInstance().getCurrentUser().getUid();
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PerfilUsuarioActivity.class);
                    //intent.putExtra("User", list.get(getAdapterPosition()));
                    context.startActivity(intent);
                }
            });
        }
    }
}
