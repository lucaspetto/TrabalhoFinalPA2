package br.edu.ifspsaocarlos.sdm.trabalhofinalpa2.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.edu.ifspsaocarlos.sdm.trabalhofinalpa2.R;
import br.edu.ifspsaocarlos.sdm.trabalhofinalpa2.entities.Usuario;
import br.edu.ifspsaocarlos.sdm.trabalhofinalpa2.interfaces.RecyclerViewOnItemSelecionado;

/**
 * Adapter de contato.
 *
 * @author Anderson Canale Garcia
 * @author Lucas Petto
 */
public class ContatoAdapter extends RecyclerView.Adapter<ContatoAdapter.MeuViewHolder> {

    private List<Usuario> listaContatos;
    private LayoutInflater mLayoutInflater;
    private RecyclerViewOnItemSelecionado meuRecyclerViewOnItemSelecionado;

    public ContatoAdapter(Context c, List<Usuario> l) {

        listaContatos = l;
        mLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public ContatoAdapter.MeuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = mLayoutInflater.inflate(R.layout.item_contato, parent, false);
        MeuViewHolder mvh = new MeuViewHolder(v);

        return mvh;
    }

    public void adicionarItemLista(Usuario c, int position) {

        listaContatos.add(c);
        notifyItemInserted(position);

    }

    @Override
    public void onBindViewHolder(MeuViewHolder holder, int position) {

        //holder.tvId.setText(listaContatos.get(position).getId());
        holder.tvNome.setText(listaContatos.get(position).getNome());
        holder.tvApelido.setText(listaContatos.get(position).getApelido());
    }

    @Override
    public int getItemCount() {

        return listaContatos.size();
    }

    public void setRecyclerViewOnItemSelecionado(RecyclerViewOnItemSelecionado r){

        meuRecyclerViewOnItemSelecionado = r;
    }

    public class MeuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //public TextView tvId;
        public TextView tvNome;
        public TextView tvApelido;

        public MeuViewHolder(View itemView) {

            super(itemView);

            //tvId = (TextView) itemView.findViewById(R.id.tv_id_contato);
            tvNome = (TextView) itemView.findViewById(R.id.tv_nome_contato);
            tvApelido = (TextView) itemView.findViewById(R.id.tv_apelido_contato);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(meuRecyclerViewOnItemSelecionado != null){
                meuRecyclerViewOnItemSelecionado.onItemSelecionado(v, getAdapterPosition());
            }
        }
    }
}
