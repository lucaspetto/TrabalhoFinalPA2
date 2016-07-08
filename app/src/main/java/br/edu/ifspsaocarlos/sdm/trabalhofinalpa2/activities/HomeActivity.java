package br.edu.ifspsaocarlos.sdm.trabalhofinalpa2.activities;

import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.edu.ifspsaocarlos.sdm.trabalhofinalpa2.R;
import br.edu.ifspsaocarlos.sdm.trabalhofinalpa2.entities.Usuario;

/**
 * Activity Home.
 */
public class HomeActivity extends AppCompatActivity {

    private List<Usuario> contatos = new ArrayList<>();
    private Intent serviceIntent;

    private Usuario contatoLogado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        contatos = (ArrayList<Usuario>) getIntent().getSerializableExtra("usuarios");

        contatoLogado = (Usuario) getIntent().getSerializableExtra("usuarioLogado");

        TextView tv = (TextView) findViewById(R.id.mensagem_home);
        tv.setText(contatoLogado.getNome());

        // Inicia serviço de novas mensages
        serviceIntent = new Intent("NOVA_MENSAGEM_SERVICE");
        serviceIntent.putExtra("contatos", (Serializable) contatos);
        serviceIntent.putExtra("contatoLogado", contatoLogado);
        startService(serviceIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.menu_contato:
                Intent contatoIntent = new Intent(getApplicationContext(), ContatoActivity.class);
                contatoIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                contatoIntent.putExtra("contatos", (Serializable) contatos);
                contatoIntent.putExtra("contatoLogado", contatoLogado);
                startActivity(contatoIntent);
                break;

            default:
                Toast.makeText(getApplicationContext(), "Menu não mapeado", Toast.LENGTH_SHORT).show();
                break;
        }

        return true;
    }

    protected void onDestroy() {
        stopService(serviceIntent);
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.cancelAll();
        super.onDestroy();
    }
}
