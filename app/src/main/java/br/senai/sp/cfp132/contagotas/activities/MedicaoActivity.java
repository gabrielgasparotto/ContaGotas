package br.senai.sp.cfp132.contagotas.activities;


import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.Calendar;

import br.senai.sp.cfp132.contagotas.R;
import br.senai.sp.cfp132.contagotas.dao.MedicaoDAO;
import br.senai.sp.cfp132.contagotas.modelo.Medicao;
import br.senai.sp.cfp132.contagotas.view.NumberPickView;

public class MedicaoActivity extends AppCompatActivity {
    private Medicao ultimaMedicao;
    private MedicaoDAO daoMedicao;
    private NumberPickView picker;
    private NumberPickView pickerInicial;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        daoMedicao = new MedicaoDAO(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicao);
        pickerInicial = (NumberPickView) findViewById(R.id.number_pick_inic_periodo);
        picker = (NumberPickView) findViewById(R.id.number_pick);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        daoMedicao.close();
    }

    @Override
    protected void onStart() {
        super.onStart();
        pickerInicial.setValorReadOnly(PrincipalActivity.getPeriodoVigente().getValorInicial());
        ultimaMedicao = daoMedicao.getLast(PrincipalActivity.getPeriodoVigente());
        if (ultimaMedicao != null) {
            picker.setValor(ultimaMedicao.getValoratual());
        } else {
            picker.setValor(PrincipalActivity.getPeriodoVigente().getValorInicial());
        }
    }

    public void salvar(View v) {
        int valor = picker.getValor();
        if (ultimaMedicao != null && valor < ultimaMedicao.getValoratual()) {
            Snackbar.make(v, R.string.valida_medicao, Snackbar.LENGTH_SHORT).show();
        } else if (valor < PrincipalActivity.getPeriodoVigente().getValorInicial()) {
            Snackbar.make(v, R.string.valida_medicao, Snackbar.LENGTH_SHORT).show();
        } else {
            Medicao m = new Medicao();
            m.setDataMedicao(Calendar.getInstance());
            m.setPeriodo(PrincipalActivity.getPeriodoVigente());
            m.setValoratual(picker.getValor());
            daoMedicao.incluir(m);
            finish();
        }

    }


}
