package br.com.inovatech.powerguard.infra.utils;

import org.modelmapper.ModelMapper;

import java.util.List;

public class Mapper {

    private static final ModelMapper mapper = new ModelMapper();

    public static <O, D> D parseObject(O source, Class<D> destinationType) {
        return mapper.map(source, destinationType);
    }

    public static <O, D> List<D> parseListObject(List<O> source, Class<D> destinationType) {
        return source.stream().map(x -> mapper.map(x, destinationType)).toList();
    }
}
