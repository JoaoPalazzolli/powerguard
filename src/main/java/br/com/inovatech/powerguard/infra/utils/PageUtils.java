package br.com.inovatech.powerguard.infra.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * Classe utilitária para manipulação de paginação de dados.
 * Esta classe fornece métodos para criar objetos Pageable
 * e calcular a última página com base na contagem total de itens.
 */
public class PageUtils {

    /**
     * Cria um objeto Pageable com base nos parâmetros fornecidos.
     *
     * @param page      O número da página a ser solicitada (0-indexed).
     * @param size      O número de itens por página.
     * @param direction A direção da ordenação (ASC ou DESC).
     * @param orderBy   O campo pelo qual os dados devem ser ordenados.
     * @return Um objeto Pageable configurado com os parâmetros fornecidos.
     */
    public static Pageable pageable(int page, int size, String direction, String orderBy) {
        var sort = direction.equalsIgnoreCase("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;

        return PageRequest.of(page, size, Sort.by(sort, orderBy));
    }

    /**
     * Retorna o número da última página com base na contagem total de itens.
     *
     * @param count A contagem total de itens.
     * @return O número da última página, ou null se a contagem for inválida.
     * Se a contagem for menor que zero, será retornado null.
     * Se a contagem for um número positivo, a última página será calculada
     * com base na suposição de que cada página contém 10 itens.
     */
    public static Integer getLastPage(Integer count) {
        if (count == null || count < 0) return null;

        if (count % 10 != 0) return (count / 10) + 1;

        return count / 10;
    }
}
