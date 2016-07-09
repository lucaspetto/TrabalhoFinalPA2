package br.edu.ifspsaocarlos.sdm.trabalhofinalpa2.util;

import android.content.Context;
import android.util.Log;
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
import java.util.List;

import br.edu.ifspsaocarlos.sdm.trabalhofinalpa2.R;
import br.edu.ifspsaocarlos.sdm.trabalhofinalpa2.entities.Mensagem;
import br.edu.ifspsaocarlos.sdm.trabalhofinalpa2.entities.Usuario;
import br.edu.ifspsaocarlos.sdm.trabalhofinalpa2.interfaces.ITrocaDeMensagem;

/**
 * Controller de envio e recebimento de mensagens.
 *
 * @author Anderson Canale Garcia
 * @author Lucas Petto
 */
public abstract class TrocaDeMensagemServiceController implements ITrocaDeMensagem {
    private final Context context;
    private RequestQueue queue;
    final List<Mensagem> mensagens = new ArrayList<>();

    public TrocaDeMensagemServiceController(Context context){
        this.context = context;
        this.queue = Volley.newRequestQueue(context);
    }

    @Override
    public List<Mensagem> getMensagens(){
        return this.mensagens;
    }

    @Override
    public void listarNovasMensages(Usuario origem, Usuario destino) {
        final String url = context.getString(R.string.base_url) + "mensagem/1/" + origem.getId() + "/" + destino.getId();
        final String urlDestino = context.getString(R.string.base_url) + "mensagem/1/" + destino.getId() + "/" + origem.getId();

        try {
            // Busca mensagens da origem pro destino
            JsonObjectRequest mensagensOrigemRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    url,
                    null,
                    new Response.Listener<JSONObject>() {
                        public void onResponse(JSONObject jsonMensagensOrigem) {
                            JSONArray mensagensJsonArray = null;
                            try {
                                converteMensagens(jsonMensagensOrigem);

                                // Busca mensagens do destino para a origem
                                JsonObjectRequest mensagensDestinoRequest = new JsonObjectRequest(
                                        Request.Method.GET,
                                        urlDestino,
                                        null,
                                        new Response.Listener<JSONObject>() {
                                            public void onResponse(JSONObject jsonMensagensDestino) {
                                                try {
                                                    converteMensagens(jsonMensagensDestino);

                                                    atualizaListaMensagens();
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

    @Override
    public void enviarMensagem(final Usuario origem, final Usuario destino, String corpoMensagem) {
        // cria nova mensagem
        final Mensagem mensagem = new Mensagem(origem, destino, "", corpoMensagem);

        // url para inserir mensagem
        String url = context.getString(R.string.base_url) + "mensagem";

        try {
            // converte mensagem para JSON
            Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
            final JSONObject jsonNovaMensagem = new JSONObject(gson.toJson(mensagem));

            // faz a requisição de post da nova mensagem
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    url,
                    jsonNovaMensagem,
                    new Response.Listener<JSONObject>() {
                        public void onResponse(JSONObject jsonMensagemCriada) {
                            // converte json da mensagem criada para o bean
                            final Mensagem mensagem = new Gson().fromJson(jsonMensagemCriada.toString(), Mensagem.class);
                            mensagem.setOrigem(origem); // corrige origem
                            mensagem.setDestino(destino);  // corrige destino
                            mensagens.add(mensagem);  // adiciona nova mensagem na lista

                            atualizaNovaMensagem();
                            atualizaListaMensagens();

                            Toast.makeText(context, R.string.mensagem_enviada_sucesso,
                                    Toast.LENGTH_SHORT).show();
                        }
                    },
                    new Response.ErrorListener() {
                        public void onErrorResponse(VolleyError volleyError) {
                            Toast.makeText(context, R.string.mensagem_enviada_erro,
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
            queue.add(jsonObjectRequest);
        } catch (Exception e) {
            Log.e("CHAT", String.valueOf(R.string.mensagem_enviada_erro));
        }
    }

    // Atualiza a lista de mensagens na view
    public abstract void atualizaListaMensagens();

    // Atualiza a lista de mensagens na view com a nova mensagem
    public abstract void atualizaNovaMensagem();

    // Converte as mensagens do array JSON e adiciona na lista de mensagens
    private void converteMensagens(JSONObject s) throws JSONException {
        JSONArray mensagensJsonArray;
        mensagensJsonArray = s.getJSONArray("mensagens");
        for(int i=0; i< mensagensJsonArray.length(); i++){
            JSONObject jsonMensagem = mensagensJsonArray.getJSONObject(i);
            Mensagem mensagem = Mensagem.parseMensagemFromJSON(jsonMensagem);

            mensagens.add(mensagem);
        }
    }
}