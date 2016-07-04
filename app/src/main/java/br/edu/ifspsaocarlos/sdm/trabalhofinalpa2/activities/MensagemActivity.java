package br.edu.ifspsaocarlos.sdm.trabalhofinalpa2.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import br.edu.ifspsaocarlos.sdm.trabalhofinalpa2.R;
import br.edu.ifspsaocarlos.sdm.trabalhofinalpa2.entities.Usuario;

/**
 * Activity responsável pela troca de mensagens entre os contatos.
 */
public class MensagemActivity extends AppCompatActivity {

    private Usuario contatoSelecionado;

    private Usuario contatoLogado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.mensagem);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        contatoLogado = (Usuario) getIntent().getSerializableExtra("contatoLogado");

        contatoSelecionado = (Usuario) getIntent().getSerializableExtra("contatoSelecionado");

        Toast.makeText(getApplicationContext(), "Contato Logado: " + contatoLogado.getNome(), Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplicationContext(), "Contato Selecionado: " + contatoSelecionado.getNome(), Toast.LENGTH_SHORT).show();
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
