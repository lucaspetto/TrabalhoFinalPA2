package br.edu.ifspsaocarlos.sdm.trabalhofinalpa2.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import br.edu.ifspsaocarlos.sdm.trabalhofinalpa2.R;
import br.edu.ifspsaocarlos.sdm.trabalhofinalpa2.entities.Usuario;
import br.edu.ifspsaocarlos.sdm.trabalhofinalpa2.util.AcessoRest;
import br.edu.ifspsaocarlos.sdm.trabalhofinalpa2.util.Util;

/**
 * Actvity responsável pelo cadastro de contato.
 *
 * @author Anderson Canale Garcia
 * @author Lucas Petto
 */
public class CadastroContatoActivity extends AppCompatActivity {

    // Objeto de progresso de diálogo.
    private ProgressDialog prgDialogo;

    // Mensagem de erro.
    private TextView mensagemErro;

    // Nome completo do contato.
    private EditText nomeCompletoET;

    // Apelido do contato.
    private EditText apelidoET;


    private final Usuario usuario = new Usuario();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastro_contato);

        // Carregando os dados do contato.
        mensagemErro = (TextView) findViewById(R.id.mensagem_erro);
        nomeCompletoET = (EditText) findViewById(R.id.cadastro_nome_completo);
        apelidoET = (EditText) findViewById(R.id.cadastro_apelido);

        prgDialogo = new ProgressDialog(this);
        prgDialogo.setMessage("Por favor, aguarde...");
        prgDialogo.setCancelable(false);
    }

    /**
     * Método chamado quando o botão cadastrar for selecionado.
     */
    public void cadastrarContato(View view) {

        String nomeCompleto = nomeCompletoET.getText().toString();
        String apelido = apelidoET.getText().toString();

        // Instanciando o parâmetro de requisição HTTP.
        RequestParams params = new RequestParams();

        if (Util.isNotNull(nomeCompleto) && Util.isNotNull(apelido)) {

            usuario.setNome(nomeCompleto);
            usuario.setApelido(apelido);

            chamarWS();
        } else {
            mensagemErro.setText("Por favor, preencha todos os campos.");
        }

    }

    /**
     * Método chamado quando o botão de login for selecionado.
     */
    public void chamarWS() {

        new Thread(){
            public void run() {
                RequestQueue queue = Volley.newRequestQueue(CadastroContatoActivity.this);
                String string = "{\"nome_completo\":\"" + usuario.getNome() + "\"," +
                        "\"apelido\":\""+ usuario.getApelido() + "\"}";

                try {
                    final JSONObject jsonBody = new JSONObject(string);
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                            Request.Method.POST,
                            AcessoRest.getAbsoluteUrl("contato"),
                            jsonBody,
                            new Response.Listener<JSONObject>() {
                                public void onResponse(JSONObject s) {
                                    Toast.makeText(CadastroContatoActivity.this, "Contato salvo com sucesso!!",
                                            Toast.LENGTH_SHORT).show();
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            limparValores();
                                        }
                                    });
                                }
                            },
                            new Response.ErrorListener() {
                                public void onErrorResponse(VolleyError volleyError) {
                                    Toast.makeText(CadastroContatoActivity.this, "Erro no cadastro do contato!",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                    queue.add(jsonObjectRequest);
                }catch (Exception e ) {
                    Log.e("SDM", "Erro no cadastro do contato. Informações técnicas: '"+e.getMessage()+"'");
                }
            }
        }.start();
    }

    /**
     * Redireciona para a tela Home.
     */
    public void redirecionarLogin(View view) {

        Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
        // Limpa o histórico da Activity.
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(loginIntent);
    }

    /**
     * Inicializa os valores da view.
     */
    public void limparValores() {

        nomeCompletoET.setText("");
        apelidoET.setText("");
        mensagemErro.setText("");
    }
}

