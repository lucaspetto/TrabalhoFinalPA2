package br.edu.ifspsaocarlos.sdm.trabalhofinalpa2.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.Serializable;
import java.util.List;

import br.edu.ifspsaocarlos.sdm.trabalhofinalpa2.R;
import br.edu.ifspsaocarlos.sdm.trabalhofinalpa2.activities.ContatoActivity;
import br.edu.ifspsaocarlos.sdm.trabalhofinalpa2.activities.ConversaActivity;
import br.edu.ifspsaocarlos.sdm.trabalhofinalpa2.activities.LoginActivity;
import br.edu.ifspsaocarlos.sdm.trabalhofinalpa2.activities.MensagemActivity;
import br.edu.ifspsaocarlos.sdm.trabalhofinalpa2.adapters.ContatoAdapter;
import br.edu.ifspsaocarlos.sdm.trabalhofinalpa2.entities.Usuario;
import br.edu.ifspsaocarlos.sdm.trabalhofinalpa2.interfaces.RecyclerViewOnItemSelecionado;

/**
 * Fragment de contato.
 */
public class ContatoFragment extends Fragment implements RecyclerViewOnItemSelecionado{

    private RecyclerView mRecyclerView;
    private List<Usuario> mList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contato, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_contatos);
        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);

        mList = ContatoActivity.listaContatos;
        ContatoAdapter adapter = new ContatoAdapter(getActivity(), mList);
        adapter.setRecyclerViewOnItemSelecionado(this);
        mRecyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onItemSelecionado(View view, int posicao) {

        Intent contatoIntent = new Intent(getActivity(), ConversaActivity.class);
        contatoIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        contatoIntent.putExtra("contatoLogado", ContatoActivity.contatoLogado);
        contatoIntent.putExtra("contatoSelecionado", ContatoActivity.listaContatos.get(posicao));
        startActivity(contatoIntent);;

        ContatoActivity.listaContatos.get(posicao);
    }

}
