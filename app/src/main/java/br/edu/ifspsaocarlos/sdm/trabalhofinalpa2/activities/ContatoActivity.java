package br.edu.ifspsaocarlos.sdm.trabalhofinalpa2.activities;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.edu.ifspsaocarlos.sdm.trabalhofinalpa2.R;
import br.edu.ifspsaocarlos.sdm.trabalhofinalpa2.entities.Usuario;
import br.edu.ifspsaocarlos.sdm.trabalhofinalpa2.fragments.ContatoFragment;

/**
 * Activity responsável pelos contatos.
 *
 * @author Anderson Canale Garcia
 * @author Lucas Petto
 */
public class ContatoActivity extends AppCompatActivity {

    public static List<Usuario> contatos = new ArrayList<>();

    public static Usuario contatoLogado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.contato);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        contatos = (ArrayList<Usuario>) getIntent().getSerializableExtra("contatos");

        contatoLogado = (Usuario) getIntent().getSerializableExtra("contatoLogado");

        ContatoFragment frag = (ContatoFragment) getFragmentManager().findFragmentByTag("mainFrag");

        if (frag == null) {
            frag = new ContatoFragment();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.rl_fragment_contato, frag, "mainFrag");
            ft.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.menu_home:
                Intent homeIntent = new Intent(getApplicationContext(), HomeActivity.class);
                homeIntent.putExtra("contatos", (Serializable) contatos);
                homeIntent.putExtra("contatoLogado", contatoLogado);
                startActivity(homeIntent);
                break;

            default:
                break;
        }

        return true;
    }
}
