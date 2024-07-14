package com.spring.jpastudy.chap06_querydsl.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "tb_idol")
@Setter @Getter
@ToString(exclude = "group")
@EqualsAndHashCode(of = "id") //id로만 비교
@NoArgsConstructor(access = AccessLevel.PROTECTED) //기본생성자 큰 의미 없음.
public class Idol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idol_id")
    private Long id;

    private String idolName;

    private int age;

    //한 그룹에 여러명의 아이돌이 있음.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;


    public Idol(String idolName, int age, Group group) {
        this.idolName = idolName;
        this.age = age;
        if (group != null) {
            changeGroup(group);
        }
    }

    //양방향 연관관계에서 변화가 일어났을 때 양쪽에서 변화를 시켜줘야 함.
    public void changeGroup(Group group) {
        this.group = group;
        group.getIdols().add(this);
    }
}
