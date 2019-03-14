package br.senai.sp.cfp132.contagotas.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.senai.sp.cfp132.contagotas.modelo.Medicao;
import br.senai.sp.cfp132.contagotas.modelo.Periodo;


/**
 * Created by Técnico Tarde on 05/11/2015.
 */
public class MedicaoDAO {
    private DataBaseHelper helper;
    private SQLiteDatabase database;

    public MedicaoDAO(Context context) {
        helper = new DataBaseHelper(context);
    }

    public SQLiteDatabase getDatabase() {
        if (database == null) {
            database = helper.getWritableDatabase();
        }
        return database;
    }

    public void close() {
        helper.close();
        if (database != null && database.isOpen()) {
            database.close();
        }

    }

    public long incluir(Medicao medicao) {
        ContentValues values = new ContentValues();
        values.put("valor", medicao.getValoratual());
        values.put("periodo_id", medicao.getPeriodo().getId());
        values.put("data", medicao.getDataMedicao().getTimeInMillis());
        return getDatabase().insert("medicao", null, values);
    }

    public void excluir(int id) {
        getDatabase().delete("medicao", "_id = ?", new String[]{id + ""});
    }

    public Medicao getLast(Periodo p) {
        Medicao medicao = null;
        Cursor cursor = getDatabase().rawQuery("SELECT * FROM medicao WHERE periodo_id = ? ORDER BY data desc LIMIT 1", new String[]{p.getId() + ""});
        if (cursor.moveToFirst()) {
            medicao = new Medicao();
            medicao.setId(cursor.getInt(cursor.getColumnIndex("_id")));
            Calendar data = Calendar.getInstance();
            data.setTimeInMillis(cursor.getLong(cursor.getColumnIndex("data")));
            medicao.setDataMedicao(data);
            medicao.setPeriodo(p);
            medicao.setValoratual(cursor.getInt(cursor.getColumnIndex("valor")));
        }
        cursor.close();
        return medicao;
    }

/*
    public List<Medicao> listar() {
        List<Medicao> lista = new ArrayList<Medicao>();
        Cursor cursor = getDatabase().rawQuery("SELECT * FROM medicao COLLATE NOCASE ORDER BY data DESC", null);

        while (cursor.moveToNext()) {
            Medicao medicao = new Medicao();
            medicao.setId(cursor.getInt(cursor.getColumnIndex("_id")));
            medicao.setValoratual(cursor.getInt(cursor.getColumnIndex("valor")));

            Periodo per = new Periodo();
            per.setId(cursor.getInt(cursor.getColumnIndex("periodo_id")));
            Calendar dataMedicao = Calendar.getInstance();
            dataMedicao.setTimeInMillis(cursor.getLong(cursor.getColumnIndex("data")));
            medicao.setDataMedicao(dataMedicao);
            medicao.setPeriodo(per);
            lista.add(medicao);
        }
		cursor.close();
        return lista;
    }
    */
}
