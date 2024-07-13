package com.spring.jpastudy.chap02.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "tbl_student")
public class Student {

    // 랜덤문자로 PK지정, 전역적으로 같은 id가 없게 됨.
    @Id
    @Column(name = "stu_id")
    @GeneratedValue(generator = "uid") //아래 이름 매칭
    @GenericGenerator(strategy = "uuid", name = "uid")
    private String id;

    @Column(name = "stu_name", nullable = false)
    private String name;


    private String city;

    private String major;

}




//@Setter
//@Getter
//@ToString
//@EqualsAndHashCode(of = "id")
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//
//
//@Entity
//@Table(name = "tbl_student")
//public class Student {
//
//    // 랜덤문자로 PK지정(아이디 생성시 좀 더 괜찮은 방법)
//    @Id
//    @Column(name = "stu_id")
//    @GeneratedValue(generator = "uid")
//    @GenericGenerator(strategy = "uuid", name = "uid")
//    private String id;
//
//    @Column(name = "stu_name", nullable = false)
//    private String name;
//
//    private String city;
//
//    private String major;
