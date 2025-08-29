package com.blm.common.service;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


public class BaseService {

    protected  <V, E> List<V> entity2VO(List<E> entities, Class<V> voClass) {
        if (entities == null || entities.isEmpty()) {
            return List.of();
        }
        return entities.stream().map(entity -> {
            try {
                V vo = voClass.getDeclaredConstructor().newInstance();
                BeanUtils.copyProperties(entity, vo);
                return vo;
            } catch (Exception e) {
                throw new RuntimeException("Error converting entity to VO", e);
            }
        }).collect(Collectors.toList());
    }

    protected <V, E> V entity2VO(E entity, Class<V> voClass) {
        if (entity == null) {
            return null;
        }
        try {
            V vo = voClass.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(entity, vo);
            return vo;
        } catch (Exception e) {
            throw new RuntimeException("Error converting entity to VO", e);
        }
    }
}
