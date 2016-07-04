package br.edu.ifspsaocarlos.sdm.trabalhofinalpa2.activities;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifspsaocarlos.sdm.trabalhofinalpa2.R;
import br.edu.ifspsaocarlos.sdm.trabalhofinalpa2.entities.Usuario;
import br.edu.ifspsaocarlos.sdm.trabalhofinalpa2.fragments.ContatoFragment;

/**
 * Criado por Lucas Petto em 02/07/2016.
 */
public class ContatoActivity extends AppCompatActivity {

    public static List<Usuario> listaContatos = new ArrayList<>();

    public static Usuario contatoLogado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.contato);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listaContatos = (ArrayList<Usuario>) getIntent().getSerializableExtra("contatos");

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

            default:
                Toast.makeText(getApplicationContext(), "Menu não mapeado", Toast.LENGTH_SHORT).show();
                break;
        }

        return true;
    }
}
