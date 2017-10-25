package org.ort.leonardomoraes.recipehub;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import de.hdodenhof.circleimageview.CircleImageView;


import org.ort.leonardomoraes.recipehub.OpenPerfilActivity;import org.ort.leonardomoraes.recipehub.R;import org.ort.leonardomoraes.recipehub.User;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by aiko2 on 21/10/2017.
 */

public class UsuarioAdapter extends RecyclerView.Adapter<UsuarioAdapter.RecyclerHolder>{
    private ArrayList<User> userArrayList;
    private String userName, userEmail, userImage, userId, userSeguindo, userSeguidores;
    private Context c;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference database;

    class RecyclerHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView profileName = (TextView) itemView.findViewById(R.id.nome_recyclerUser);
        CircleImageView profileImage = (CircleImageView) itemView.findViewById(R.id.imageView_recyclerUser);
        Button followButton = (Button) itemView.findViewById(R.id.botaoSeguir_recyclerUser);
        //TextView seguindo = (TextView) itemView.findViewById(R.id.tv_seguindo_act_perfil_usuario);
        //TextView seguidores = (TextView) itemView.findViewById(R.id.tv_seguidores_act_perfil_usuario);

        RecyclerHolder(View itemView) {
            super(itemView);


            itemView.setTag(itemView);
            itemView.setOnClickListener(this);
            database = FirebaseDatabase.getInstance().getReference("Seguidas").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            User user;
            userName = userArrayList.get(pos).getName();
            userEmail = userArrayList.get(pos).getEmail();
            userId = userArrayList.get(pos).getId();
            userImage = userArrayList.get(pos).getImageUrl();
            user = userArrayList.get(pos);

            Context context = itemView.getContext();
            Intent intent = new Intent(context, OpenPerfilActivity.class);
            intent.putExtra("userName", user.getName());
            intent.putExtra("userEmail", user.getEmail());
            intent.putExtra("userId", user.getId());
            intent.putExtra("userImage", user.getImageUrl());
            context.startActivity(intent);
        }
    }

    public UsuarioAdapter(ArrayList<User> userArrayList, Context c) {
        this.userArrayList = userArrayList;
        this.c = c;
    }

    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecyclerHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_user, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerHolder holder, final int position) {
        holder.profileName.setText(userArrayList.get(position).getName());
        String a = userArrayList.get(position).getId();
        String b = FirebaseAuth.getInstance().getCurrentUser().getUid();


        if(!userArrayList.get(position).getImageUrl().isEmpty()){
            Glide.with(c).load(Uri.parse(userArrayList.get(position).getImageUrl())).into(holder.profileImage);
        }
        if(userArrayList.get(position).getId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
            holder.followButton.setVisibility(View.GONE);
        }else{
            database.child(userArrayList.get(position).getId()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(final DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        holder.followButton.setText("seguindo");
                        holder.followButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                database.child(userArrayList.get(position).getId()).removeValue();
                            }
                        });
                    }else{
                        holder.followButton.setText("Seguir");
                        holder.followButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                database.child(userArrayList.get(position).getId()).setValue(userArrayList.get(position));
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
        return userArrayList.size();
    }
}
