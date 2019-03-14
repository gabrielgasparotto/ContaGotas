package br.senai.sp.cfp132.contagotas.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.senai.sp.cfp132.contagotas.modelo.Medicao;
import br.senai.sp.cfp132.contagotas.modelo.Periodo;


/**
 * Created by Técnico Tarde on 09/11/2015.
 */
public class PeriodoDAO {
    private DataBaseHelper helper;
    private SQLiteDatabase database;

    public PeriodoDAO(Context context) {
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

    public long incluir(Periodo periodo) {
        ContentValues values = new ContentValues();
        values.put("data_inicial", periodo.getDataInicial().getTimeInMillis());
        values.put("valor_inicial", periodo.getValorInicial());
        values.put("meta", periodo.getMeta());
        values.put("media",periodo.getMedia());
        values.put("data_final", periodo.getDataFinal().getTimeInMillis());
        return getDatabase().insert("periodo", null, values);
    }

    public void excluir(long id) {
        getDatabase().delete("periodo", "_id = ?", new String[]{id + ""});
    }

    public List<Periodo> listar() {
        List<Periodo> lista = new ArrayList<Periodo>();
        Cursor cursor = getDatabase().rawQuery("select p._id periodo_id, p.data_inicial, p.data_final,p.media, p.meta, p.valor_inicial, m._id medicao_id, m.valor, m.data data_medicao from periodo p left join medicao m on m.periodo_id = p._id ORDER BY p._id DESC, m._id DESC", null);
        while (cursor.moveToNext()) {
            Periodo periodo = new Periodo();
            periodo.setId(cursor.getInt(cursor.getColumnIndex("periodo_id")));
            Calendar data_inicial = Calendar.getInstance();
            data_inicial.setTimeInMillis(cursor.getLong(cursor.getColumnIndex("data_inicial")));
            periodo.setDataInicial(data_inicial);
            Calendar data_final = Calendar.getInstance();
            data_final.setTimeInMillis(cursor.getLong(cursor.getColumnIndex("data_final")));
            periodo.setDataFinal(data_final);
            periodo.setMeta(cursor.getInt(cursor.getColumnIndex("meta")));
            periodo.setMedia(cursor.getInt(cursor.getColumnIndex("media")));
            periodo.setValor_inicial(cursor.getInt(cursor.getColumnIndex("valor_inicial")));

            do {
                if (cursor.getInt(cursor.getColumnIndex("periodo_id")) == periodo.getId()) {
                    if (!cursor.isNull(cursor.getColumnIndex("medicao_id"))) {
                        Medicao m = new Medicao();
                        m.setId(cursor.getInt(cursor.getColumnIndex("medicao_id")));
                        m.setPeriodo(periodo);
                        m.setValoratual(cursor.getInt(cursor.getColumnIndex("valor")));
                        Calendar data_medicao = Calendar.getInstance();
                        data_medicao.setTimeInMillis(cursor.getLong(cursor.getColumnIndex("data_medicao")));
                        m.setDataMedicao(data_medicao);
                        periodo.getMedicoes().add(m);
                    }
                } else {
                    cursor.moveToPrevious();
                    break;
                }
            } while (cursor.moveToNext());
            lista.add(periodo);
        }
        cursor.close();
        return lista;
    }


    public Periodo buscarVigente() {
        Periodo periodo = null;
        Cursor cursor = getDatabase().rawQuery("select p._id periodo_id, p.data_inicial, p.data_final,p.media, p.meta, p.valor_inicial, m._id medicao_id, m.valor, m.data data_medicao from periodo p left join medicao m on m.periodo_id = p._id order by p._id desc, m._id desc", null);
        if (cursor.moveToNext()) {
            periodo = new Periodo();
            periodo.setId(cursor.getInt(cursor.getColumnIndex("periodo_id")));
            Calendar data_inicial = Calendar.getInstance();
            data_inicial.setTimeInMillis(cursor.getLong(cursor.getColumnIndex("data_inicial")));
            periodo.setDataInicial(data_inicial);
            Calendar data_final = Calendar.getInstance();
            data_final.setTimeInMillis(cursor.getLong(cursor.getColumnIndex("data_final")));
            periodo.setDataFinal(data_final);
            periodo.setMeta(cursor.getInt(cursor.getColumnIndex("meta")));
            periodo.setMedia(cursor.getInt(cursor.getColumnIndex("media")));
            periodo.setValor_inicial(cursor.getInt(cursor.getColumnIndex("valor_inicial")));
            do {
                if (cursor.getInt(cursor.getColumnIndex("periodo_id")) == periodo.getId()) {
                    if (!cursor.isNull(cursor.getColumnIndex("medicao_id"))) {
                        Medicao m = new Medicao();
                        m.setId(cursor.getInt(cursor.getColumnIndex("medicao_id")));
                        m.setPeriodo(periodo);
                        m.setValoratual(cursor.getInt(cursor.getColumnIndex("valor")));
                        Calendar data_medicao = Calendar.getInstance();
                        data_medicao.setTimeInMillis(cursor.getLong(cursor.getColumnIndex("data_medicao")));
                        m.setDataMedicao(data_medicao);
                        periodo.getMedicoes().add(m);
                    }
                } else {
                    cursor.moveToPrevious();
                    break;
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return periodo;
    }


    public int getUltimaMedicao() {
        int valor = 0;
        Cursor cursor = getDatabase().rawQuery("SELECT * FROM medicao ORDER BY data desc LIMIT 1", null);
        if (cursor.moveToFirst()) {
            valor = cursor.getInt(cursor.getColumnIndex("valor"));
        }
        cursor.close();
        return valor;
    }
}
