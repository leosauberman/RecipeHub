package com.example.leonardomoraes.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ADM on 22/05/2017.
 */

public class DataBaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "RecipeHub.db";
    public static final String TABLE_NAME = "tabela_Receitas";

    public static final String COL_1 = "ID";
    public static final String COL_2 = "NOME";
    public static final String COL_3 = "INGREDIENTES";
    public static final String COL_4 = "TEMPO";
    public static final String COL_5 = "SABOR";
    public static final String COL_6 = "TIPO";
    public static final String COL_7 = "PREPARO";
    //imagem
    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE " + DATABASE_NAME +
                "(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "NOME TEXT, " +
                "INGREDIENTE TEXT, " +
                "TEMPO INTEGER, " +
                "SABOR TEXT, " +
                "TIPO TEXT, " +
                "PREPARO TEXT)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_NAME);
    }
    public boolean insertData(String nome, String ingredientes, int tempo, String sabor, String tipo, String preparo){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valores  = new ContentValues();
        valores.put(COL_2, nome);
        valores.put(COL_3, ingredientes);
        valores.put(COL_4, tempo);
        valores.put(COL_5, sabor);
        valores.put(COL_6, tipo);
        valores.put(COL_7, preparo);
        long result = db.insert(TABLE_NAME, null, valores);
        db.close();
        if(result == -1){
            return false;
        }
        else{
            return true;
        }
    }
    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM" + TABLE_NAME, null);
        return res;
    }
}
