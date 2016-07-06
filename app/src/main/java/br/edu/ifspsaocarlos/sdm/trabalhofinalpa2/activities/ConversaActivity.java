package br.edu.ifspsaocarlos.sdm.trabalhofinalpa2.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.edu.ifspsaocarlos.sdm.trabalhofinalpa2.R;
import br.edu.ifspsaocarlos.sdm.trabalhofinalpa2.adapters.MensagemArrayAdapter;
import br.edu.ifspsaocarlos.sdm.trabalhofinalpa2.entities.Mensagem;
import br.edu.ifspsaocarlos.sdm.trabalhofinalpa2.entities.Usuario;

public class ConversaActivity extends AppCompatActivity implements View.OnClickListener {

    List<Mensagem> mensagens;
    public MensagemArrayAdapter adapter;
    private ListView listMensagens;
    private Button btNovaMensagem;
    private EditText novaMensagem;
    private RequestQueue queue;
    private Usuario origem;
    private Usuario destino;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversa);

        queue = Volley.newRequestQueue(this);
        btNovaMensagem = (Button) findViewById(R.id.btNovaMensagem);
        novaMensagem = (EditText) findViewById(R.id.novaMensagem);
        listMensagens = (ListView) findViewById(R.id.listMensagens);
        listMensagens.setDivider(null);

        btNovaMensagem.setOnClickListener(this);

        origem = (Usuario) getIntent().getSerializableExtra("contatoLogado");
        destino = (Usuario) getIntent().getSerializableExtra("contatoSelecionado");

        listarMensagens();
    }

    private void listarMensagens() {
        final String url = getString(R.string.base_url) + "/mensagem/1/" + origem.getId() + "/" + destino.getId();
        final String urlDestino = getString(R.string.base_url) + "/mensagem/1/" + destino.getId() + "/" + origem.getId();
        mensagens = new ArrayList<>();

        try {
            JsonObjectRequest mensagensOrigemRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    url,
                    null,
                    new Response.Listener<JSONObject>() {
                        public void onResponse(JSONObject s) {
                            JSONArray mensagensJsonArray = null;
                            try {
                                mensagensJsonArray = s.getJSONArray("mensagens");
                                for(int i=0; i< mensagensJsonArray.length(); i++){
                                    JSONObject jsonMensagem = mensagensJsonArray.getJSONObject(i);
                                    Mensagem mensagem = parseMensagemFromJSON(jsonMensagem);

                                    mensagens.add(mensagem);
                                }
                                JsonObjectRequest mensagensDestinoRequest = new JsonObjectRequest(
                                        Request.Method.GET,
                                        urlDestino,
                                        null,
                                        new Response.Listener<JSONObject>() {
                                            public void onResponse(JSONObject s) {
                                                JSONArray mensagensJsonArray = null;
                                                try {
                                                    mensagensJsonArray = s.getJSONArray("mensagens");
                                                    for(int i=0; i< mensagensJsonArray.length(); i++){
                                                        JSONObject jsonMensagem = mensagensJsonArray.getJSONObject(i);
                                                        Mensagem mensagem = parseMensagemFromJSON(jsonMensagem);
                                                        mensagens.add(mensagem);
                                                    }

                                                    bindListView();
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        },
                                        new Response.ErrorListener() {
                                            public void onErrorResponse(VolleyError volleyError) {
                                                Log.d("CHAT", "Erro ao buscar mensagens: " + volleyError);
                                            }
                                        });
                                queue.add(mensagensDestinoRequest);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        public void onErrorResponse(VolleyError volleyError) {
                            Log.d("CHAT", "Erro ao buscar mensagens: " + volleyError);
                        }
                    });
            queue.add(mensagensOrigemRequest);
        }catch (Exception e ) {
            Log.e("CHAT", String.valueOf(R.string.mensagem_enviada_erro));
        }
    }

    @NonNull
    private Mensagem parseMensagemFromJSON(JSONObject jsonMensagem) throws JSONException {
        JSONObject jsonOrigem = (JSONObject) jsonMensagem.get("origem");

        Gson gson = new Gson();
        Mensagem mensagem = gson.fromJson(jsonMensagem.toString(), Mensagem.class);
        mensagem.getOrigem().setNome(jsonOrigem.get("nome_completo").toString());
        return mensagem;
    }

    @Override
    public void onClick(View v) {
        if(v == btNovaMensagem){
            final Mensagem mensagem = new Mensagem();
            mensagem.setOrigemId(origem.getId());
            mensagem.setDestinoId(destino.getId());
            mensagem.setAssunto("Teste ACG");
            mensagem.setCorpo(novaMensagem.getText().toString());

            String url = getString(R.string.base_url) + "/mensagem";

            try {
                Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
                final JSONObject jsonBody = new JSONObject(gson.toJson(mensagem));
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                        Request.Method.POST,
                        url,
                        jsonBody,
                        new Response.Listener<JSONObject>() {
                            public void onResponse(JSONObject s) {
                                try {
                                    Mensagem mensagem = parseMensagemFromJSON(jsonBody);
                                    mensagens.add(mensagem);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            bindListView();
                                        }
                                    });
                                    Toast.makeText(ConversaActivity.this, R.string.mensagem_enviada_sucesso,
                                        Toast.LENGTH_SHORT).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            public void onErrorResponse(VolleyError volleyError) {
                                Toast.makeText(ConversaActivity.this, R.string.mensagem_enviada_erro,
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                queue.add(jsonObjectRequest);
            }catch (Exception e ) {
                Log.e("CHAT", String.valueOf(R.string.mensagem_enviada_erro));
            }

        }
    }

    private void bindListView() {
        Collections.sort(mensagens);
        adapter = new MensagemArrayAdapter(getApplicationContext(), mensagens);
        listMensagens.setAdapter(adapter);
    }
}