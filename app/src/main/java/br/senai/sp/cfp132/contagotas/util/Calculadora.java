package br.senai.sp.cfp132.contagotas.util;

import android.util.Log;

import java.sql.ResultSet;
import java.util.Calendar;

import br.senai.sp.cfp132.contagotas.R;
import br.senai.sp.cfp132.contagotas.modelo.Periodo;

/**
 * Created by Tecnico_Tarde on 29/10/2015.
 */
public class Calculadora {

    // FAIXAS DE CONSUMO
    private static final int FAIXA1_MAX = 10;
    private static final int FAIXA2_MAX = 20;
    private static final int FAIXA3_MAX = 30;
    private static final int FAIXA4_MAX = 50;

    // Tarifas, referência 06/2015
    public static final double[][] TARIFAS;

    static {
        TARIFAS = new double[5][5];
        // Residencial / Social
        TARIFAS[0][0] = 7;
        TARIFAS[1][0] = 1.21;
        TARIFAS[2][0] = 4.28;
        TARIFAS[3][0] = 6.1;
        TARIFAS[4][0] = 6.74;
        // Residencial / Favelas
        TARIFAS[0][1] = 5.34;
        TARIFAS[1][1] = 0.61;
        TARIFAS[2][1] = 2.02;
        TARIFAS[3][1] = 6.1;
        TARIFAS[4][1] = 6.74;
        // Residencial / Normal
        TARIFAS[0][2] = 20.64;
        TARIFAS[1][2] = 3.23;
        TARIFAS[2][2] = 8.07;
        TARIFAS[3][2] = 8.07;
        TARIFAS[4][2] = 8.89;
        // Comercial
        TARIFAS[0][3] = 41.45;
        TARIFAS[1][3] = 8.07;
        TARIFAS[2][3] = 15.45;
        TARIFAS[3][3] = 15.45;
        TARIFAS[4][3] = 16.1;
        // Industrial
        TARIFAS[0][4] = 41.45;
        TARIFAS[1][4] = 8.07;
        TARIFAS[2][4] = 15.45;
        TARIFAS[3][4] = 15.45;
        TARIFAS[4][4] = 16.1;
    }

//
//    private static final double VALOR_FX1 = 20.64;
//    private static final double VALOR_FX2 = 3.23;
//    private static final double VALOR_FX3 = 8.07;
//    private static final double VALOR_FX4 = 8.07;
//    private static final double VALOR_FX5 = 8.89;


    /**
     * Recebe o valor gasto em m3 de água e retorna o valor estimado da conta de água
     */
    public static double[][] calcularValorDaConta(double consumo, int meta, int media, boolean esgoto, int classe) {
        double[][] matriz = new double[9][2];
        double tarifa;
        double resultado = 0;
        Log.w("CONSUMO",consumo+"");
        int m3 = (int) consumo;
        resultado = TARIFAS[0][classe];
        matriz[0][1] = TARIFAS[0][classe];
        m3 = m3 - FAIXA1_MAX;

        if (m3 > 0) {
            matriz[0][0] = FAIXA1_MAX;

            if (m3 <= FAIXA2_MAX - FAIXA1_MAX) {
                tarifa = m3 * TARIFAS[1][classe];
                matriz[1][0] = m3;
                matriz[1][1] = tarifa;
                resultado += tarifa;
            } else {
                tarifa = TARIFAS[1][classe] * (FAIXA2_MAX - FAIXA1_MAX);
                matriz[1][0] = FAIXA2_MAX - FAIXA1_MAX;
                matriz[1][1] = tarifa;
                resultado += tarifa;
                m3 -= FAIXA2_MAX - FAIXA1_MAX;
                if (m3 <= FAIXA3_MAX - FAIXA2_MAX) {
                    tarifa = m3 * TARIFAS[2][classe];
                    matriz[2][0] = m3;
                    matriz[2][1] = tarifa;
                    resultado += tarifa;
                } else {
                    tarifa = TARIFAS[2][classe] * (FAIXA3_MAX - FAIXA2_MAX);
                    matriz[2][0] = FAIXA3_MAX - FAIXA2_MAX;
                    matriz[2][1] = tarifa;
                    resultado += tarifa;
                    m3 -= FAIXA3_MAX - FAIXA2_MAX;
                    if (m3 <= FAIXA4_MAX - FAIXA3_MAX) {
                        tarifa = m3 * TARIFAS[3][classe];
                        matriz[3][0] = m3;
                        matriz[3][1] = tarifa;
                        resultado += tarifa;
                    } else {
                        tarifa = TARIFAS[3][classe] * (FAIXA4_MAX - FAIXA3_MAX);
                        matriz[3][0] = FAIXA4_MAX - FAIXA3_MAX;
                        matriz[3][1] = tarifa;
                        resultado += tarifa;
                        m3 -= FAIXA4_MAX - FAIXA3_MAX;
                        tarifa = TARIFAS[4][classe] * m3;
                        matriz[4][0] = m3;
                        matriz[4][1] = tarifa;
                        resultado += tarifa;
                    }
                }
            }
        } else if (m3 < 0) {
            m3 = m3 * (-1);
            matriz[0][0] = FAIXA1_MAX - m3;

        } else {
            matriz[0][0] = FAIXA1_MAX;
        }
        Log.w("RESULTADO",resultado+"");
        matriz[5][1] = resultado;
        if (esgoto) {
            matriz[6][1] = resultado;
        }
        double descAcrescimo = 1 - descAcresc(resultado, meta, media, (int) consumo);
        double valorAgua = resultado;
        resultado*=2;
        double bonusMulta;
        if(descAcrescimo < 0){
            bonusMulta = valorAgua * descAcrescimo;
        }else{
            bonusMulta = resultado * descAcrescimo;
        }
        matriz[7][1] = bonusMulta * (-1);
        resultado = resultado - bonusMulta;
        matriz[8][1] = resultado;
        return matriz;

    }

    public static double getIdealConta(int meta, int media, boolean esgoto, int classe) {
        return calcularValorDaConta(media, meta, media, esgoto, classe)[8][1];
    }


    public static double descAcresc(double valor, int meta, int media, int consumo) {
        int mediaFator = (int) Math.round(media * 0.78);

        // conta para deteminar 20% acima da média
        int media20 = (int) (Math.round(media * 1.20));
        // conta para determinar 10% abaixo da média multiplicada pelo fator
        int mediaMenos10 = (int) (Math.round(mediaFator * 0.9));
        // conta para determinar 15% abaixo da média multilplicada pelo fator
        int mediaMenos15 = (int) (Math.round(mediaFator * 0.85));

        // se o consumo é maior que a média, haverá tarifa de contingência
        if (consumo > media) {
            // pessoas que consomem menos de 10% não pagam multa
            if (consumo <= 10) {
                return 1;
                // se consumo for maior que a média em até 20%, multa de 40%
            } else if (consumo < media20) {
                return 1.4;
                // se consumo for maior que a média mais que 20%, multa de 100%
            } else {
                return 2;
            }
        } else if (consumo < media) {
            // se consumo for menor ou igual a meta, bônus de 30%
            if (consumo <= meta) {
                return 0.7;
                // se consumo for de 20% a 15% menor que a média, bônus de 20%
            } else if (consumo <= mediaMenos15) {
                return 0.8;
                // se consumo for até de 15% a 10% menor que a média, bônus de 10%
            } else if (consumo <= mediaMenos10) {
                return 0.9;
            } else {
                return 1;
            }
        } else {
            return 1;
        }
    }

    public static double[] calculaMedias(Periodo periodo) {
        double retorno[] = new double[2];
        Calendar dataFinal = Calendar.getInstance();
        int diasPeriodo = (int) ((periodo.getDataFinal().getTimeInMillis() - periodo.getDataInicial().getTimeInMillis()) / (1000 * 60 * 60 * 24));
        retorno[0] = (periodo.getMedia() * 1.0) / diasPeriodo;
        if (periodo.getConsumo() != 0) {
            int diasAteMomento = (int) ((System.currentTimeMillis() - periodo.getDataInicial().getTimeInMillis()) / (1000 * 60 * 60 * 24));
            Log.w("DIAS",diasAteMomento+"");
            if (diasAteMomento == 0)
                diasAteMomento++;
            retorno[1] = periodo.getConsumo() / diasAteMomento;
        } else {
            retorno[1] = 0;
        }
        return retorno;
    }


}
