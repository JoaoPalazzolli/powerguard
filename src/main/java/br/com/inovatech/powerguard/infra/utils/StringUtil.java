package br.com.inovatech.powerguard.infra.utils;

/**
 * Classe utilitária para manipulação de strings.
 * Fornece métodos estáticos para realizar operações comuns em strings.
 */
public class StringUtil {

    /**
     * Retorna o caractere em uma posição específica de uma string.
     *
     * @param text A string da qual o caractere será extraído.
     * @param index O índice do caractere que será retornado.
     * @return O caractere na posição especificada da string.
     * @throws StringIndexOutOfBoundsException Se o índice estiver fora do intervalo válido da string.
     */
    public static Character getAnyCharInString(String text, int index){
        return text.charAt(index);
    }
}

