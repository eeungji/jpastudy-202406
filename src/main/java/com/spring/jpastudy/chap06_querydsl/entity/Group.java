package com.spring.jpastudy.chap06_querydsl.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = {"idols"})
@EqualsAndHashCode(of = "id")
@Table(name = "tbl_group")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private Long id;

    private String groupName;

    //mappedBy 아이돌쪽에서 나를 뭐라고 부르고 있는지
    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Idol> idols = new ArrayList<>();

    public Group(String groupName) {
        this.groupName = groupName;
    }

    public void addIdol(Idol idol) {
        idols.add(idol);
        idol.setGroup(this);
    }

    public void removeIdol(Idol idol) {
        idols.remove(idol);
        idol.setGroup(null);
    }
}
