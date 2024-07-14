package com.spring.jpastudy.chap06_querydsl.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spring.jpastudy.chap06_querydsl.dto.GroupAverageAgeDto;
import com.spring.jpastudy.chap06_querydsl.entity.Group;
import com.spring.jpastudy.chap06_querydsl.entity.Idol;
import com.spring.jpastudy.chap06_querydsl.entity.QGroup;
import com.spring.jpastudy.chap06_querydsl.entity.QIdol;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

//import static com.spring.jpastudy.chap06_querydsl.entity.QIdol.idol;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
@Transactional
//@Rollback(false)
class QueryDslJoinTest {

    @Autowired
    IdolRepository idolRepository;

    @Autowired
    GroupRepository groupRepository;

    //쿼리 dsl을 사용하기 위한 builder
    @Autowired
    JPAQueryFactory factory;


    @BeforeEach
    void setUp() {
        //given
        Group leSserafim = new Group("르세라핌");
        Group ive = new Group("아이브");
        Group bts = new Group("방탄소년단");
        Group newjeans = new Group("뉴진스");

        groupRepository.save(leSserafim);
        groupRepository.save(ive);
        groupRepository.save(bts);
        groupRepository.save(newjeans);

        Idol idol1 = new Idol("김채원", 24, "여", leSserafim);
        Idol idol2 = new Idol("사쿠라", 26, "여", leSserafim);
        Idol idol3 = new Idol("가을", 22, "여", ive);
        Idol idol4 = new Idol("리즈", 20, "여", ive);
        Idol idol5 = new Idol("장원영", 20, "여", ive);
        Idol idol6 = new Idol("안유진", 21, "여", ive);
        Idol idol7 = new Idol("카즈하", 21, "여", leSserafim);
        Idol idol8 = new Idol("RM", 29, "남", bts);
        Idol idol9 = new Idol("정국", 26, "남", bts);
        Idol idol10 = new Idol("해린", 18, "여", newjeans);
        Idol idol11 = new Idol("혜인", 16, "여", newjeans);
        Idol idol12 = new Idol("김종국", 48, "남", null);
        Idol idol13 = new Idol("아이유", 31, "여", null);


        idolRepository.save(idol1);
        idolRepository.save(idol2);
        idolRepository.save(idol3);
        idolRepository.save(idol4);
        idolRepository.save(idol5);
        idolRepository.save(idol6);
        idolRepository.save(idol7);
        idolRepository.save(idol8);
        idolRepository.save(idol9);
        idolRepository.save(idol10);
        idolRepository.save(idol11);
        idolRepository.save(idol12);
        idolRepository.save(idol13);

    }
    
    @Test
    @DisplayName("내부 조인 예제")
    void innerJoinTest() {
        //given
        
        //when
        List<Tuple> idolList = factory
                .select(QIdol.idol, QGroup.group)
                .from(QIdol.idol)
                // 첫번쨰 파라미터는 from절에 있는 엔터티의 연관 객체
                // 두번째 파라미터는 실제로 조인할 엔터티
                .innerJoin(QIdol.idol.group, QGroup.group)
                .fetch();

        //then
        System.out.println("\n\n\n");
        for (Tuple tuple : idolList) {
            Idol foundIdol = tuple.get(QIdol.idol);
            Group foundGroup = tuple.get(QGroup.group);
            System.out.println(foundIdol);
            System.out.println(foundGroup);
        }

        System.out.println("\n\n\n");
    }



}



