package br.senai.sp.cfp132.contagotas.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.TextViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Calendar;

import br.senai.sp.cfp132.contagotas.R;
import br.senai.sp.cfp132.contagotas.modelo.Periodo;
import br.senai.sp.cfp132.contagotas.util.Calculadora;
import br.senai.sp.cfp132.contagotas.util.Calendario;

/**
 * Created by Tecnico Tarde on 26/11/2015.
 */
public class TableResultadoView extends LinearLayout {
    private Context context;
    private TextView tvInicPeriodo;
    private TextView tvFimPeriodo;
    private TextView tvMediaIdeal;
    private TextView tvMediaReal;
    private TextView tvConsumoIdeal;
    private TextView tvConsumoReal;
    private TextView tvValorIdeal;
    private TextView tvValorReal;
    private TableRow trConta;
    private TextView tvConFx1, tvConFx2, tvConFx3, tvConFx4, tvConFx5, tvVlrFx1, tvVlrFx2, tvVlrFx3, tvVlrFx4, tvVlrFx5, tvVlrAg, tvVlrEsg, tvBonMul, tvTarFx1, tvTarFx2, tvTarFx3, tvTarFx4, tvTarFx5;
    private LinearLayout tableConta;
    private TableRow trCabDetConta;

    public TableResultadoView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.context = context;
        init();
    }

    @Override
    public boolean isInEditMode() {
        return true;
    }

    private void init() {
        inflate(context, R.layout.tabela_situacao, this);
        tvInicPeriodo = (TextView) findViewById(R.id.tv_inic_periodo);
        tvFimPeriodo = (TextView) findViewById(R.id.tv_fim_periodo);
        tvMediaIdeal = (TextView) findViewById(R.id.tv_med_dia_ideal);
        tvMediaReal = (TextView) findViewById(R.id.tv_med_dia_real);
        tvConsumoIdeal = (TextView) findViewById(R.id.tv_cons_ideal);
        tvConsumoReal = (TextView) findViewById(R.id.tv_cons_real);
        tvValorIdeal = (TextView) findViewById(R.id.tv_val_ideal);
        tvValorReal = (TextView) findViewById(R.id.tv_val_real);
        tableConta = (LinearLayout) findViewById(R.id.table_conta);
        trCabDetConta = (TableRow) findViewById(R.id.cabecalho_det_conta);
        trConta = (TableRow) findViewById(R.id.table_row_conta);
        trConta.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tableConta.getVisibility() == GONE) {
                    tableConta.setVisibility(VISIBLE);
                    tvValorIdeal.setVisibility(INVISIBLE);
                } else {
                    tableConta.setVisibility(GONE);
                    tvValorIdeal.setVisibility(VISIBLE);
                }
            }
        });
        trCabDetConta.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tableConta.getVisibility() == VISIBLE) {
                    tableConta.setVisibility(GONE);
                    tvValorIdeal.setVisibility(VISIBLE);
                }
            }
        });
        tvConFx1 = (TextView) findViewById(R.id.tv_cons_fx1);
        tvConFx2 = (TextView) findViewById(R.id.tv_cons_fx2);
        tvConFx3 = (TextView) findViewById(R.id.tv_cons_fx3);
        tvConFx4 = (TextView) findViewById(R.id.tv_cons_fx4);
        tvConFx5 = (TextView) findViewById(R.id.tv_cons_fx5);
        tvVlrFx1 = (TextView) findViewById(R.id.tv_valor_fx1);
        tvVlrFx2 = (TextView) findViewById(R.id.tv_valor_fx2);
        tvVlrFx3 = (TextView) findViewById(R.id.tv_valor_fx3);
        tvVlrFx4 = (TextView) findViewById(R.id.tv_valor_fx4);
        tvVlrFx5 = (TextView) findViewById(R.id.tv_valor_fx5);
        tvTarFx1 = (TextView) findViewById(R.id.tv_tar_fx1);
        tvTarFx2 = (TextView) findViewById(R.id.tv_tar_fx2);
        tvTarFx3 = (TextView) findViewById(R.id.tv_tar_fx3);
        tvTarFx4 = (TextView) findViewById(R.id.tv_tar_fx4);
        tvTarFx5 = (TextView) findViewById(R.id.tv_tar_fx5);
        tvVlrAg = (TextView) findViewById(R.id.tv_total_agua);
        tvVlrEsg = (TextView) findViewById(R.id.tv_total_esgoto);
        tvBonMul = (TextView) findViewById(R.id.tv_bonus_multa);
    }

    public void setPeriodo(Periodo periodo) {
        tvInicPeriodo.setText(DateFormat.getDateInstance(DateFormat.SHORT).format(periodo.getDataInicial().getTime()));
        tvFimPeriodo.setText(DateFormat.getDateInstance(DateFormat.SHORT).format(periodo.getDataFinal().getTime()));
        tvInicPeriodo.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(context, Calendario.imagens[periodo.getDataInicial().get(Calendar.DAY_OF_MONTH)-1]), null, null, null);
        tvFimPeriodo.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(context, Calendario.imagens[periodo.getDataFinal().get(Calendar.DAY_OF_MONTH)-1]), null, null, null);
        double medias[] = Calculadora.calculaMedias(periodo);
        tvMediaIdeal.setText(String.format("%3.1f", medias[0]) + getResources().getString(R.string.metro3));
        tvMediaReal.setText(String.format("%3.1f", medias[1]) + getResources().getString(R.string.metro3));
        if (medias[1] > medias[0]) {
            tvMediaReal.setTextColor(ContextCompat.getColor(context, R.color.vermelho));
            tvMediaReal.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(context, R.drawable.copo_vermelho), null, null, null);
        } else if (medias[1] == medias[0]) {
            tvMediaReal.setTextColor(ContextCompat.getColor(context, R.color.amarelo));
            tvMediaReal.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(context, R.drawable.copo_amarelo), null, null, null);
        } else {
            tvMediaReal.setTextColor(ContextCompat.getColor(context, R.color.verde));
            tvMediaReal.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(context, R.drawable.copo_verde), null, null, null);
        }
        tvConsumoIdeal.setText(periodo.getMedia() + getResources().getString(R.string.metro3));
        tvConsumoReal.setText(periodo.getConsumo() + getResources().getString(R.string.metro3));
        if (periodo.getConsumoInt() > periodo.getMedia()) {
            tvConsumoReal.setTextColor(ContextCompat.getColor(context, R.color.vermelho));
            tvConsumoReal.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(context, R.drawable.copo_vermelho), null, null, null);
        } else if (periodo.getConsumoInt() == periodo.getMedia()) {
            tvConsumoReal.setTextColor(ContextCompat.getColor(context, R.color.amarelo));
            tvConsumoReal.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(context, R.drawable.copo_amarelo), null, null, null);
        } else {
            tvConsumoReal.setTextColor(ContextCompat.getColor(context, R.color.verde));
            tvConsumoReal.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(context, R.drawable.copo_verde), null, null, null);
        }
        SharedPreferences preferencias = PreferenceManager
                .getDefaultSharedPreferences(context);
        boolean esgoto = preferencias.getBoolean("esgoto", true);
        int classe = Integer.parseInt(preferencias.getString("classe", "2"));
        double dadosConta[][] = Calculadora.calcularValorDaConta(periodo.getConsumo(), periodo.getMeta(), periodo.getMedia(), esgoto, classe);
        tvValorIdeal.setText(String.format("R$ %6.2f", Calculadora.getIdealConta(periodo.getMeta(),periodo.getMedia(), esgoto, classe)));
        tvValorReal.setText(String.format("R$ %6.2f", dadosConta[8][1]));
        tvConFx1.setText(String.format("%d", (int) dadosConta[0][0]));
        tvTarFx1.setText(String.format("%5.2f", Calculadora.TARIFAS[0][classe]));
        tvVlrFx1.setText(String.format("%5.2f", dadosConta[0][1]));
        tvConFx2.setText(String.format("%d", (int) dadosConta[1][0]));
        tvTarFx2.setText(String.format("%5.2f", Calculadora.TARIFAS[1][classe]));
        tvVlrFx2.setText(String.format("%5.2f", dadosConta[1][1]));
        tvConFx3.setText(String.format("%d", (int) dadosConta[2][0]));
        tvTarFx3.setText(String.format("%5.2f", Calculadora.TARIFAS[2][classe]));
        tvVlrFx3.setText(String.format("%5.2f", dadosConta[2][1]));
        tvConFx4.setText(String.format("%d", (int) dadosConta[3][0]));
        tvTarFx4.setText(String.format("%5.2f", Calculadora.TARIFAS[3][classe]));
        tvVlrFx4.setText(String.format("%5.2f", dadosConta[3][1]));
        tvConFx5.setText(String.format("%d", (int) dadosConta[4][0]));
        tvTarFx5.setText(String.format("%5.2f", Calculadora.TARIFAS[4][classe]));
        tvVlrFx5.setText(String.format("%5.2f", dadosConta[4][1]));
        tvVlrAg.setText(String.format("%5.2f", dadosConta[5][1]));
        tvVlrEsg.setText(String.format("%5.2f", dadosConta[6][1]));
        tvBonMul.setText(String.format("%5.2f", dadosConta[7][1]));
    }
}
