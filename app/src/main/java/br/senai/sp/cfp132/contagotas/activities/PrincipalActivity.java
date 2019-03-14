package br.senai.sp.cfp132.contagotas.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Calendar;

import br.senai.sp.cfp132.contagotas.R;
import br.senai.sp.cfp132.contagotas.dao.PeriodoDAO;
import br.senai.sp.cfp132.contagotas.modelo.Periodo;
import br.senai.sp.cfp132.contagotas.view.TableResultadoView;

public class PrincipalActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static Periodo periodoVigente;
    private TableResultadoView tableResultado;
    private PeriodoDAO periodoDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        periodoDao = new PeriodoDAO(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        tableResultado = (TableResultadoView) findViewById(R.id.table_resultado);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PrincipalActivity.this, MedicaoActivity.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Intent intent;
        int id = item.getItemId();

        if (id == R.id.periodos) {
            intent = new Intent(this, HistoricoPeriodoActitvity.class);
            startActivity(intent);
        } else if (id == R.id.historico_leitura) {
            intent = new Intent(this, HistoricoMedicaoActivity.class);
            intent.putExtra("periodo", periodoVigente);
            startActivity(intent);
        } else if (id == R.id.efetuar_leitura) {
            intent = new Intent(this, MedicaoActivity.class);
            startActivity(intent);
        } else if (id == R.id.dicas) {
            intent = new Intent(this, DicasActivity.class);
            startActivity(intent);
        } else if (id == R.id.configuracoes) {
            intent = new Intent(this, ConfigActivity.class);
            startActivity(intent);
        } else if (id == R.id.telefonar_cia) {
            SharedPreferences preferencias = PreferenceManager
                    .getDefaultSharedPreferences(this);
            String uri = "tel:" + preferencias.getString("telefone", "08000119911");
            intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse(uri));
            startActivity(intent);
        } else if (id == R.id.grafico) {
            intent = new Intent(this, GraficoActivity.class);
            intent.putExtra("periodo", periodoVigente);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        drawer.setSelected(false);
        return true;
    }

    @Override
    protected void onStart() {
        boolean cadastrarPeriodo = false;
        super.onStart();
        periodoVigente = periodoDao.buscarVigente();
        Calendar dataAtual = Calendar.getInstance();

        // Se não existe período vigente é preciso criar um período
        if (periodoVigente == null) {
            cadastrarPeriodo = true;
        // Se o período vigente já encerrou
        } else if (periodoVigente.getDataFinal().before(dataAtual)) {
            cadastrarPeriodo = true;
        // se está no dia da medição e já foi realizada a medição de hoje, abre-se para um novo período. A ideia é que a última medição seja realizada no dia da medição, e ao realizá-la já é aberta a tela para o novo período
        } else if ( (periodoVigente.getDataFinal().getTimeInMillis() - System.currentTimeMillis() / 1000 / 60 / 60 / 24) < 1 && periodoVigente.getMedicoes().size() > 0 && periodoVigente.getMedicoes().get(0).getDataMedicao().get(Calendar.DAY_OF_YEAR) == dataAtual.get(Calendar.DAY_OF_YEAR)) {
            cadastrarPeriodo = true;
        }
        if (cadastrarPeriodo) {
            Intent intent = new Intent(this,PeriodoActivity.class);
            // Se houver um período vigente, salva na intent para a activity de cadastro de período, a média
            if (periodoVigente != null){
               intent.putExtra("media",periodoVigente.getMedia());
            }
            startActivity(intent);
            finish();
        } else {
            tableResultado.setPeriodo(periodoVigente);
        }
    }

    public static Periodo getPeriodoVigente() {
        return periodoVigente;
    }

    public static void setPeriodoVigente(Periodo periodo) {
        periodoVigente = periodo;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        periodoDao.close();
    }
}
