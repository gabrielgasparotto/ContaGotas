package br.senai.sp.cfp132.contagotas.view;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import br.senai.sp.cfp132.contagotas.R;

/**
 * Created by joserobertochilesilva on 22/11/15.
 */
public class NumberPickView extends LinearLayout {
    private ImageView image1, image2, image3, image4, image5, image6;
    List<ImageView> images;
    private LinearLayout linearLayout;
    private Context context;
    private int[] valores = new int[6];


    public NumberPickView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.context = context;
        init();
    }

    public NumberPickView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public void setValorReadOnly(int valor) {
        this.setValor(valor);
        setAlpha(0.5f);
        images.get(0).setOnTouchListener(null);
        images.get(1).setOnTouchListener(null);
        images.get(2).setOnTouchListener(null);
        images.get(3).setOnTouchListener(null);
        images.get(4).setOnTouchListener(null);
        images.get(5).setOnTouchListener(null);
    }


    private void init() {
        images = new ArrayList<ImageView>();
        inflate(context, R.layout.number_pick, this);
        linearLayout = (LinearLayout) this.findViewById(R.id.linear_layout);
        images.add((ImageView) findViewById(R.id.image1));
        images.get(0).setOnTouchListener(touchListener);
        images.add((ImageView) findViewById(R.id.image2));
        images.get(1).setOnTouchListener(touchListener);
        images.add((ImageView) findViewById(R.id.image3));
        images.get(2).setOnTouchListener(touchListener);
        images.add((ImageView) findViewById(R.id.image4));
        images.get(3).setOnTouchListener(touchListener);
        images.add((ImageView) findViewById(R.id.image5));
        images.get(4).setOnTouchListener(touchListener);
        images.add((ImageView) findViewById(R.id.image6));
        images.get(5).setOnTouchListener(touchListener);
    }


    private int[] numerosPretos = new int[]
            {
                    R.drawable.a0preto,
                    R.drawable.a1preto,
                    R.drawable.a2preto,
                    R.drawable.a3preto,
                    R.drawable.a4preto,
                    R.drawable.a5preto,
                    R.drawable.a6preto,
                    R.drawable.a7preto,
                    R.drawable.a8preto,
                    R.drawable.a9preto,
            };

    private int[] numerosPretosPequenos = new int[]
            {
                    R.drawable.a0pretop,
                    R.drawable.a1pretop,
                    R.drawable.a2pretop,
                    R.drawable.a3pretop,
                    R.drawable.a4pretop,
                    R.drawable.a5pretop,
                    R.drawable.a6pretop,
                    R.drawable.a7pretop,
                    R.drawable.a8pretop,
                    R.drawable.a9pretop,
            };

    private int[] numerosVermelhos = new int[]
            {
                    R.drawable.a0vermelho,
                    R.drawable.a1vermelho,
                    R.drawable.a2vermelho,
                    R.drawable.a3vermelho,
                    R.drawable.a4vermelho,
                    R.drawable.a5vermelho,
                    R.drawable.a6vermelho,
                    R.drawable.a7vermelho,
                    R.drawable.a8vermelho,
                    R.drawable.a9vermelho,
            };

    private int[] numerosVermelhosPequenos = new int[]
            {
                    R.drawable.a0vermelhop,
                    R.drawable.a1vermelhop,
                    R.drawable.a2vermelhop,
                    R.drawable.a3vermelhop,
                    R.drawable.a4vermelhop,
                    R.drawable.a5vermelhop,
                    R.drawable.a6vermelhop,
                    R.drawable.a7vermelhop,
                    R.drawable.a8vermelhop,
                    R.drawable.a9vermelhop,
            };


    private void trocaValor(int position, String direction) {
        switch (direction) {
            case "up":
                valores[position]++;
                if (valores[position] == 10) {
                    valores[position] = 0;
                }
                break;
            case "down":
                valores[position]--;

                if (valores[position] == -1) {
                    valores[position] = 9;
                }
                break;
        }
        if (position < 4) {
            images.get(position).setImageDrawable(ContextCompat.getDrawable(context, numerosPretos[valores[position]]));
        } else {
            images.get(position).setImageDrawable(ContextCompat.getDrawable(context, numerosVermelhos[valores[position]]));
        }
    }


    OnTouchListener touchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int indice = linearLayout.indexOfChild(v);
            int y1 = 0, y2 = 0, dy = 0;
            String direction = "erro";
            switch (event.getAction()) {
                case (MotionEvent.ACTION_DOWN):
                    y1 = (int) event.getY();
                    break;
                case (MotionEvent.ACTION_UP):
                    y2 = (int) event.getY();
                    dy = y2 - y1;
                    if (dy > 0) {
                        direction = "down";
                    } else {
                        direction = "up";
                    }
                    trocaValor(indice, direction);
            }
            return true;
        }
    };

    public int getValor() {
        String valor = "";
        for (int i = 0; i < valores.length; i++) {
            valor += valores[i];
        }
        return Integer.parseInt(valor);
    }

    public void setValor(int valor) {
        String valorStr = String.format("%06d", valor);
        for (int i = 0; i < valorStr.length(); i++) {
            valores[i] = valorStr.charAt(i) - 48;
            if (i < 4) {
                images.get(i).setImageDrawable(ContextCompat.getDrawable(context, numerosPretos[valores[i]]));
            } else {
                images.get(i).setImageDrawable(ContextCompat.getDrawable(context, numerosVermelhos[valores[i]]));
            }
        }
    }

    public void setValorHistorico(int valor){
        images.get(0).setOnTouchListener(null);
        images.get(1).setOnTouchListener(null);
        images.get(2).setOnTouchListener(null);
        images.get(3).setOnTouchListener(null);
        images.get(4).setOnTouchListener(null);
        images.get(5).setOnTouchListener(null);
        String valorStr = String.format("%06d", valor);
        for (int i = 0; i < valorStr.length(); i++) {
            valores[i] = valorStr.charAt(i) - 48;
            if (i < 4) {
                images.get(i).setImageDrawable(ContextCompat.getDrawable(context, numerosPretosPequenos[valores[i]]));
            } else {
                images.get(i).setImageDrawable(ContextCompat.getDrawable(context, numerosVermelhosPequenos[valores[i]]));
            }
        }
    }
}
