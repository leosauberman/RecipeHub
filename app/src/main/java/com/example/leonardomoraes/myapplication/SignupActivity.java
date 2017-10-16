package com.example.leonardomoraes.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SignupActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword, inputName;
    private Button bt_SignIn, bt_SignUp, bt_ResetaSenha;
    private TextView titulo;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("Usuario");
    private String id, idReceitas;
    private User user;
    private static final String TAG = "Android Bash";

    protected void setUpUser(){
        user = new User();
        user.setName(inputName.getText().toString());
        user.setEmail(inputEmail.getText().toString());
    }

    private void createNewAccount(String email, String password){
        Log.d(TAG, "createNewAccount: " + email);
        if(!validateForm()){
            return;
        }
        setUpUser(); //create user.Name and user.Email
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        Log.d(TAG, "createUserWithEmail:onComplete: " + task.isSuccessful());

                        if(!task.isSuccessful()){
                            Toast.makeText(SignupActivity.this, "Auth Failed. Maybe you're already registered.", Toast.LENGTH_SHORT).show();
                        }else{
                            onAuthenticationSuccess(task.getResult().getUser());
                        }
                    }
                });
    }

    private void onAuthenticationSuccess(FirebaseUser myUser){
        saveNewUser(myUser.getUid(), user.getName(), user.getEmail(), null);

        startActivity(new Intent(SignupActivity.this, MainActivity.class));
        finish();
    }

    private void signOut() {
        auth.signOut();
    }

    private void saveNewUser(String userId, String name, String email, String imageUrl) {
        User user = new User(userId, name, email, imageUrl, null);

        myRef.child(userId).child("nome").setValue(user.getName());
    }

    private boolean validateForm() {
        boolean valid = true;

        String userEmail = inputEmail.getText().toString();
        if(TextUtils.isEmpty(userEmail)){
            inputEmail.setError("Required");
            valid = false;
        }else if(!isValidEmail(userEmail)){
            inputEmail.setError("E-mail invalido");
            valid = false;
        }else{
            inputEmail.setError(null);
        }

        String userName = inputName.getText().toString();
        if(TextUtils.isEmpty(userName)){
            inputName.setError("Required");
            valid = false;
        }else{
            inputName.setError(null);
        }



        String userPassword = inputPassword.getText().toString();
        if(TextUtils.isEmpty(userPassword)) {
            inputPassword.setError("Required");
            valid = false;
        }else if(userPassword.length() < 6){
            inputPassword.setError("Senha muito curta");
            valid = false;
        }else{
            inputPassword.setError(null);
        }

        return valid;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singup);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        bt_SignIn = (Button) findViewById(R.id.bt_singIn_Act_singUp);
        bt_SignUp = (Button) findViewById(R.id.bt_singUp_Act_singUp);
        inputEmail = (EditText) findViewById(R.id.et_emailSingUp);
        inputPassword = (EditText) findViewById(R.id.et_senhaSingUp);
        inputName = (EditText) findViewById(R.id.et_nomeSingUp);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        bt_ResetaSenha = (Button) findViewById(R.id.bt_resetaSenha_Act_singUp);
        titulo = (TextView) findViewById(R.id.tv_titulo_Act_singup);

        bt_ResetaSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, ResetPasswordActivity.class));
            }
        });

        bt_SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        bt_SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBar.setVisibility(View.VISIBLE);
                //create user
                createNewAccount(inputEmail.getText().toString(), inputPassword.getText().toString());
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

}