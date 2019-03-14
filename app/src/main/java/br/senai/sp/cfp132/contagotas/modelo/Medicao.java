package br.senai.sp.cfp132.contagotas.modelo;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by Técnico Tarde on 03/11/2015.
 */
public class Medicao implements Serializable {

    private int id;
    private Periodo periodo;
    private int valoratual;
    private Calendar dataMedicao;

    public Calendar getDataMedicao() {

        return dataMedicao;
    }

    public void setDataMedicao(Calendar dataMedicao) {
        this.dataMedicao = dataMedicao;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Periodo getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }

    public int getValoratual() {
        return valoratual;
    }

    public void setValoratual(int valoratual) {
        this.valoratual = valoratual;
    }
}
