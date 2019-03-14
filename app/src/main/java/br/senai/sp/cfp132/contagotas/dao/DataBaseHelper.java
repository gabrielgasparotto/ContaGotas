package br.senai.sp.cfp132.contagotas.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Técnico Tarde on 05/11/2015.
 */
public class DataBaseHelper extends SQLiteOpenHelper {
    private static final String BANCO_DADOS = "ContaGotas";
    private static final int versao = 1;

    public DataBaseHelper(Context context) {
        super(context, BANCO_DADOS, null, versao);
    }


    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()){
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE periodo(_id INTEGER PRIMARY KEY,data_inicial INTEGER,valor_inicial INTEGER,media INTEGER, meta INTEGER, data_final INTEGER)");
        db.execSQL("CREATE TABLE medicao(_id INTEGER PRIMARY KEY,periodo_id INTEGER, valor INTEGER, data INTEGER,"
                + " FOREIGN KEY(periodo_id) REFERENCES periodo(_id) ON DELETE CASCADE)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}
