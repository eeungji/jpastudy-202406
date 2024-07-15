package com.spring.jpastudy.event.controller;


import com.spring.jpastudy.event.dto.request.EventSaveDto;
import com.spring.jpastudy.event.entity.Event;
import com.spring.jpastudy.event.service.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//이제부터 리액트랑 연동되는 api는 비동기컨트롤러
@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin
public class EventController {

    @Autowired
    private EventService eventService;

    // 전체 조회 요청
    @GetMapping
    public ResponseEntity<?> getList(String sort) {
        List<Event> events = eventService.getEvents(sort);
        return ResponseEntity.ok().body(events);
    }

    // 등록 요청
    @PostMapping
    public ResponseEntity<?> register(@RequestBody EventSaveDto dto) {
        List<Event> events = eventService.saveEvent(dto);
        return ResponseEntity.ok().body(events);
    }
}
