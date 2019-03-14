package br.senai.sp.cfp132.contagotas.activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;

import java.util.ArrayList;

import br.senai.sp.cfp132.contagotas.R;
import br.senai.sp.cfp132.contagotas.modelo.Periodo;

/*Grafico pronto para receber os dados do consumo e da meta*/
//TODO
public class GraficoActivity extends AppCompatActivity {
    private PieChart pieChart;
    private ChartData dd;
    private static Periodo p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafico);

        p = (Periodo) getIntent().getSerializableExtra("periodo");

        pieChart = (PieChart) findViewById(R.id.pieChart);

        /*definimos algunos atributos*/
        pieChart.setHoleRadius(40f);
        pieChart.setDrawYValues(true);
        pieChart.setDrawXValues(true);
        pieChart.setRotationEnabled(true);
        pieChart.animateXY(1500, 1500);

		/*creamos una lista para los valores Y*/
        ArrayList<Entry> valsY = new ArrayList<Entry>();



        float consumo = p.getConsumoInt();
        float meta = 100;
        float valorConsumo = (100*consumo)/p.getMeta();
        float valorMeta = 100;

        if ((meta - valorConsumo) < 0){
             valorMeta  = 0;
        }else{
            valorMeta = meta - valorConsumo;
        }

        valsY.add(new Entry(valorMeta, 0));
        valsY.add(new Entry(valorConsumo, 1));
        /*
        valsY.add(new Entry(5 * 100 / 25, 0));
        valsY.add(new Entry(15 * 100 / 25, 1));
        valsY.add(new Entry(5 * 100 / 25, 1));
*/
 		/*creamos una lista para los valores X*/
        ArrayList<String> valsX = new ArrayList<String>();
        valsX.add("Média");
        valsX.add("Consumo");
        //    valsX.add("Mais ou menos");

 		/*creamos una lista de colores*/
        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(getResources().getColor(R.color.verde));
        colors.add(getResources().getColor(R.color.vermelho));
        //     colors.add(getResources().getColor(R.color.material_blue_grey_800));

 		/*seteamos los valores de Y y los colores*/
        PieDataSet set1 = new PieDataSet(valsY,"");
        set1.setSliceSpace(3f);
        set1.setColors(colors);

		/*seteamos los valores de X*/
        PieData data = new PieData(valsX, set1);
        pieChart.setData(data);
        pieChart.highlightValues(null);
        pieChart.invalidate();

        /*Ocutar descripcion*/
        pieChart.setDescription("");



        /*Ocultar leyenda*/
        //     pieChart.setDrawLegend(false);
    }
}

