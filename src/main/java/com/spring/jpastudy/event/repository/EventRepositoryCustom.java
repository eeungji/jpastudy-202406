package com.spring.jpastudy.event.repository;

import com.spring.jpastudy.event.entity.Event;

import java.util.List;

public interface EventRepositoryCustom {

    //조회할때사용할것
    List<Event> findEvents(String sort);
}
