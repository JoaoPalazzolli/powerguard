package br.com.inovatech.powerguard.infra.utils;

import org.modelmapper.ModelMapper;

import java.util.List;

/**
 * Classe utilitária para mapeamento de objetos usando ModelMapper.
 * Fornece métodos para converter objetos de um tipo para outro.
 */
public class Mapper {

    // Instância do ModelMapper usada para realizar o mapeamento de objetos.
    private static final ModelMapper mapper = new ModelMapper();

    /**
     * Converte um objeto de um tipo para outro tipo.
     *
     * @param source O objeto de origem a ser convertido.
     * @param destinationType A classe do tipo de destino para o qual o objeto será convertido.
     * @param <O> O tipo do objeto de origem.
     * @param <D> O tipo do objeto de destino.
     * @return O objeto convertido no tipo de destino especificado.
     */
    public static <O, D> D parseObject(O source, Class<D> destinationType) {
        return mapper.map(source, destinationType);
    }

    /**
     * Converte uma lista de objetos de um tipo para uma lista de outro tipo.
     *
     * @param source A lista de objetos de origem a serem convertidos.
     * @param destinationType A classe do tipo de destino para o qual os objetos serão convertidos.
     * @param <O> O tipo dos objetos de origem.
     * @param <D> O tipo dos objetos de destino.
     * @return Uma lista contendo os objetos convertidos no tipo de destino especificado.
     */
    public static <O, D> List<D> parseListObject(List<O> source, Class<D> destinationType) {
        return source.stream().map(x -> mapper.map(x, destinationType)).toList();
    }
}

