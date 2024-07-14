package com.spring.jpastudy.chap06_querydsl.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spring.jpastudy.chap06_querydsl.dto.GroupAverageAgeDto;
import com.spring.jpastudy.chap06_querydsl.entity.Group;
import com.spring.jpastudy.chap06_querydsl.entity.Idol;
import com.spring.jpastudy.chap06_querydsl.entity.QIdol;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.spring.jpastudy.chap06_querydsl.entity.QIdol.idol;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
//@Rollback(false)
class QueryDslGroupingTest {

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

    }

    //엔터티의 수정이 일어나면 재빌드
    @Test
    @DisplayName("성별별, 그룹별로 아이돌의 숫자가 3명 이하인 그룹만 조회")
    void groupByGenderTest() {
        //given

        //when

        /*
            SELECT G.*, I.gender, COUNT(I.idol_id)
            FROM tbl_idol I
            JOIN tbl_group G
            ON I.group_id = G.group_id
            GROUP BY G.group_id, I.gender
         */

        List<Tuple> idolList = factory
                .select(idol.group, idol.gender, idol.count()) //그룹바이에 있는 애들 바로 붙일 수 있음, 성별이랑, 성별별 숫자
                .from(idol)
                .groupBy(idol.gender, idol.group) //성별로 그룹화, id가 포함되었다고 생각해도 무방
                .having(idol.count().loe(3)) // 숫자가 3명 이하
                .fetch();

        //남자그룹하나 여자그룹하나 예상

        //then
        System.out.println("\n\n\n");
        for (Tuple tuple : idolList) {

            Group group = tuple.get(idol.group);
            String gender = tuple.get(idol.gender);
            Long count = tuple.get(idol.count());

            System.out.println(
                    String.format("\n그룹명 : %s, 성별: %s, 인원수: %d\n",
                            group.getGroupName(), gender, count)
            );
        }
        System.out.println("\n\n\n");
    }

    @Test
    @DisplayName("연령대별로 그룹화하여 아이돌 수를 조회")
    void ageGroupTest() {

        /*
            SELECT

            FROM tbl_idol
            GROUP BY
                CASE age WHEN BETWEEN 10 AND 19 THEN 10
                CASE age WHEN BETWEEN 20 AND 29 THEN 20
                CASE age WHEN BETWEEN 30 AND 39 THEN 30
                END,
                COUNT(idol_id)
            FROM tbl_idol
            GROUP BY
                CASE age WHEN BETWEEN 10 AND 19 THEN 10
                CASE age WHEN BETWEEN 20 AND 29 THEN 20
                CASE age WHEN BETWEEN 30 AND 39 THEN 30
                END
         */
        //given

        // QueryDSL로 CASE WHEN THEN 표현식 만들기
        NumberExpression<Integer> ageGroupExpression = new CaseBuilder()
                .when(idol.age.between(10, 19)).then(10)
                .when(idol.age.between(20, 29)).then(20)
                .when(idol.age.between(30, 39)).then(30)
                .otherwise(0);

        //when
        List<Tuple> result = factory
                .select(ageGroupExpression, idol.count())
                .from(idol)
                .groupBy(ageGroupExpression)
                .having(idol.count().gt(5)) //그룹바이에 대한 조건 5보다 큰것
                .fetch();

        //then
        assertFalse(result.isEmpty());
        for (Tuple tuple : result) {
            int ageGroupValue = tuple.get(ageGroupExpression);
            long count = tuple.get(idol.count());

            System.out.println();

            System.out.println("\n\nAge Group: "
                    + ageGroupValue + "대, Count: " + count);
        }
    }
    
    
    @Test
    @DisplayName("그룹별 평균 나이 조회")
    void groupAverageAgeTest() {

        /*
        age는 아이돌쪽에 있고 groupName은 그룹에 있음.
            SELECT G.group_name, AVG(I.age)
            FROM tbl_idol I
            JOIN tbl_group G
            ON I.group_id = G.group_id
            GROUP BY G.group_id //그룹별이니까 그룹의 pk로
            HAVING AVG(I.age) BETWEEN 20 AND 25
         */

        List<Tuple> result = factory
                .select(idol.group.groupName, idol.age.avg())
                .from(idol) //Idol 객체 안애 group가 있음 조인이 나감
                .groupBy(idol.group) //그룹을 참조하는 순간 join이 나감
                .having(idol.age.avg().between(20, 25))
                .fetch();

        //then
        assertFalse(result.isEmpty());
        for (Tuple tuple : result) {
            String groupName = tuple.get(idol.group.groupName);
            Double averageAge = tuple.get(idol.age.avg());

            System.out.println("\n\nGroup: " + groupName +
                    ", Average Age: " + averageAge);
        }
    }

        @Test
        @DisplayName("그룹별 평균 나이 조회(결과 DTO 처리")
        void groupAverageAgeDtoTest() {

        /*
        age는 아이돌쪽에 있고 groupName은 그룹에 있음.
        //그룹명과 평균나이를 담고 싶음.
            SELECT G.group_name, AVG(I.age)
            FROM tbl_idol I
            JOIN tbl_group G
            ON I.group_id = G.group_id
            GROUP BY G.group_id //그룹별이니까 그룹의 pk로
            HAVING AVG(I.age) BETWEEN 20 AND 25
         */

            //Tuple 대신에 DTO 사용을 원함
            //DTO에 담아야 하는 것 => .select(idol.group.groupName, idol.age.avg())

//            List<Tuple> result = factory
//                    .select(idol.group.groupName, idol.age.avg())
//                    .from(idol) //Idol 객체 안애 group가 있음 조인이 나감
//                    .groupBy(idol.group) //그룹을 참조하는 순간 join이 나감
//                    .having(idol.age.avg().between(20, 25))
//                    .fetch();

            // => DTO 사용 시작

            // Projections : 커스텀 DTO를 포장해주는 객체
            List<GroupAverageAgeDto> result = factory
                        .select(

                                Projections.constructor(
                                        GroupAverageAgeDto.class, //사용할 클래스의 타입
                                        idol.group.groupName, // dto 생성자 그룹네임
                                        idol.age.avg() // dot 생성자 평균나이
                                        // 프로젝셔이 알아서 dto로 변함.
                                        // select 결과를 dto로 변환
                                        // GroupAverageAgeDto.class 에 명시한타입의 dto로 변환
                                        // dto에 @NoArgsConstructor @AllArgsConstructor 생성자가 있어야함.

                                )

                        )
                        .from(idol) //Idol 객체 안애 group가 있음 조인이 나감
                        .groupBy(idol.group) //그룹을 참조하는 순간 join이 나감
                        .having(idol.age.avg().between(20, 25))
                        .fetch();




            //then
            assertFalse(result.isEmpty());
            for (GroupAverageAgeDto dto : result) {
                String groupName = dto.getGroupName();
                double averageAge = dto.getAverageAge();

                System.out.println("\n\nGroup: " + groupName +
                        ", Average Age: " + averageAge);
            }

    }


/*
    @Test
    @DisplayName("연령대별로 그룹화하여 아이돌 수를 조회")
    void ageGroupTest() {


//            SELECT
//                CASE age WHEN BETWEEN 10 AND 19 THEN 10
//                CASE age WHEN BETWEEN 20 AND 29 THEN 20
//                CASE age WHEN BETWEEN 30 AND 39 THEN 30
//                END,
//                COUNT(idol_id)
//            FROM tbl_idol
//            GROUP BY
//                CASE age WHEN BETWEEN 10 AND 19 THEN 10
//                CASE age WHEN BETWEEN 20 AND 29 THEN 20
//                CASE age WHEN BETWEEN 30 AND 39 THEN 30
//                END



        //given

        // QueryDSL로 CASE WHEN THEN 표현식 만들기
        NumberExpression<Integer> ageGroupExpression = new CaseBuilder()
                .when(idol.age.between(10, 19)).then(10)
                .when(idol.age.between(20, 29)).then(20)
                .when(idol.age.between(30, 39)).then(30)
                .otherwise(0);

        //when
        List<Tuple> result = factory
                .select(ageGroupExpression, idol.count())
                .from(idol)
                .groupBy(ageGroupExpression)
                .fetch();

        //then
        assertFalse(result.isEmpty());
        for (Tuple tuple : result) {
            int ageGroupValue = tuple.get(ageGroupExpression);
            long count = tuple.get(idol.count());

            System.out.println("\n\nAge Group: " + ageGroupValue + "대, Count: " + count);
        }
    }


    @Test
    @DisplayName("아이돌의 그룹명과 평균 나이를 조회해서 평균 나이가 20세와 25세 사이인 그룹만 조회하기")
    void tttest() {
        // given

        // when
        List<Tuple> result = factory
                .select(idol.group, idol.age.avg())
                .from(idol)
                .groupBy(idol.group)
                .having(idol.age.avg().between(20, 25))
                .fetch();


        //        assertFalse(result.isEmpty());
        for (Tuple tuple : result) {
            String groupName = tuple.get(idol.group.groupName);
            double averageAge = tuple.get(idol.age.avg());

            System.out.println("\n\nGroup: " + groupName
                    + ", Average Age: " + averageAge);
        }
    }

    @Test
    @DisplayName("아이돌의 그룹명과 평균 나이를 조회해서 평균 나이가 20세와 25세 사이인 그룹만 조회하기(dto로 받기)")
    void dtotest() {
        // given

        // when

        // Projections ": 커스텀 DTO를 포장해주는 객체.
        List<GroupAverageDto> result = factory
                .select(Projections.constructor(
                        GroupAverageDto.class,
                        idol.group.groupName,
                        idol.age.avg()
                ))
                .from(idol)
                .groupBy(idol.group)
                .having(idol.age.avg().between(20, 25))
                .fetch();

        //then
        assertFalse(result.isEmpty());
        for (GroupAverageDto tuple : result) {
            String groupName = tuple.getGroupName();
            double averageAge = tuple.getAverageAge();

            System.out.println("\n\nGroup: " + groupName
                    + ", Average Age: " + averageAge);
        }
    }

    */


}



