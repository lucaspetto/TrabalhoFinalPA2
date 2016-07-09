package br.edu.ifspsaocarlos.sdm.trabalhofinalpa2.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Classe de utilitários.
 *
 * @author Anderson Canale Garcia
 * @author Lucas Petto
 */
public class Util {

    /**
     * Verifica se o ID é válido.
     *
     * @return true caso o email seja válido.
     */
    public static boolean isIdValido(String id) {

        Integer i = new Integer(id);

        if (i == null) {

            return false;
        }

        return true;
    }

    /**
     * Verifica se o texto não é nulo.
     *
     * @return true caso o texto não seja nulo, false caso contrário.
     */
    public static boolean isNotNull(String texto) {

        return texto != null && texto.trim().length() > 0 ? true : false;
    }

}
