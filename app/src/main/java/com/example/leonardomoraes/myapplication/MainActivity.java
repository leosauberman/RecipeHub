package com.example.leonardomoraes.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button add1, menu1;
    private TextView receitaFeed, tipoTextView, sdTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        receitaFeed = (TextView) findViewById(R.id.receitaFeed);
        tipoTextView = (TextView) findViewById(R.id.tipoTextView);
        sdTextView = (TextView) findViewById(R.id.sdTextView);

        add1 = (Button) findViewById(R.id.addRecipe);
        add1.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v){
                startActivity(new Intent(MainActivity.this, TelaReceita.class));
            }
        });

        Intent intent = getIntent();
        if (intent != null) {
            Bundle params = intent.getExtras();
            if (params != null) {
                String nome = params.getString("nome");
                String tipo = params.getString("tipo");
                String sOUd = params.getString("sOUd");
                String string1 = getString(R.string.nome);
                String string2 = getString(R.string.tipo);

                receitaFeed.setText(string1+ " " + nome);
                tipoTextView.setText(string2+ " " + tipo);
                sdTextView.setText(sOUd);
            }
            else{
                receitaFeed.setText(getString(R.string.nome));
                tipoTextView.setText(getString(R.string.tipo));
                sdTextView.setText(" ");
            }
        }
    }
    protected void onActivityResult(int codigoTela, int resultado, Intent intent){
        if(codigoTela == 1) {
            Bundle params = intent.getExtras();
            if (params != null) {
                String msg = params.getString("msg");
                Toast.makeText(this, "TelaReceita -> Resultado: " + resultado + " | Msg: " + msg, Toast.LENGTH_LONG).show();
            }
        }
    }
}