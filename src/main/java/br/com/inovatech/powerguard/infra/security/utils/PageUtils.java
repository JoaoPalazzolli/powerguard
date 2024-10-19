package br.com.inovatech.powerguard.infra.security.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class PageUtils {

    public static Pageable pageable(int page, int size, String direction, String orderBy){
        var sort = direction.equalsIgnoreCase("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;

        return PageRequest.of(page, size, Sort.by(sort, orderBy));
    }
}
