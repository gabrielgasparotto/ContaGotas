package br.senai.sp.cfp132.contagotas.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Técnico Tarde on 03/11/2015.
 */
public class Periodo implements Serializable {


    private long id;
    private Calendar dataInicial;
    private Calendar dataFinal;
    private int meta;
    private int media;
    private int valorInicial;
    private List<Medicao> medicoes;

    public Periodo() {
        medicoes = new ArrayList<Medicao>();
    }

    public List<Medicao> getMedicoes() {
        return medicoes;
    }

    public void setMedicoes(List<Medicao> medicoes) {
        this.medicoes = medicoes;
    }

    public int getValorInicial() {
        return valorInicial;
    }

    public void setValor_inicial(int valorInicial) {
        this.valorInicial = valorInicial;
    }

    public Calendar getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(Calendar dataFinal) {
        this.dataFinal = dataFinal;
    }


    public Calendar getDataInicial() {
        return dataInicial;
    }

    public void setDataInicial(Calendar dataInicial) {
        this.dataInicial = dataInicial;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getMeta() {
        return meta;
    }

    public void setMeta(int meta) {
        this.meta = meta;
    }



    @Override
    public boolean equals(Object o) {
        if (o == null || o.getClass() != this.getClass()) {
            return false;
        }
        Periodo p = (Periodo) o;
        if (p.getId() == this.getId()) {
            return true;
        } else {
            return false;
        }
    }

    public double getConsumo() {
        double consumo = 0;
        if (medicoes.size() != 0) {
            consumo = (medicoes.get(0).getValoratual() - valorInicial) / 100d;
        }
        return consumo;
    }

    public int getConsumoInt(){
        return (int) getConsumo();
    }

    public int getMedia() {
        return media;
    }

    public void setMedia(int media) {
        this.media = media;
    }
}
