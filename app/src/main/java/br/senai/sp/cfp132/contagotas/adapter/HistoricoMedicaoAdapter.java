package br.senai.sp.cfp132.contagotas.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;

import br.senai.sp.cfp132.contagotas.R;
import br.senai.sp.cfp132.contagotas.modelo.Medicao;
import br.senai.sp.cfp132.contagotas.util.Calendario;
import br.senai.sp.cfp132.contagotas.view.NumberPickView;


public class HistoricoMedicaoAdapter extends BaseAdapter {
    List<Medicao> medicoes;


    public HistoricoMedicaoAdapter(List<Medicao> medicoes) {
        this.medicoes = medicoes;
        Log.w("SIZE", medicoes.size() + "");
    }

    @Override
    public int getCount() {
        return medicoes.size();
    }

    @Override
    public Object getItem(int position) {
        return medicoes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return medicoes.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Medicao medicao = this.medicoes.get(position);
        TableLayout linha;
        if (convertView != null) {
            linha = (TableLayout) convertView;

        } else {
            linha = new TableLayout(parent.getContext());
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(R.layout.item_hist_medicao, linha, true);
        }
        ImageView imageData = (ImageView) linha.findViewById(R.id.image_data);
        imageData.setImageDrawable(ContextCompat.getDrawable(parent.getContext(), Calendario.imagens[medicao.getDataMedicao().get(Calendar.DAY_OF_MONTH) - 1]));
        TextView tvdata = (TextView) linha.findViewById(R.id.tv_data_med_hist);
        NumberPickView pickValor = (NumberPickView) linha.findViewById(R.id.number_pick_historico);
        tvdata.setText(DateFormat.getDateInstance(DateFormat.LONG).format(medicao.getDataMedicao().getTime()));
        pickValor.setValorHistorico(medicao.getValoratual());
        return linha;
    }

    public void remove(Medicao medicao) {
        medicoes.remove(medicao);
    }

    public void remove(int position) {
        medicoes.remove(position);
    }
}
