package br.senai.sp.cfp132.contagotas.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import br.senai.sp.cfp132.contagotas.R;
import br.senai.sp.cfp132.contagotas.activities.PrincipalActivity;
import br.senai.sp.cfp132.contagotas.modelo.Periodo;
import br.senai.sp.cfp132.contagotas.util.Calendario;

/**
 * Created by Tecnico_Tarde on 19/11/2015.
 */
public class HistoricoPeriodoAdapter extends BaseAdapter {
    private List<Periodo> periodos;


    public HistoricoPeriodoAdapter(List<Periodo> periodos) {
        this.periodos = periodos;
    }

    @Override
    public int getCount() {
        return periodos.size();
    }

    @Override
    public Object getItem(int position) {
        return periodos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return periodos.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Periodo periodo = periodos.get(position);
        TableLayout linha;
        if (convertView != null) {
            linha = (TableLayout) convertView;
        } else {
            linha = new TableLayout(parent.getContext());
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(R.layout.item_hist_periodo, linha, true);
        }
        ImageView imageInicio = (ImageView) linha.findViewById(R.id.image_inicio);
        imageInicio.setImageDrawable(ContextCompat.getDrawable(parent.getContext(), Calendario.imagens[periodo.getDataInicial().get(Calendar.DAY_OF_MONTH)-1]));
        ImageView imageFim = (ImageView) linha.findViewById(R.id.image_fim);;
        imageFim.setImageDrawable(ContextCompat.getDrawable(parent.getContext(), Calendario.imagens[periodo.getDataFinal().get(Calendar.DAY_OF_MONTH)-1]));
        TextView textViewInicio = (TextView) linha.findViewById(R.id.text_view_inicio);
        SimpleDateFormat formatador = new SimpleDateFormat("dd/MMM/yyyy");
        textViewInicio.setText(formatador.format(periodo.getDataInicial().getTime()));
        TextView textViewFim = (TextView) linha.findViewById(R.id.text_view_fim);
        textViewFim.setText(formatador.format(periodo.getDataFinal().getTime()));
        TextView textViewConsumo = (TextView) linha.findViewById(R.id.text_view_consumo);
        textViewConsumo.setText(periodo.getConsumo()+" "+parent.getResources().getString(R.string.metro3));
        if (periodo.equals(PrincipalActivity.getPeriodoVigente())) {
            linha.setBackgroundColor(ContextCompat.getColor(linha.getContext(), R.color.azul));
        } else if(periodo.getConsumo() <= periodo.getMeta()) {
            linha.setBackgroundColor(ContextCompat.getColor(linha.getContext(), R.color.verde));
        }else {
            linha.setBackgroundColor(ContextCompat.getColor(linha.getContext(), R.color.vermelho));
        }

        return linha;
    }

    public void remove(Periodo periodo) {
        periodos.remove(periodo);
    }

    public void remove(int position) {
        periodos.remove(position);
    }
}
