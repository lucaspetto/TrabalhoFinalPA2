package br.edu.ifspsaocarlos.sdm.trabalhofinalpa2.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import br.edu.ifspsaocarlos.sdm.trabalhofinalpa2.R;
import br.edu.ifspsaocarlos.sdm.trabalhofinalpa2.entities.Mensagem;

/**
 * Created by Anderson on 08/07/2016.
 */
public class MensagemBaseAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<Mensagem> mensagens;

    public MensagemBaseAdapter(Context activity, List<Mensagem> mensagens) {
        super();

        this.mensagens = mensagens;

        this.inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mensagens.size();
    }

    @Override
    public Mensagem getItem(int position) {
        return this.mensagens.get(position);
    }

    @Override
    public long getItemId(int position) {
        return this.mensagens.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.mensagem_celula, null);
            holder = new ViewHolder();
            holder.nome = (TextView) convertView.findViewById(R.id.nome);
            holder.mensagem = (TextView) convertView.findViewById(R.id.mensagem);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Mensagem mensagem = getItem(position);

        holder.nome.setText("[" + mensagem.getOrigem().getNome() + "] ");
        holder.mensagem.setText(mensagem.getCorpo());

        return convertView;
    }


    static class ViewHolder {
        public TextView nome;
        public TextView mensagem;
    }
}
