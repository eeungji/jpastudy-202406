package com.spring.jpastudy.chap06_querydsl.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spring.jpastudy.chap06_querydsl.dto.GroupAverageAgeDto;
import com.spring.jpastudy.chap06_querydsl.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLOutput;
import java.util.List;

//import static com.spring.jpastudy.chap06_querydsl.entity.QIdol.idol;
import static com.spring.jpastudy.chap06_querydsl.entity.QAlbum.*;
import static com.spring.jpastudy.chap06_querydsl.entity.QGroup.*;
import static com.spring.jpastudy.chap06_querydsl.entity.QIdol.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
@Transactional
//@Rollback(false)
class QueryDslJoinTest {

    @Autowired
    IdolRepository idolRepository;

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    AlbumRepositoy albumRepository;

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


        Album album1 = new Album("MAP OF THE SOUL 7", 2020, bts);
        Album album2 = new Album("FEARLESS", 2022, leSserafim);
        Album album3 = new Album("UNFORGIVEN", 2023, bts);
        Album album4 = new Album("ELEVEN", 2021, ive);
        Album album5 = new Album("LOVE DIVE", 2022, ive);
        Album album6 = new Album("OMG", 2023, newjeans);
        Album album7 = new Album("AFTER LIKE", 2022, ive);

        albumRepository.save(album1);
        albumRepository.save(album2);
        albumRepository.save(album3);
        albumRepository.save(album4);
        albumRepository.save(album5);
        albumRepository.save(album6);
        albumRepository.save(album7);
    }

    @Test
    @DisplayName("내부 조인 예제")
    void innerJoinTest() {
        //given

        //when
        List<Tuple> idolList = factory
                .select(idol, group)
                .from(idol)
                // 첫번쨰 파라미터는 from절에 있는 엔터티의 연관 객체
                // 두번째 파라미터는 실제로 조인할 엔터티
                .innerJoin(idol.group, group)
                .fetch();

        //then
        System.out.println("\n\n\n");
        for (Tuple tuple : idolList) {
            Idol foundIdol = tuple.get(idol);
            Group foundGroup = tuple.get(group);
            System.out.println(foundIdol);
            System.out.println(foundGroup);
        }

        System.out.println("\n\n\n");
    }


    @Test
    @DisplayName("Left Outer Join")
    void outerJoinTest() {
        //given

        //when
        List<Tuple> result = factory
                .select(idol, group)
                .from(idol)
                .leftJoin(idol.group, group) //아이돌에있는그룹아이디, q그룹에있는그룹아이디
                .fetch();

        //then
        assertFalse(result.isEmpty());
        for (Tuple tuple : result) {
            Idol i = tuple.get(idol);
            Group g = tuple.get(group);

            System.out.println("\nIdol: " + i.getIdolName()
                    + ", Group: "
                    + (g != null ? g.getGroupName() : "솔로가수"));
        }

    }

    @Test
    @DisplayName("특정 그룹에 속한 아이돌의 정보 조회")
    void pratice1Test() {
        //given
        String groupName = "아이브";
        //when
        List<Tuple> result = factory
                .select(idol, group)
                .from(idol)
                .innerJoin(idol.group, group)
                .where(group.groupName.eq(groupName)) //조인후 필터링
                .fetch();

        //then
        assertFalse(result.isEmpty());
        result.forEach(tuple -> {
            Idol foundIdol = tuple.get(idol);
            Group foundGroup = tuple.get(group);
            System.out.printf("\n이름: %s, 그룹명: %s\n\n"
                    , foundIdol.getIdolName(), foundGroup.getGroupName());
        });
    }

    @Test
    @DisplayName("그룹별 평균 나이 계산")
    void practice2Test() {
        //given


        //when
        List<Tuple> result = factory
                .select(group.groupName, idol.age.avg())
                .from(idol)
                .innerJoin(idol.group, group)
                .groupBy(group.id)
                .having(idol.age.avg().goe(22))
                .fetch();

        //then
        assertFalse(result.isEmpty());
        result.forEach(tuple -> {
            String groupName = tuple.get(group.groupName);
            double averageAge = tuple.get(idol.age.avg());
            System.out.printf("\n그룹명: %s, 평균나이: %.2f\n\n"
                    , groupName, averageAge);
        });
    }

    @Test
    @DisplayName("특정연도에 발매된 앨범의 아이돌 정보 조회")
    void practice3Test() {
        //given
        int year = 2022;
        //when
        List<Tuple> result = factory
                .select(idol, album)
                .from(idol)
                .innerJoin(idol.group, group)
                .innerJoin(group.albums, album)
                .where(album.releaseYear.eq(year))
                .fetch();

        //then
        assertFalse(result.isEmpty());
        result.forEach(tuple -> {
            Idol foundIdol = tuple.get(idol);
            Album foundAlbum = tuple.get(album);
            System.out.printf("\n아이돌명: %s, 그룹명: %s, 앨범명: %s, " +
                            "발매년도: %d년\n\n"
                    , foundIdol.getIdolName(), foundIdol.getGroup().getGroupName(),
                    foundAlbum.getAlbumName(), foundAlbum.getReleaseYear());
        });
    }

    @Test
    @DisplayName("그룹별 가장 최근의 발매된 앨범 정보 조회")
    void subqueryTest2() {

        /*

            SELECT G.group_name, A.album_name, A.release_year
            FROM tbl_group G
            INNER JOIN tbl_album A
            ON G.group_id = A.group_id
            WHERE A.album_id IN (
                    SELECT S.album_id
                    FROM tbl_album S
                    WHERE S.group_id = A.group_id
                        AND (
                            SELECT MAX(release_year)
                            FROM tbl_album
                            WHERE S.group_id = A.group_id
                        )
            )

         */

        //given
        QAlbum albumA = new QAlbum("albumA");
        QAlbum albumS = new QAlbum("albumS");

        //when
        List<Tuple> result = factory
                .select(group.groupName, albumA.albumName, albumA.releaseYear)
                .from(group)
                .innerJoin(group.albums, albumA)
                .where(albumA.id.in(
                        JPAExpressions
                                .select(albumS.id)
                                .from(albumS)
                                .where(albumS.group.id.eq(albumA.group.id)
                                        .and(albumS.releaseYear.eq(
                                                JPAExpressions
                                                        .select(albumS.releaseYear.max())
                                                        .from(albumS)
                                                        .where(albumS.group.id.eq(albumA.group.id))
                                        ))
                                )

                ))
                .distinct()
                .fetch();

        //then
        assertFalse(result.isEmpty());
        for (Tuple tuple : result) {
            String groupName = tuple.get(group.groupName);
            String albumName = tuple.get(albumA.albumName);
            Integer releaseYear = tuple.get(albumA.releaseYear);

            System.out.println("\nGroup: " + groupName
                    + ", Album: " + albumName
                    + ", Release Year: " + releaseYear);
        }
    }

    @Test
    @DisplayName("특정 연도에 발매된 앨범 수가 2개 이상인 그룹 조회")
    void testFindGroupsWithMultipleAlbumsInYear() {
        int targetYear = 2022;

        QAlbum subAlbum = new QAlbum("subAlbum");

        // 서브쿼리: 각 그룹별로 특정 연도에 발매된 앨범 수를 계산
        JPQLQuery<Long> subQuery = JPAExpressions
                .select(subAlbum.group.id)
                .from(subAlbum)
                .where(subAlbum.releaseYear.eq(targetYear))
                .groupBy(subAlbum.group.id)
                .having(subAlbum.count().goe(2L));

        // 메인쿼리: 서브쿼리의 결과와 일치하는 그룹 조회
        List<Group> result = factory
                .selectFrom(group)
                .where(group.id.in(subQuery))
                .fetch();

        assertFalse(result.isEmpty());
        for (Group g : result) {
            System.out.println("\nGroup: " + g.getGroupName());

        }
    }
}


