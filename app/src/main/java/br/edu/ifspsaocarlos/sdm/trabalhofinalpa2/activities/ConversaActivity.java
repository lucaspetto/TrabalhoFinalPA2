package br.edu.ifspsaocarlos.sdm.trabalhofinalpa2.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.Collections;

import br.edu.ifspsaocarlos.sdm.trabalhofinalpa2.R;
import br.edu.ifspsaocarlos.sdm.trabalhofinalpa2.adapters.MensagemBaseAdapter;
import br.edu.ifspsaocarlos.sdm.trabalhofinalpa2.entities.Usuario;
import br.edu.ifspsaocarlos.sdm.trabalhofinalpa2.interfaces.ITrocaDeMensagem;
import br.edu.ifspsaocarlos.sdm.trabalhofinalpa2.util.TrocaDeMensagemServiceController;

/**
 * Activity responsável pela conversa entre os contatos.
 *
 * @author Anderson Canale Garcia
 * @author Lucas Petto
 */
public class ConversaActivity extends AppCompatActivity implements View.OnClickListener {

    private MensagemBaseAdapter adapter;
    public ListView listMensagens;
    private Button btNovaMensagem;
    private EditText novaMensagem;
    private Usuario origem;
    private Usuario destino;
    ITrocaDeMensagem trocaDeMensagem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversa);

        btNovaMensagem = (Button) findViewById(R.id.btNovaMensagem);
        novaMensagem = (EditText) findViewById(R.id.novaMensagem);
        listMensagens = (ListView) findViewById(R.id.listMensagens);
        listMensagens.setDivider(null);

        btNovaMensagem.setOnClickListener(this);

        origem = (Usuario) getIntent().getSerializableExtra("contatoLogado");
        destino = (Usuario) getIntent().getSerializableExtra("contatoDestino");

        trocaDeMensagem = new TrocaDeMensagemServiceController(this) {
            @Override
            public void atualizaListaMensagens() {
                bindListView();
            }

            @Override
            public void atualizaNovaMensagem() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                        listMensagens.setSelection(adapter.getCount() - 1);
                        novaMensagem.getText().clear();
                    }
                });
            }
        };
        trocaDeMensagem.listarNovasMensages(origem, destino);
    }

    @Override
    public void onClick(View v) {
        if(v == btNovaMensagem) {
            new Thread() {
                public void run() {
                    trocaDeMensagem.enviarMensagem(origem, destino, novaMensagem.getText().toString());
                }
            }.start();
        }
    }

    private void bindListView() {
        Collections.sort(trocaDeMensagem.getMensagens());
        adapter = new MensagemBaseAdapter(getApplicationContext(), trocaDeMensagem.getMensagens());
        listMensagens.setAdapter(adapter);
    }
}