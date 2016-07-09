package br.edu.ifspsaocarlos.sdm.trabalhofinalpa2.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.edu.ifspsaocarlos.sdm.trabalhofinalpa2.R;
import br.edu.ifspsaocarlos.sdm.trabalhofinalpa2.activities.ContatoActivity;
import br.edu.ifspsaocarlos.sdm.trabalhofinalpa2.activities.ConversaActivity;
import br.edu.ifspsaocarlos.sdm.trabalhofinalpa2.entities.Mensagem;
import br.edu.ifspsaocarlos.sdm.trabalhofinalpa2.entities.Usuario;

/**
 * Service de verificação de novas mensagens.
 *
 * @author Anderson Canale Garcia
 * @author Lucas Petto
 */
public class NovaMensagemService  extends Service implements Runnable {
    private boolean appAberta;
    private HashMap<Integer, Integer> ultimaMensagemRecebida;
    private Usuario contatoLogado;
    private List<Usuario> contatos;

    public void onCreate() {
        super.onCreate();
        appAberta = true;

        new Thread(this).start();
    }

    private void inicializaUltimaMensagem() {
        ultimaMensagemRecebida = new HashMap<>();
        for (int i=0; i<contatos.size(); i++){
            Usuario usuario = contatos.get(i);
            ultimaMensagemRecebida.put(usuario.getId(), 0);
        }
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            contatoLogado = (Usuario) intent.getSerializableExtra("contatoLogado");
            contatos = (ArrayList<Usuario>) intent.getSerializableExtra("contatos");

            // restringindo aos 5 primeiros elementos - utilização para testes
            /*for(int i=contatos.size()-1; i>4; i--){
                contatos.remove(i);
            }*/

            inicializaUltimaMensagem();
        }catch (Exception e){
            Log.d("CHAT", "onStartCommand: Erro de leitura dos extras");
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void run() {
        while (appAberta) {
            try {
                Thread.sleep(getResources().getInteger(R.integer.tempo_inatividade_servico));
                buscaNovasMensagens();
            } catch (InterruptedException e) {
                Log.e("CHAT", "Erro ao recuperar mensagens");
            }
        }
    }

    private void buscaNovasMensagens() {
        RequestQueue queue = Volley.newRequestQueue(NovaMensagemService.this);
        try {
            for(int i=0; i<contatos.size(); i++){
                Thread.sleep(getResources().getInteger(R.integer.tempo_entre_requisicoes));
                // url = /mensagem/ultimaMensagem/origem/destino
                // url = /mensagem/ultimaMensagem/contato/usuarioLogado
                String url = getString(R.string.base_url) + "/mensagem/" + ultimaMensagemRecebida.get(i) + "/" + contatos.get(i).getId() + "/" + contatoLogado.getId();
                Log.d("CHAT", "buscaNovasMensagens: " + url);
                final Usuario contatoSelecionado = contatos.get(i);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            public void onResponse(JSONObject s) {
                                JSONArray jsonArray;
                                try {
                                    jsonArray = s.getJSONArray("mensagens");
                                    // Se houver novas mensagens do contato, guarda id de origem
                                    if(jsonArray.length() > 0){
                                        // Busca mensagem recebida mais recente
                                        JSONObject jsonMensagem = jsonArray.getJSONObject(jsonArray.length()-1);
                                        Mensagem mensagem = Mensagem.parseMensagemFromJSON(jsonMensagem);

                                        // Se última mensagem recebida não é a mais recente, há novas mensagens
                                        if(ultimaMensagemRecebida.get(contatoSelecionado.getId()) < mensagem.getId()){
                                            // atualiza última mensagem recebida
                                            ultimaMensagemRecebida.put(contatoSelecionado.getId(), mensagem.getId());
                                            // adiciona notificação de contat com novas mensagens
                                            inserirNofiticação(contatoSelecionado);
                                        }
                                    }
                                }
                                catch (JSONException je) {
                                    Toast.makeText(NovaMensagemService.this, "Erro na conversão de objeto JSON!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(NovaMensagemService.this, "Erro na recuperação de mensagens!", Toast.LENGTH_SHORT).show();
                    }
                });
                queue.add(jsonObjectRequest);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void inserirNofiticação(Usuario contato) {
        Log.d("CHAT", "inserirNofiticação: " + contato.getNome());
        NotificationManager nm = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);
        Intent intent = new Intent(this, ConversaActivity.class);
        intent.putExtra("notificacao_nova_mensagem",
                getString(R.string.nova_mensagem_notify));
        intent.putExtra("contatoLogado", contatoLogado);
        intent.putExtra("contatoDestino", contato);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(ConversaActivity.class);
        stackBuilder.addNextIntent(intent);
        //PendingIntent p = PendingIntent.getActivity(this, 0, intent, 0);
        PendingIntent p =
                stackBuilder.getPendingIntent(contato.getId(), PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(R.drawable.ic_nova_mensagem);
        builder.setTicker(getString(R.string.nova_mensagem_notify));
        builder.setContentTitle(getString(R.string.nova_mensagem_title));
        builder.setContentText(contato.getNome());
        builder.setWhen(System.currentTimeMillis());
        builder.setContentIntent(p);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_mensageiro));
        Notification notification = builder.build();
        notification.vibrate = new long[] {100, 250};
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        nm.notify(contato.getId(), notification);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        appAberta = false;
        stopSelf();
    }
}