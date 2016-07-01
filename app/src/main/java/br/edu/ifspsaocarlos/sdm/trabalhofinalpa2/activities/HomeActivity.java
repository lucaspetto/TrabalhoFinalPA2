package br.edu.ifspsaocarlos.sdm.trabalhofinalpa2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifspsaocarlos.sdm.trabalhofinalpa2.R;
import br.edu.ifspsaocarlos.sdm.trabalhofinalpa2.entities.Usuario;

/**
 * Activity Home.
 */
public class HomeActivity extends AppCompatActivity {

    private List<Usuario> contatos = new ArrayList<>();

    private Usuario usuarioLogado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        contatos = (ArrayList<Usuario>) getIntent().getSerializableExtra("usuarios");

        usuarioLogado = (Usuario) getIntent().getSerializableExtra("usuarioLogado");
        TextView tv = (TextView) findViewById(R.id.mensagem_home);
        tv.setText(usuarioLogado.getNome());
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
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
                break;

            case R.id.menu_contato:
                Toast.makeText(HomeActivity.this, "Número de usuários cadastrados: " + contatos.size(), Toast.LENGTH_SHORT).show();
                break;

            default:
                Toast.makeText(getApplicationContext(), "Menu não mapeado", Toast.LENGTH_SHORT).show();
                break;
        }

        return true;
    }
}
