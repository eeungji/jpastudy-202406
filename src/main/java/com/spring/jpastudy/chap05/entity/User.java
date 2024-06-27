package com.spring.jpastudy.chap05.entity;


import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@ToString(exclude = "purchaseList")
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "tbl_mtm_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "user_name")
    private String name;

    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Purchase> purchaseList = new ArrayList<>();

}


//상품과 유저 다대다 관계
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "user_id")
//    private Long id;
//
//    @Column(name = "user_name")
//    private String name;
//
//
//    //여러개 구매 가능
//    //유저입장에서(나를 기준으로 생각) 1: 다
//    @OneToMany(mappedBy = "", orphanRemoval = true, cascade = CascadeType.ALL)
//    private  List<Purchase> purchaseList = new ArrayList<>();

    //중간 테이블을 만들어서 일대다 관계로 만들어라
//    @ManyToOne
//    private List<Goods> goodsList;
