package br.edu.ifspsaocarlos.sdm.trabalhofinalpa2.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
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
import br.edu.ifspsaocarlos.sdm.trabalhofinalpa2.activities.ConversaActivity;
import br.edu.ifspsaocarlos.sdm.trabalhofinalpa2.entities.Mensagem;
import br.edu.ifspsaocarlos.sdm.trabalhofinalpa2.entities.Usuario;

/**
 * Created by Anderson Garcia
 */
public class NovaMensagemService  extends Service implements Runnable {
    private boolean appAberta;
    private boolean primeiraBusca;
    private HashMap<Integer, Integer> ultimaMensagemRecebida;
    private List<Mensagem> novasMensagens;
    private Usuario usuarioLogado;
    private List<Usuario> contatos;

    public void onCreate() {
        super.onCreate();
        appAberta = true;
        primeiraBusca = true;

        new Thread(this).start();
    }

    private void inicializaUltimaMensagem() {
        ultimaMensagemRecebida = new HashMap<>();
        for (int i=0; i<contatos.size(); i++){
            ultimaMensagemRecebida.put(i, 0);
        }
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        usuarioLogado = (Usuario) intent.getSerializableExtra("contatoLogado");
        contatos = (ArrayList<Usuario>) intent.getSerializableExtra("contatos");
        novasMensagens = new ArrayList<>();
        inicializaUltimaMensagem();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void run() {
        novasMensagens = new ArrayList<>();
        while (appAberta) {
            try {
                Thread.sleep(getResources().getInteger(R.integer.tempo_inatividade_servico));
                buscaNovasMensagens();
                if (!primeiraBusca && novasMensagens.size() > 0) {
                    NotificationManager nm = (NotificationManager)
                            getSystemService(NOTIFICATION_SERVICE);
                    Intent intent = new Intent(this, ConversaActivity.class);
                    intent.putExtra("notificacao_nova_mensagem",
                            getString(R.string.nova_mensagem_notify));
                    PendingIntent p = PendingIntent.getActivity(this, 0, intent, 0);
                    Notification.Builder builder = new Notification.Builder(this);
                    builder.setSmallIcon(R.drawable.ic_nova_mensagem);
                    builder.setTicker(getString(R.string.nova_mensagem_notify));
                    builder.setContentTitle(getString(R.string.nova_mensagem_notify));
                    builder.setContentText(getString(R.string.nova_mensagem_notify));
                    builder.setWhen(System.currentTimeMillis());
                    builder.setContentIntent(p);
                    builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),
                            R.drawable.ic_mensageiro));
                    Notification notification = builder.build();
                    notification.vibrate = new long[] {100, 250};
                    nm.notify(R.mipmap.ic_launcher, notification);
                }
                primeiraBusca = false;
            } catch (InterruptedException e) {
                Log.e("CHAT", "Erro ao recuperar mensagens");
            }
        }

    }

    private void buscaNovasMensagens() {
        RequestQueue queue = Volley.newRequestQueue(NovaMensagemService.this);
        try {
            for(int i=0; i<contatos.size(); i++){
                // url = /mensagem/ultimaMensagem/origem/destino
                // url = /mensagem/ultimaMensagem/contato/usuarioLogado
                String url = getString(R.string.base_url) + "/mensagem/" + ultimaMensagemRecebida.get(i) + "/" + i + "/" + usuarioLogado.getId();
                final int finalI = i;
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            public void onResponse(JSONObject s) {
                                JSONArray jsonArray;
                                try {
                                    jsonArray = s.getJSONArray("mensagens");
                                    for(int j=0; j<jsonArray.length(); j++){
                                        JSONObject jsonMensagem = jsonArray.getJSONObject(j);
                                        Mensagem mensagem = Mensagem.parseMensagemFromJSON(jsonMensagem);

                                        ultimaMensagemRecebida.put(finalI, mensagem.getId());
                                        novasMensagens.add(mensagem);
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
