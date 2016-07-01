package br.edu.ifspsaocarlos.sdm.trabalhofinalpa2.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Classe com métodos utilitários.
 */
public class Util {
    //Email Pattern
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static Pattern pattern;

    private static Matcher matcher;

    /**
     * Validação de email.
     *
     * @return true caso o email seja válido.
     */
    public static boolean isEmailValido(String email) {

        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);

        return matcher.matches();
    }

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
