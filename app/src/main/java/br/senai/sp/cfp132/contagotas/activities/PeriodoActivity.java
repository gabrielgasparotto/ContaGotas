package br.senai.sp.cfp132.contagotas.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.CalendarContract;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;


import java.util.Calendar;
import java.util.GregorianCalendar;

import br.senai.sp.cfp132.contagotas.R;
import br.senai.sp.cfp132.contagotas.dao.PeriodoDAO;
import br.senai.sp.cfp132.contagotas.modelo.Periodo;
import br.senai.sp.cfp132.contagotas.view.NumberPickView;


/**
 * s
 * Created by Tecnico_Tarde on 11/11/2015.
 */
public class PeriodoActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    // Valor implantado a partir de fevereiro/2016 para multiplicar o MCR
    public static final double FATORAJUSTE2016 = 0.78;

    private Button btDataInicial, btDataFinal;
    private EditText  editMedia;
    private int anoI, diaI, mesI;
    private int anoF, diaF, mesF;
    private Calendar dataI, dataF, dataEscolhida;
    private Periodo periodo;
    private View a;
    private CheckBox checkBox;
    private int AGENDA = 42;
    long retorno = 0;
    int ultimaMedicao;

    private PeriodoDAO daoPeriodo;
    private DatePickerDialog datePicker;
    private boolean dataInicial = false;
    private NumberPickView picker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_periodo);
        picker = (NumberPickView) findViewById(R.id.number_pick);
        daoPeriodo = new PeriodoDAO(PeriodoActivity.this);
        picker.setValor(daoPeriodo.getUltimaMedicao());
        checkBox = (CheckBox) findViewById(R.id.cbnotificar);
        daoPeriodo = new PeriodoDAO(PeriodoActivity.this);
        btDataInicial = (Button) findViewById(R.id.buttonDataInicial);
        btDataFinal = (Button) findViewById(R.id.buttonDataFinal);
//        editMetaConsumo = (EditText) findViewById(R.id.editMetaConsumoPeriodoMensal);
        editMedia = (EditText) findViewById(R.id.editMediaConsumo);
        if (getIntent().getExtras() != null){
            editMedia.setText(String.valueOf((int)getIntent().getExtras().get("media")));

        }

//        editMedia.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if(!hasFocus && !editMedia.getEditableText().toString().trim().isEmpty()){
//                    int media =Integer.parseInt(editMedia.getEditableText().toString());
//                    double meta = media * 0.8;
//                    int metaInteiro =(int) Math.round(meta);
//                    editMetaConsumo.setText(metaInteiro+"");
//                }
//            }
//        });

        dataEscolhida = Calendar.getInstance();

        dataI = Calendar.getInstance();
        diaI = dataI.get(Calendar.DAY_OF_MONTH);
        anoI = dataI.get(Calendar.YEAR);
        mesI = dataI.get(Calendar.MONTH);

        btDataInicial.setText(String.format("%02d/%02d/%4d", diaI, mesI + 1, anoI));

        dataF = Calendar.getInstance();
        dataF.add(Calendar.DAY_OF_MONTH, 30);
        diaF = dataF.get(Calendar.DAY_OF_MONTH);
        anoF = dataF.get(Calendar.YEAR);
        mesF = dataF.get(Calendar.MONTH);

        btDataFinal.setText(String.format("%02d/%02d/%4d", diaF, mesF + 1, anoF));

        datePicker = new DatePickerDialog(this, this, anoI, mesI, diaI);
        agendamento();

    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        dataEscolhida.set(year, monthOfYear, dayOfMonth,0,0,0);
        if (dataInicial) {
            if (dataEscolhida.after(dataF) || dataEscolhida.after(Calendar.getInstance())) {
                Toast.makeText(this, R.string.valida_data_inicial, Toast.LENGTH_SHORT).show();
            } else {
                diaI = dayOfMonth;
                mesI = monthOfYear;
                anoI = year;
                dataI.setTime(dataEscolhida.getTime());
                dataF.setTime(dataEscolhida.getTime());
                dataF.add(Calendar.DAY_OF_MONTH, 30);
                diaF = dataF.get(Calendar.DAY_OF_MONTH);
                anoF = dataF.get(Calendar.YEAR);
                mesF = dataF.get(Calendar.MONTH);
                btDataInicial.setText(String.format("%02d/%02d/%4d", diaI, mesI + 1, anoI));
                btDataFinal.setText(String.format("%02d/%02d/%4d", diaF, mesF + 1, anoF));
            }
        } else {
            if (dataEscolhida.before(dataI)) {
               Toast.makeText(this, R.string.valida_data_final, Toast.LENGTH_SHORT).show();
            } else {
                diaF = dayOfMonth;
                mesF = monthOfYear;
                anoF = year;
                dataF.set(anoF, mesF, diaF,23,59,59);
                btDataFinal.setText(String.format("%02d/%02d/%4d", diaF, mesF + 1, anoF));
            }
        }
    }

    public void buttonDataInicialClick(View v) {
        dataInicial = true;
        datePicker.updateDate(anoI, mesI, diaI);
        datePicker.show();
    }

    public void buttonDataFinalClick(View v) {
        dataInicial = false;
        datePicker.updateDate(anoF, mesF, diaF);
        datePicker.show();
    }

    public void buttonSalvarClick(View v) {
        if (editMedia.getEditableText().toString().isEmpty()) {
            Snackbar.make(v, R.string.valida_media, Snackbar.LENGTH_SHORT).show();
            editMedia.requestFocus();
        } else if (picker.getValor() < ultimaMedicao) {
            Snackbar.make(v, R.string.valida_val_inic_periodo, Snackbar.LENGTH_SHORT).show();
        } else {
            periodo = new Periodo();
            dataI.set(anoI, mesI, diaI, 0, 0,0);
            periodo.setDataInicial(dataI);
            dataF.set(anoF, mesF, diaF, 23, 59, 59);
            periodo.setDataFinal(dataF);
            periodo.setValor_inicial(picker.getValor());
            periodo.setMedia(Integer.parseInt(editMedia.getEditableText().toString()));
            // multiplica a média pelo fator e estipula a meta em 20% deste valor
            int meta =(int) Math.round((periodo.getMedia() * FATORAJUSTE2016) * 0.8);
            periodo.setMeta(meta);

            retorno = daoPeriodo.incluir(periodo);


            if (retorno > 0) {
                periodo.setId(retorno);
                //PrincipalActivity.setPeriodoVigente(periodo);
                if (checkBox.isChecked()) {
                    startActivityForResult(calPopulation(retorno), AGENDA);
                } else {
                    startActivity(new Intent(this, PrincipalActivity.class));
                    finish();
                }

            } else {
                Snackbar.make(v, R.string.erro_inserir_periodo, Snackbar.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    protected void onStart() {
        ultimaMedicao = daoPeriodo.getUltimaMedicao();
        picker.setValor(ultimaMedicao);
        super.onStart();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AGENDA) {
            startActivity(new Intent(this, PrincipalActivity.class));
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        daoPeriodo.close();
    }

    private void agendamento() {
        SharedPreferences preferencias = PreferenceManager
                .getDefaultSharedPreferences(this);

        boolean retorno = preferencias.getBoolean("agenda", false);

        if (retorno) {
            checkBox.setChecked(true);
        }
    }

    public Intent calPopulation(long idEvent) {
        Intent calIntent = new Intent(Intent.ACTION_INSERT);
        calIntent.setType("vnd.android.cursor.item/event");
        calIntent.putExtra(CalendarContract.Events.TITLE, getString(R.string.titulo_evento));



        GregorianCalendar calDateFim = new GregorianCalendar(anoF, mesF, diaF, dataF.get(Calendar.HOUR_OF_DAY), dataF.get(Calendar.MINUTE));
        calIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, calDateFim.getTimeInMillis());
        calIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, calDateFim.getTimeInMillis());
        calIntent.putExtra(CalendarContract.Events.HAS_ALARM, true);
        calIntent.putExtra(CalendarContract.Reminders.EVENT_ID, idEvent);
        calIntent.putExtra(CalendarContract.Events.ALLOWED_REMINDERS, "METHOD_DEFAULT");
        calIntent.putExtra(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);

        calIntent.putExtra(CalendarContract.Reminders.MINUTES, 5);

        return calIntent;
    }
}
