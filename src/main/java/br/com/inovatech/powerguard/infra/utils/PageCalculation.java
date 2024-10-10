package br.com.inovatech.powerguard.infra.utils;

/**
 * Classe utilitária para cálculos relacionados à paginação.
 * Fornece métodos para determinar a última página com base na contagem total de itens.
 */
public class PageCalculation {

    /**
     * Retorna o número da última página com base na contagem total de itens.
     *
     * @param count A contagem total de itens.
     * @return O número da última página, ou null se a contagem for inválida.
     *         Se a contagem for menor que zero, será retornado null.
     *         Se a contagem for um número positivo, a última página será calculada
     *         com base na suposição de que cada página contém 10 itens.
     */
    public static Integer getLastPage(Integer count){

        if(count == null || count < 0) return null;

        if(count % 10 != 0) return (count / 10) + 1;

        return count / 10;
    }
}

