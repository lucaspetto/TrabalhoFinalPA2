package br.edu.ifspsaocarlos.sdm.trabalhofinalpa2.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.edu.ifspsaocarlos.sdm.trabalhofinalpa2.R;
import br.edu.ifspsaocarlos.sdm.trabalhofinalpa2.entities.Usuario;
import br.edu.ifspsaocarlos.sdm.trabalhofinalpa2.util.AcessoRest;
import br.edu.ifspsaocarlos.sdm.trabalhofinalpa2.util.Util;
import cz.msebera.android.httpclient.Header;


/**
 * Activity de login.
 */
public class LoginActivity extends Activity {

    //Usuários do sistema.
    public static final List<Usuario> finalContatos = new ArrayList<Usuario>();
    // Objeto de progresso de diálogo.
    ProgressDialog prgDialogo;
    // Mensagem de erro Text View.
    TextView mensagemErro;
    // ID Edit View.
    EditText idET;
    //Usuário logado no sistema.
    private Usuario usuarioLogado = new Usuario();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.login);

        // Referência ao Text View de mensagem de erro.
        mensagemErro = (TextView) findViewById(R.id.login_erro);

        // Referência ao Edit View do ID do usuário.
        idET = (EditText) findViewById(R.id.login);

        // Referência ao objeto de diálogo de progresso.
        prgDialogo = new ProgressDialog(this);

        prgDialogo.setMessage("Por favor, aguarde...");

        prgDialogo.setCancelable(false);

        // Invocar o Web Service RESTful passando o parâmetro de id.
        carregarContatosWS();
    }

    /**
     * Método chamado quando o botão de login for selecionado.
     */
    public void loginUsuario(View view) {

        String apelido = idET.getText().toString();

        // Faz uma pré validação para verificar se o ID está dentro dos padrões exigidos.
        if (Util.isNotNull(apelido)) {
            Usuario usuario = new Usuario();
            usuario.setApelido(apelido);

            if (validarNomeUsuario(usuario)) {
                Toast.makeText(getApplicationContext(), "Login feito com sucesso!.", Toast.LENGTH_LONG).show();

                redirecionarActivityHome();
            } else {
                mensagemErro.setText("Por favor, digite um apelido de usuário válido.");
                //fgToast.makeText(getApplicationContext(), "Por favor, digite um apelido de usuário válido.", Toast.LENGTH_LONG).show();
            }
        }
        // Caso algum Edit View tenha ficado em branco.
        else {
            mensagemErro.setText("Por favor, preencha o campo do apelido.");
        }
    }

    /**
     * Verifica se o usuário é válido.
     *
     * @param usuario O usuário que pretende fazer login no sistema.
     * @return true caso o usuário exista, false caso contrário.
     */
    private boolean validarNomeUsuario(Usuario usuario) {

        if (usuario == null) {
            return false;
        }

        for (Usuario u : finalContatos) {

            if (u.getApelido().equals(usuario.getApelido())) {
                usuarioLogado.setNome(u.getNome());
                usuarioLogado.setApelido(u.getApelido());
                usuarioLogado.setId(u.getId());

                return true;
            }
        }

        return false;
    }

    /**
     * Método que carrega os usuários do WebService RESTFul.
     */
    public void carregarContatosWS() {

        prgDialogo.show();

        AcessoRest.get("contato", new JsonHttpResponseHandler() {

            // Caso a resposta Http tenha sido bem sucedida '200'.
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                prgDialogo.hide();

                try {
                    JSONArray contatosJsonArray = response.getJSONArray("contatos");

                    for (int i = 0; i < contatosJsonArray.length(); i++) {

                        JSONObject jsonNoFilho = contatosJsonArray.getJSONObject(i);

                        Usuario usuario = new Usuario();

                        usuario.setNome(jsonNoFilho.optString("nome_completo"));
                        usuario.setId(new Integer(jsonNoFilho.optString("id")));
                        usuario.setApelido(jsonNoFilho.optString("apelido"));

                        finalContatos.add(usuario);
                    }

                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Ocorreu um Erro. Informações técnicas [" + e.getMessage() + "]", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }

            // Quando à resposta do servidor REST possuir alguma resposta Http diferente de '200'.
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                prgDialogo.hide();
                // Resposta Http '404'.
                if (statusCode == 404) {
                    Toast.makeText(getApplicationContext(), "O recurso solicitado não foi encontrado", Toast.LENGTH_LONG).show();
                }
                // Resposta Http '500'.
                else if (statusCode == 500) {
                    Toast.makeText(getApplicationContext(), "Erro interno do servidor.", Toast.LENGTH_LONG).show();
                }
                // Resposta Http diferente de 404 e 500.
                else {
                    Toast.makeText(getApplicationContext(), "Ocorreu um erro inesperado! [Erro mais comum: O dispositivo pode não estar conectado à internet ou o servidor pode não estar disponível]", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * Redireciona para a tela Home.
     */
    public void redirecionarActivityHome() {

/*        Bundle bundle = new Bundle();
        bundle.putParcelable("usuarioLogado", finalUsuarios);*/

        Intent homeIntent = new Intent(getApplicationContext(), HomeActivity.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        homeIntent.putExtra("usuarios", (Serializable) finalContatos);
        homeIntent.putExtra("usuarioLogado", usuarioLogado);

        startActivity(homeIntent);
    }

    /**
     * Método invocado para enviar nova senha.
     *
     * @param view
     */
    public void enviarNovaSenha(View view) {

        Toast.makeText(getApplicationContext(), "Método não implementado", Toast.LENGTH_SHORT).show();
    }

}
