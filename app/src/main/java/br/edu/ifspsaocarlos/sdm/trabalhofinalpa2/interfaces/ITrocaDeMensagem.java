package br.edu.ifspsaocarlos.sdm.trabalhofinalpa2.interfaces;

import java.util.List;

import br.edu.ifspsaocarlos.sdm.trabalhofinalpa2.entities.Mensagem;
import br.edu.ifspsaocarlos.sdm.trabalhofinalpa2.entities.Usuario;

/**
 * Created by Anderson on 08/07/2016.
 */
public interface ITrocaDeMensagem {
    List<Mensagem> getMensagens();
    void listarNovasMensages(Usuario origem, Usuario destino);
    void enviarMensagem(Usuario origem, Usuario destino, String mensagem);
}
