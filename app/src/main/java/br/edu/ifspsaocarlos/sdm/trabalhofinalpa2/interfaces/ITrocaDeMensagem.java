package br.edu.ifspsaocarlos.sdm.trabalhofinalpa2.interfaces;

import java.util.List;

import br.edu.ifspsaocarlos.sdm.trabalhofinalpa2.entities.Mensagem;
import br.edu.ifspsaocarlos.sdm.trabalhofinalpa2.entities.Usuario;

/**
 * Interface de troca de mensagens.
 *
 * @author Anderson Canale Garcia
 * @author Lucas Petto
 */
public interface ITrocaDeMensagem {
    List<Mensagem> getMensagens();
    void listarNovasMensages(Usuario origem, Usuario destino);
    void enviarMensagem(Usuario origem, Usuario destino, String mensagem);
}
