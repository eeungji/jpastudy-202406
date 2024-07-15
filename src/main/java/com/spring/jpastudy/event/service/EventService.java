package com.spring.jpastudy.event.service;


import com.spring.jpastudy.event.dto.request.EventSaveDto;
import com.spring.jpastudy.event.entity.Event;
import com.spring.jpastudy.event.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional // 반드시 붙여야 함
public class EventService {

    private final EventRepository eventRepository;

    //전제 조회 서비스
    public List<Event> getEvents(String sort) {
        return eventRepository.findEvents(sort);
    }

    // 이벤트 등록 => 클라이언트한테 받아와야함.(EventSaveDto dto)
    public List<Event> saveEvent(EventSaveDto dto) {
        // 왜 엔터티로 바꿔야 하지?
        Event savedEvent = eventRepository.save(dto.toEntity());
        log.info("saved event: {}", savedEvent);
        return getEvents("date");
    }

}
