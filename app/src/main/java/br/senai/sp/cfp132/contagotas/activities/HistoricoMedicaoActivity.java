package br.senai.sp.cfp132.contagotas.activities;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import br.senai.sp.cfp132.contagotas.R;
import br.senai.sp.cfp132.contagotas.adapter.HistoricoMedicaoAdapter;
import br.senai.sp.cfp132.contagotas.dao.MedicaoDAO;
import br.senai.sp.cfp132.contagotas.modelo.Medicao;
import br.senai.sp.cfp132.contagotas.modelo.Periodo;

public class HistoricoMedicaoActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, DialogInterface.OnClickListener {

    ListView medicoes;
    MedicaoDAO medicaoDAO = new MedicaoDAO(this);
    private AlertDialog dialog;
    Medicao medicao;
    Periodo periodo;
    int position;

    HistoricoMedicaoAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        periodo = (Periodo) getIntent().getSerializableExtra("periodo");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico);
        medicoes = (ListView) findViewById(R.id.lista_historioco);
        medicoes.setAdapter(adapter = new HistoricoMedicaoAdapter(periodo.getMedicoes()));

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.confirma_excl_medicao);
        builder.setNegativeButton(R.string.nao, this);
        builder.setPositiveButton(R.string.sim, this);
        dialog = builder.create();
        registerForContextMenu(medicoes);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        medicao = (Medicao) parent.getAdapter().getItem(position);
        dialog.show();

    }


    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                medicaoDAO.excluir(medicao.getId());
                adapter.remove(position);
                //medicoes.notifyAll();
                medicoes.invalidateViews();
                break;
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        position = info.position;
        medicao = (Medicao) adapter.getItem(position);
        switch (item.getItemId()) {
            case R.id.excluir_medicao:
                dialog.show();
                break;
        }
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.menu_contexto_medicoes, menu);
    }
}
