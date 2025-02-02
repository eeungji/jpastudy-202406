package com.spring.jpastudy.chap02.entity.repository;

import com.spring.jpastudy.chap02.entity.Student;
import lombok.Setter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, String> {

// 쿼리메서드 : 메서드 이름에 특별한 규칙을 적용하면 SQL이 규칙에 맞게 생성됨
    List<Student> findByName(String name); //단일조회가 아니기 때문에 Optional X

    //조건 2개일 경우(도시이름과, 전공으로 학생 조회)
    List<Student> findByCityAndMajor(String city, String major);

    // where major like'%major%' : major 포함
    List<Student> findByMajorContaining(String major);

    // where major like 'major%' :
    List<Student> findByMajorStartingWith(String major);

    // where major like '%major'
    List<Student> findByMajorEndingWith(String major);

    // where age <= ? : 주어진 나이 이하인 학생들을 찾는데 사용
//    List<Student> findByAgeLessThanEqual(int age);

    // 순수한 sql 사용, native sql 사용하기, db에 stu_name, 두번째 파라미터 중요
    @Query(value = "SELECT * FROM tbl_student WHERE stu_name = :snm OR city = :city", nativeQuery = true)
    List<Student> getStudentByNameOrCity(@Param("snm") String name, @Param("city") String city);

    @Query(value = "SELECT * FROM tbl_student WHERE stu_name = ?1 OR city = ?2", nativeQuery = true)
    List<Student> getStudentByNameOrCity2(String name, String city);


    /*

     - JPQL

    SELECT 엔터티별칭
    FROM 엔터티클래스명 AS 엔터티별칭
    WHERE 별칭.필드명

    ex) native - SELECT * FROM tbl_student WHERE stu_name = ?
        JPQL   - SELECT st FROM Student AS st WHERE st.name = ?

    //AS 생략 가능

     */

    // 도시명으로 학생 1명을 단일 조회
    // 학생이 1명이기 때문에 조회시 NULL이 나올 수도 있기 때문에 Optional 사용(null safe 문법)
    // Student를 st로 정의, ?1 과 city 매칭
    @Query(value = "SELECT st FROM Student st WHERE st.city = ?1", nativeQuery = false ) //JPQL 사용시   nativeQuery = false 생략 가능
    Optional<Student> getByCityWithJPQL(String city);


    //특정 이름이 포함된 학생 리스트 조회하기
    @Query("SELECT stu From Student stu WHERE stu.name LIKE %?1%")
    List<Student> searchByNameWithJPQL(String name);

    // JPQL로 갱신 처리하기(이름과 도시가 동시에(AND) 일치해야 지워줄거다~)
    @Modifying //SELECT가 아니면 무조건 붙이기
    @Query("DELETE FROM Student s WHERE s.name = ?1 AND s.city =?2")
    void deleteByNameAndCityWithJPQL(String name, String city);


    //기본적으로 셀렉트에만 동작하게 되어있다 삽입, 삭제 ,수정 같은 애들은 어노테이션 붙여야함.

}





















    /*
    //쿼리 메서드라는 문법 사용 가능
    //쿼리 메서드 : 메서드에 이름에 특별한 규칙을 적용하면 SQL이 규칙에 맞게 생성됨

    //룰이다. findBy + 필드명(name) (String name 전달)
    //이름 다중행 조회 될 수도 있음. 이름 중복
    List<Student> findByName(String name);

    //도시 이름 찾기
    List<Student> findByCityOrMajor(String city, String major);

    // where major like '%major%'
    List<Student> findByMajorContaining(String major);

    // where major like 'major%' 김으로 시작~
    List<Student> findByMajorStartingWith(String major);

    // where major like '%major' ~로 끝나는것
    List<Student> findByMajorEndingWith(String major);

    // where age <= ?
//    List<Student> findByAgeLessThanEqual(int age);

    // native sql 사용하기 db 컬럼 확인하기
    @Query(value = "SELECT * FROM tbl_student WHERE stu_name = :snm OR city  = :city", nativeQuery = true)
    List<Student> getStudentByNameOrCity(@Param("snm") String name, @Param("city") String city);

    @Query(value = "SELECT * FROM tbl_student WHERE stu_name = ?1 OR city  = ?2", nativeQuery = true)
    List<Student> getStudentByNameOrCity2(String name, String city);


    /*
        - JPQL

        SELECT 엔터티별칭
        FROM 엔터티클래스명 AS 엔터티별칭
        WHERE 별칭.필드명

        ex) native - SELECT * FROM tbl_student WHERE stu_name = ?
            JPQL   - SELECT st FROM Student AS st WHERE st.name = ?




    // 도시명으로 학생 1명을 단일 조회
    @Query(value = "SELECT st FROM Student st WHERE st.city = ?1")
    Optional<Student> getByCityWithJPQL(String city);

    // 특정 이름이 포함된 학생 리스트 조회하기
    @Query("SELECT stu FROM Student stu WHERE stu.name LIKE %?1%")
    List<Student> searchByNameWithJPQL(String name);

    // JPQL로 갱신 처리하기
    @Modifying // SELECT가 아니면 무조건 붙이기
    @Query("DELETE FROM Student s WHERE s.name = ?1 AND s.city = ?2")
    void deleteByNameAndCityWithJPQL(String name, String city);
}
*/












