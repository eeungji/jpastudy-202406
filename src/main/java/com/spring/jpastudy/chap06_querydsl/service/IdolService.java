package com.spring.jpastudy.chap06_querydsl.service;


import com.spring.jpastudy.chap06_querydsl.entity.Idol;
import com.spring.jpastudy.chap06_querydsl.repository.IdolRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional // JPA, QueryDsl 쓸 때 잊지말겄!
public class IdolService {

    //제이피에이 쿼리dsl jdbc템플릿듯 온갖 것 할 수 있음.
    private final IdolRepository idolRepository;

    // 아이돌을 나이순으로 내림차 정렬해서 조회
    public List<Idol> getIdols() {
//        List<Idol> idolList = idolRepository.findAll();

//        List<Idol> idolList = idolRepository.findAllBySorted();

        List<Idol> idolList = idolRepository.foundByGroupName();


//        return idolList.stream()
//                .sorted(Comparator.comparing(Idol::getAge).reversed())
//                .collect(Collectors.toList());

        return idolList;
    }
}
