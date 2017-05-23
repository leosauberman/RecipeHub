package com.example.leonardomoraes.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ListViewCompat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button add1;
    private ListView listaReceitas;
    private List<String> feed = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        DataBaseHelper myDB = new DataBaseHelper(this);
        Cursor res = myDB.getAllData();
        if(res != null && res.getCount() > 0) {
            while (res.moveToNext()) {
                feed.add("Nome: " + res.getString(1));
                feed.add("Tempo: " + res.getString(3));
                feed.add("Sabor: " + res.getString(4));
                feed.add("Tipo: " + res.getString(5));
            }
            Toast.makeText(this, "TOMA OS DADO AE", Toast.LENGTH_LONG);
        }
        else{
            Toast.makeText(this, "No hay datos", Toast.LENGTH_LONG);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getApplicationContext(),
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                feed
        );
        listaReceitas.setAdapter(adapter);
        add1 = (Button) findViewById(R.id.addRecipe);
        add1.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v){
                startActivity(new Intent(MainActivity.this, TelaReceita.class));
            }
        });
    }
}