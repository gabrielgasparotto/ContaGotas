package br.senai.sp.cfp132.contagotas.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import br.senai.sp.cfp132.contagotas.R;
import br.senai.sp.cfp132.contagotas.adapter.HistoricoPeriodoAdapter;
import br.senai.sp.cfp132.contagotas.dao.PeriodoDAO;
import br.senai.sp.cfp132.contagotas.modelo.Periodo;

/**
 * Created by Tecnico_Tarde on 18/11/2015.
 */
public class HistoricoPeriodoActitvity extends AppCompatActivity implements DialogInterface.OnClickListener {

    ListView periodos;
    List<Periodo> listaperiodo;
    PeriodoDAO daoperiodo;
    HistoricoPeriodoAdapter adapter;
    Periodo periodo;
    AlertDialog dialog;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        daoperiodo = new PeriodoDAO(this);
        setContentView(R.layout.activity_historico);

        periodos = (ListView) findViewById(R.id.lista_historioco);
        //listaperiodo = daoperiodo.listar();

        registerForContextMenu(periodos);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.confirma_excl_periodo);
        builder.setPositiveButton(R.string.sim, this);
        builder.setNegativeButton(R.string.nao, this);
        dialog = builder.create();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter = new HistoricoPeriodoAdapter(daoperiodo.listar());
        periodos.setAdapter(adapter);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.menu_contexto_periodo, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        position = info.position;
        periodo = (Periodo) adapter.getItem(position);
        switch (item.getItemId()) {
            case R.id.excluir_periodo:
                dialog.show();
                break;
            case R.id.exibir_medicoes:
                Intent intent = new Intent(this, HistoricoMedicaoActivity.class);
                intent.putExtra("periodo", periodo);
                startActivity(intent);
                break;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        daoperiodo.close();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                daoperiodo.excluir(periodo.getId());
                adapter.remove(position);
                periodos.invalidateViews();
                if (position == 0) {
                    PrincipalActivity.setPeriodoVigente(null);
                }
                position = -1;
                break;
        }
    }
}
