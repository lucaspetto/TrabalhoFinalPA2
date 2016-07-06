package br.edu.ifspsaocarlos.sdm.trabalhofinalpa2.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import br.edu.ifspsaocarlos.sdm.trabalhofinalpa2.R;
import br.edu.ifspsaocarlos.sdm.trabalhofinalpa2.entities.Mensagem;

public class MensagemArrayAdapter extends ArrayAdapter<Mensagem> {
    private LayoutInflater inflater;

    public MensagemArrayAdapter(Context activity, List<Mensagem> objects) {
        super(activity, R.layout.mensagem_celula, objects);

        this.inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

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

        holder.nome.setText("[" + mensagem.getId() + ": " + mensagem.getOrigem().getNome() + "] ");
        holder.mensagem.setText(mensagem.getCorpo());

        return convertView;
    }

    static class ViewHolder {
        public TextView nome;
        public TextView mensagem;
    }
}
