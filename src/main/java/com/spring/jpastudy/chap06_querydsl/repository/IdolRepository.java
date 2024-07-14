package com.spring.jpastudy.chap06_querydsl.repository;

import com.spring.jpastudy.chap06_querydsl.entity.Idol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

//다중상속
public interface IdolRepository extends JpaRepository<Idol, Long>,
IdolCustomerRepository {

    @Query("SELECT i FROM Idol i ORDER BY i.age DESC")
    List<Idol> findAllBySorted();

}
