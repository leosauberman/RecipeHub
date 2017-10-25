package org.ort.leonardomoraes.recipehub;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.ort.leonardomoraes.recipehub.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class OpenPerfilActivity extends AppCompatActivity {

    private String id, userId, userName, userEmail, userImageUrl;
    private FirebaseDatabase database;
    private DatabaseReference mref;
    private Button botaoseguir;
    private CircleImageView fotoPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_perfil);
        database = FirebaseDatabase.getInstance();
        DatabaseReference db = database.getReference("Seguidas").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Intent intent = getIntent();
        userName = (String) intent.getSerializableExtra("userName");
        userEmail = (String) intent.getSerializableExtra("userEmail");
        userId = (String) intent.getSerializableExtra("userId");
        userImageUrl = (String) intent.getSerializableExtra("userImage");

        fotoPerfil = (CircleImageView)findViewById(R.id.imagemOpenPerfil);
        botaoseguir = (Button)findViewById(R.id.botaoseguir);


        if(!userImageUrl.isEmpty()){
            Glide.with(OpenPerfilActivity.this).load(Uri.parse(userImageUrl)).into(fotoPerfil);
        }
        if(userId.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
            botaoseguir.setVisibility(View.GONE);
        }else{
            //database.
        }
    }
}
